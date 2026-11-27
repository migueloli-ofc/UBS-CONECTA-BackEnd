package br.com.ubsconecta.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.ubsconecta.model.Horarios;
import br.com.ubsconecta.model.FuncionamentoUbs;

import br.com.ubsconecta.repository.HorariosRepository;
import br.com.ubsconecta.repository.FuncionamentoUbsRepository;
import br.com.ubsconecta.repository.PeriodoAgendamentoRepository;

@Service
public class HorariosService {

    private static final Integer UBS_PILOTO = 4;

    private static final ZoneId FUSO =
        ZoneId.of("America/Fortaleza");

    private final HorariosRepository horariosRepository;

    private final FuncionamentoUbsRepository
        funcionamentoUbsRepository;

    private final PeriodoAgendamentoRepository
        periodoAgendamentoRepository;

    public HorariosService(
        HorariosRepository horariosRepository,
        FuncionamentoUbsRepository funcionamentoUbsRepository,
        PeriodoAgendamentoRepository periodoAgendamentoRepository
    ) {
        this.horariosRepository = horariosRepository;
        this.funcionamentoUbsRepository = funcionamentoUbsRepository;
        this.periodoAgendamentoRepository = periodoAgendamentoRepository;
    }


    public List<Horarios> listarTodos() {
        return horariosRepository.findAll();
    }


    public boolean periodoAberto(
        Integer idUbs,
        Integer mes,
        Integer ano
    ) {
        return periodoAgendamentoRepository
            .existsByIdUbsAndMesAndAnoAndAbertoTrue(
                idUbs,
                mes,
                ano
            );
    }


    private void validarUbs(Integer idUbs) {

        if (!Objects.equals(idUbs, UBS_PILOTO)) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Agendamento disponível apenas para a UBS de Lagoa do Mato."
            );
        }
    }


    private boolean dataBloqueada(LocalDate data) {

        return data.equals(LocalDate.of(2026, 12, 25));
    }


    private List<LocalDateTime> gerarHorariosValidos(
        Integer idUbs,
        YearMonth periodo
    ) {

        List<LocalDateTime> horariosValidos =
            new ArrayList<>();

        List<FuncionamentoUbs> expedientes =
            funcionamentoUbsRepository
                .findByIdUbsAndAtivoTrue(idUbs);

        LocalDateTime agora =
            LocalDateTime.now(FUSO);

        for (
            LocalDate dia = periodo.atDay(1);
            !dia.isAfter(periodo.atEndOfMonth());
            dia = dia.plusDays(1)
        ) {

            if (
                dia.isBefore(agora.toLocalDate())
                || dataBloqueada(dia)
            ) {
                continue;
            }

            int diaSemana =
                dia.getDayOfWeek().getValue();

            for (FuncionamentoUbs expediente : expedientes) {

                if (expediente.getDiaSemana() != diaSemana) {
                    continue;
                }

                int intervalo =
                    expediente.getDuracaoConsulta()
                    + expediente.getTolerancia();

                LocalDateTime inicio =
                    dia.atTime(expediente.getHoraAbertura());

                LocalDateTime fechamento =
                    dia.atTime(expediente.getHoraFechamento());

                while (
                    !inicio.plusMinutes(intervalo)
                        .isAfter(fechamento)
                ) {

                    if (inicio.isAfter(agora)) {
                        horariosValidos.add(inicio);
                    }

                    inicio = inicio.plusMinutes(intervalo);
                }
            }
        }

        return horariosValidos;
    }


    @Transactional
    public List<Horarios> buscarDisponiveis(
        Integer idUbs,
        Integer mes,
        Integer ano
    ) {

        validarUbs(idUbs);

        if (
            mes == null || mes < 1 || mes > 12
            || ano == null || ano < 2026 || ano > 2100
        ) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Mês ou ano inválido."
            );
        }

        YearMonth periodo = YearMonth.of(ano, mes);

        YearMonth periodoAtual =
            YearMonth.now(FUSO);

        if (
            periodo.isBefore(periodoAtual)
            || !periodoAberto(idUbs, mes, ano)
        ) {
            return List.of();
        }

        List<LocalDateTime> horariosValidos =
            gerarHorariosValidos(idUbs, periodo);

        for (LocalDateTime dataHora : horariosValidos) {

            horariosRepository.inserirHorarioSeNaoExistir(
                idUbs,
                dataHora
            );
        }

        LocalDateTime inicio =
            periodo.atDay(1).atStartOfDay();

        LocalDateTime fim =
            periodo.plusMonths(1)
                .atDay(1).atStartOfDay();

        LocalDateTime agora =
            LocalDateTime.now(FUSO);

        return horariosRepository
            .findByIdUbsAndDataHoraGreaterThanEqualAndDataHoraLessThanOrderByDataHoraAsc(
                idUbs,
                inicio,
                fim
            )
            .stream()
            .filter(h -> Boolean.TRUE.equals(h.getDisponivel()))
            .filter(h -> h.getDataHora().isAfter(agora))
            .filter(h -> horariosValidos.contains(h.getDataHora()))
            .toList();
    }


    public void validarHorarioAgendamento(Horarios horario) {

        validarUbs(horario.getIdUbs());

        LocalDateTime dataHora = horario.getDataHora();

        if (
            dataHora == null
            || !dataHora.isAfter(LocalDateTime.now(FUSO))
        ) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "O horário selecionado já passou."
            );
        }

        YearMonth periodo = YearMonth.from(dataHora);

        if (!periodoAberto(
            horario.getIdUbs(),
            periodo.getMonthValue(),
            periodo.getYear()
        )) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "O mês selecionado não está liberado."
            );
        }

        if (!gerarHorariosValidos(
            horario.getIdUbs(),
            periodo
        ).contains(dataHora)) {

            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Horário fora do expediente permitido."
            );
        }
    }
}
