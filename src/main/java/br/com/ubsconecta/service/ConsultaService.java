
package br.com.ubsconecta.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.ubsconecta.model.Consulta;
import br.com.ubsconecta.model.Horarios;
import br.com.ubsconecta.model.Paciente;
import br.com.ubsconecta.model.Profissional;
import br.com.ubsconecta.repository.ConsultaRepository;
import br.com.ubsconecta.repository.HorariosRepository;
import br.com.ubsconecta.repository.ProfissionalRepository;

@Service
public class ConsultaService {

    private final ProfissionalRepository profissionalRepository;
	private final ConsultaRepository consultaRepository;//Como o java vai reconhecer as variaveis
    private final PacienteService pacienteService;
    private final HorariosRepository horariosRepository;

    private static final Set<String> STATUS_EXIBIDOS = ///status da consulta, pelo horario
            Set.of("solicitada", "agendada", "confirmada");

    private static final List<String> STATUS_OCUPADOS =
            List.of("Solicitada", "Agendada", "Confirmada");

    public ConsultaService(
            ConsultaRepository consultaRepository,
            PacienteService pacienteService,
            HorariosRepository horariosRepository,
            ProfissionalRepository profissionalRepository) {

        this.consultaRepository = consultaRepository;
        this.pacienteService = pacienteService;
        this.horariosRepository = horariosRepository;
        this.profissionalRepository = profissionalRepository;
    }

    public List<Consulta> listarTodos() {
        return consultaRepository.findAll();
    }
    
    public List<Consulta> buscarConsultasDaUbs(Integer idUsuario) {

        Profissional profissional = profissionalRepository
                .findByIdUsuario(idUsuario);

        if (profissional == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Profissional não encontrado.");
        }

        return consultaRepository
                .findByIdUbsOrderByDataAtendimentoAsc(
                        profissional.getIdUbs());
    }
    
    @Transactional
    public Consulta confirmarConsulta(Integer idConsulta, Integer idUsuario) {

        Profissional profissional = profissionalRepository
                .findByIdUsuario(idUsuario);

        if (profissional == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Profissional não encontrado.");
        }

        Consulta consulta = consultaRepository.findById(idConsulta)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Consulta não encontrada."));

        if (!profissional.getIdUbs().equals(consulta.getIdUbs())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Esta consulta pertence a outra UBS.");
        }

        if (consulta.getIdProfissional() != null
                && !consulta.getIdProfissional().equals(profissional.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Esta consulta já está vinculada a outro profissional.");
        }

        if (!"Solicitada".equalsIgnoreCase(consulta.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Esta solicitação não está mais pendente.");
        }

        consulta.setIdProfissional(profissional.getId());
        consulta.setStatus("Agendada");

        return consultaRepository.save(consulta);
    }

    public List<Consulta> buscarProximasPorUsuario(Integer idUsuario) {

        Paciente paciente = pacienteService.buscarPorUsuario(idUsuario);

        if (paciente == null) {
            return List.of();
        }

        return consultaRepository
                .findByIdPacienteAndDataAtendimentoAfterOrderByDataAtendimentoAsc(
                        paciente.getId(),
                        LocalDateTime.now())
                .stream()
                .filter(consulta -> consulta.getStatus() != null)
                .filter(consulta -> STATUS_EXIBIDOS.contains(
                        consulta.getStatus().trim().toLowerCase()))
                .toList();
    }

    @Transactional
    public Consulta salvar(Consulta consulta) {

        if (consulta.getIdHorarios() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Selecione um horário.");
        }

        Horarios horario = horariosRepository
                .buscarPorIdComBloqueio(consulta.getIdHorarios())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Horário não encontrado."));

        if (!Boolean.TRUE.equals(horario.getDisponivel())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Este horário não está mais disponível.");
        }

        if (horario.getDataHora() == null
                || !horario.getDataHora().isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Selecione um horário futuro.");
        }

        boolean jaReservado =
                consultaRepository.existsByIdHorariosAndStatusIn(
                        consulta.getIdHorarios(),
                        STATUS_OCUPADOS);

        if (jaReservado) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Este horário já possui uma solicitação.");
        }

        consulta.setDataAtendimento(horario.getDataHora());

        if (consulta.getStatus() == null
                || consulta.getStatus().isBlank()) {
            consulta.setStatus("Solicitada");
        }

        Consulta salva = consultaRepository.save(consulta);

        horario.setDisponivel(false);
        horariosRepository.save(horario);

        return salva;
    }


    @Transactional
    public Consulta recusarConsulta(Integer idConsulta, Integer idUsuario) {

        Profissional profissional = profissionalRepository.findByIdUsuario(idUsuario);

        if (profissional == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Profissional não encontrado."
            );
        }

        Consulta consulta = consultaRepository.findById(idConsulta)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Consulta não encontrada."
            ));

        if (!profissional.getIdUbs().equals(consulta.getIdUbs())) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Esta consulta pertence a outra UBS."
            );
        }

        if (!"Solicitada".equalsIgnoreCase(consulta.getStatus())) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Esta solicitação não está mais pendente."
            );
        }

        consulta.setStatus("Recusada");

        Consulta recusada = consultaRepository.save(consulta);

        if (consulta.getIdHorarios() != null) {

            Horarios horario = horariosRepository
                .buscarPorIdComBloqueio(consulta.getIdHorarios())
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Horário não encontrado."
                ));

            boolean aindaOcupado = consultaRepository.existsByIdHorariosAndStatusIn(
                consulta.getIdHorarios(),
                STATUS_OCUPADOS
            );

            if (!aindaOcupado) {

                boolean horarioFuturo = horario.getDataHora() != null
                        && horario.getDataHora().isAfter(LocalDateTime.now());

                horario.setDisponivel(horarioFuturo);
                horariosRepository.save(horario);
            }
        }

        return recusada;
    }
}
