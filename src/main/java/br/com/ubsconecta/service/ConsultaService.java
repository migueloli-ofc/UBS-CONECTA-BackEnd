
package br.com.ubsconecta.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import br.com.ubsconecta.model.Consulta;
import br.com.ubsconecta.model.Horarios;
import br.com.ubsconecta.model.Paciente;
import br.com.ubsconecta.model.Profissional;
import br.com.ubsconecta.model.Ubs;
import br.com.ubsconecta.repository.ConsultaRepository;
import br.com.ubsconecta.repository.HorariosRepository;
import br.com.ubsconecta.repository.ProfissionalRepository;
import br.com.ubsconecta.repository.PacienteRepository;
import br.com.ubsconecta.repository.UbsRepository;


@Service
public class ConsultaService {

    private final UbsRepository ubsRepository;
	private final ProfissionalRepository profissionalRepository;
	private final ConsultaRepository consultaRepository;
    private final PacienteService pacienteService;
    private final HorariosRepository horariosRepository;
    private final PacienteRepository pacienteRepository;
    private final HorariosService horariosService;
    
    private static final Set<String> STATUS_EXIBIDOS =
            Set.of("solicitada", "agendada", "confirmada");

    private static final List<String> STATUS_OCUPADOS =
            List.of("Solicitada", "Agendada", "Confirmada");

    public ConsultaService(
            ConsultaRepository consultaRepository,
            PacienteService pacienteService,
            HorariosRepository horariosRepository,
            ProfissionalRepository profissionalRepository,
            PacienteRepository pacienteRepository,
            UbsRepository ubsRepository,
            HorariosService horariosService) {

        this.consultaRepository = consultaRepository;
        this.pacienteService = pacienteService;
        this.horariosRepository = horariosRepository;
        this.profissionalRepository = profissionalRepository;
        this.pacienteRepository = pacienteRepository;
        this.ubsRepository = ubsRepository;
        this.horariosService = horariosService;
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

    List<Consulta> consultas = consultaRepository
            .findByIdUbsOrderByDataAtendimentoAsc(
                    profissional.getIdUbs());

   
    List<Integer> idsPacientes = consultas.stream()
            .map(Consulta::getIdPaciente)
            .filter(Objects::nonNull)
            .distinct()
            .toList();

    
    Map<Integer, Paciente> pacientes = pacienteRepository
            .findAllById(idsPacientes)
            .stream()
            .collect(Collectors.toMap(
                    Paciente::getId,
                    paciente -> paciente));

   
    for (Consulta consulta : consultas) {

        Paciente paciente = pacientes.get(
                consulta.getIdPaciente());

        consulta.setNomePaciente(
                paciente != null && paciente.getNome() != null
                        ? paciente.getNome()
                        : "Paciente não identificado");
    }

    return consultas;
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
	
	 
	
	    if (!Objects.equals(consulta.getIdUbs(), 4)) {
	        throw new ResponseStatusException(
	            HttpStatus.BAD_REQUEST,
	            "O agendamento está disponível apenas para a UBS de Lagoa do Mato."
	        );
	    }
	
	    
	
	    if (consulta.getIdPaciente() == null
	            || !pacienteRepository.existsById(
	                consulta.getIdPaciente())) {
	
	        throw new ResponseStatusException(
	            HttpStatus.BAD_REQUEST,
	            "Paciente não encontrado."
	        );
	    }
	
	    
	
	    if (consulta.getIdHorarios() == null) {
	        throw new ResponseStatusException(
	            HttpStatus.BAD_REQUEST,
	            "Selecione um horário."
	        );
	    }
	
	    
	
	    Horarios horario = horariosRepository
	        .buscarPorIdComBloqueio(
	            consulta.getIdHorarios()
	        )
	        .orElseThrow(() -> new ResponseStatusException(
	            HttpStatus.NOT_FOUND,
	            "Horário não encontrado."
	        ));
	
	    
	
	    if (!Objects.equals(
	            horario.getIdUbs(),
	            consulta.getIdUbs())) {
	
	        throw new ResponseStatusException(
	            HttpStatus.BAD_REQUEST,
	            "O horário selecionado pertence a outra UBS."
	        );
	    }
	
	    
	
	    if (!Boolean.TRUE.equals(horario.getDisponivel())) {
	        throw new ResponseStatusException(
	            HttpStatus.CONFLICT,
	            "Este horário não está mais disponível."
	        );
	    }
	
	  
	
	    horariosService.validarHorarioAgendamento(horario);
	
	    
	
	    boolean jaReservado =
	        consultaRepository.existsByIdHorariosAndStatusIn(
	            consulta.getIdHorarios(),
	            STATUS_OCUPADOS
	        );
	
	    if (jaReservado) {
	        throw new ResponseStatusException(
	            HttpStatus.CONFLICT,
	            "Este horário já possui uma solicitação."
	        );
	    }
	
	    
	
	    consulta.setId(null);
	    consulta.setIdProfissional(null);
	    consulta.setDataAtendimento(horario.getDataHora());
	
	    consulta.setDataSolicitacao(
	        LocalDateTime.now(
	            java.time.ZoneId.of("America/Fortaleza")
	        )
	    );
	
	    consulta.setStatus("Solicitada");
	
	    // Salvar a consulta
	
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
    


	public List<Consulta> buscarConsultasPorPaciente(Integer idUsuario) {
	
	    Paciente paciente = pacienteService.buscarPorUsuario(idUsuario);
	
	    if (paciente == null) {
	        throw new ResponseStatusException(
	                HttpStatus.NOT_FOUND,
	                "Paciente não encontrado.");
	    }
	
	    List<Consulta> consultas =
	            consultaRepository.findByIdPaciente(paciente.getId());
	
	    List<Integer> idsUbs = consultas.stream()
	            .map(Consulta::getIdUbs)
	            .filter(Objects::nonNull)
	            .distinct()
	            .toList();
	
	    Map<Integer, Ubs> unidades = ubsRepository
	            .findAllById(idsUbs)
	            .stream()
	            .collect(Collectors.toMap(Ubs::getId, ubs -> ubs));
	
	    for (Consulta consulta : consultas) {
	
	        Ubs ubs = unidades.get(consulta.getIdUbs());
	
	        consulta.setNomeUbs(
	                ubs != null && ubs.getNomeUbs() != null
	                        ? ubs.getNomeUbs()
	                        : "Unidade não identificada"
	        );
	    }
	
	    return consultas;
	}

	

}
