[1mdiff --git a/src/main/java/br/com/ubsconecta/controller/HorariosController.java b/src/main/java/br/com/ubsconecta/controller/HorariosController.java[m
[1mindex 1b671f2..f203dcf 100644[m
[1m--- a/src/main/java/br/com/ubsconecta/controller/HorariosController.java[m
[1m+++ b/src/main/java/br/com/ubsconecta/controller/HorariosController.java[m
[36m@@ -1,23 +1,30 @@[m
[32m+[m
 package br.com.ubsconecta.controller;[m
 [m
 import java.util.List;[m
 [m
[32m+[m[32mimport org.springframework.web.bind.annotation.CrossOrigin;[m
 import org.springframework.web.bind.annotation.GetMapping;[m
[31m-import org.springframework.web.bind.annotation.PostMapping;[m
[31m-import org.springframework.web.bind.annotation.RequestBody;[m
 import org.springframework.web.bind.annotation.RequestMapping;[m
[32m+[m[32mimport org.springframework.web.bind.annotation.RequestParam;[m
 import org.springframework.web.bind.annotation.RestController;[m
 [m
 import br.com.ubsconecta.model.Horarios;[m
 import br.com.ubsconecta.service.HorariosService;[m
 [m
[32m+[m[32m@CrossOrigin(origins = {[m
[32m+[m[32m    "http://127.0.0.1:5500",[m
[32m+[m[32m    "http://localhost:5500"[m
[32m+[m[32m})[m
 @RestController[m
 @RequestMapping("/api/horarios")[m
 public class HorariosController {[m
 [m
     private final HorariosService horariosService;[m
 [m
[31m-    public HorariosController(HorariosService horariosService) {[m
[32m+[m[32m    public HorariosController([m
[32m+[m[32m        HorariosService horariosService[m
[32m+[m[32m    ) {[m
         this.horariosService = horariosService;[m
     }[m
 [m
[36m@@ -26,8 +33,17 @@[m [mpublic class HorariosController {[m
         return horariosService.listarTodos();[m
     }[m
 [m
[31m-    @PostMapping[m
[31m-    public Horarios salvar(@RequestBody Horarios horarios) {[m
[31m-        return horariosService.salvar(horarios);[m
[32m+[m[32m    @GetMapping("/disponiveis")[m
[32m+[m[32m    public List<Horarios> buscarDisponiveis([m
[32m+[m[32m        @RequestParam Integer idUbs,[m
[32m+[m[32m        @RequestParam Integer mes,[m
[32m+[m[32m        @RequestParam Integer ano[m
[32m+[m[32m    ) {[m
[32m+[m
[32m+[m[32m        return horariosService.buscarDisponiveis([m
[32m+[m[32m            idUbs,[m
[32m+[m[32m            mes,[m
[32m+[m[32m            ano[m
[32m+[m[32m        );[m
     }[m
[31m-}[m
\ No newline at end of file[m
[32m+[m[32m}[m
[1mdiff --git a/src/main/java/br/com/ubsconecta/model/Horarios.java b/src/main/java/br/com/ubsconecta/model/Horarios.java[m
[1mindex edba647..80e7a00 100644[m
[1m--- a/src/main/java/br/com/ubsconecta/model/Horarios.java[m
[1m+++ b/src/main/java/br/com/ubsconecta/model/Horarios.java[m
[36m@@ -1,3 +1,4 @@[m
[32m+[m
 package br.com.ubsconecta.model;[m
 [m
 import java.time.LocalDateTime;[m
[36m@@ -17,13 +18,16 @@[m [mpublic class Horarios {[m
     @GeneratedValue(strategy = GenerationType.IDENTITY)[m
     private Integer id;[m
 [m
[31m-    @Column(name = "data_hora")[m
[32m+[m[32m    @Column(name = "data_hora", nullable = false)[m
     private LocalDateTime dataHora;[m
 [m
[32m+[m[32m    @Column(nullable = false)[m
     private Boolean disponivel;[m
 [m
[31m-    public Horarios() {[m
[32m+[m[32m    @Column(name = "id_ubs", nullable = false)[m
[32m+[m[32m    private Integer idUbs;[m
 [m
[32m+[m[32m    public Horarios() {[m
     }[m
 [m
     public Integer getId() {[m
[36m@@ -49,4 +53,12 @@[m [mpublic class Horarios {[m
     public void setDisponivel(Boolean disponivel) {[m
         this.disponivel = disponivel;[m
     }[m
[31m-}[m
\ No newline at end of file[m
[32m+[m
[32m+[m[32m    public Integer getIdUbs() {[m
[32m+[m[32m        return idUbs;[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m[32m    public void setIdUbs(Integer idUbs) {[m
[32m+[m[32m        this.idUbs = idUbs;[m
[32m+[m[32m    }[m
[32m+[m[32m}[m
[1mdiff --git a/src/main/java/br/com/ubsconecta/repository/HorariosRepository.java b/src/main/java/br/com/ubsconecta/repository/HorariosRepository.java[m
[1mindex b0cd82f..1bfeea7 100644[m
[1m--- a/src/main/java/br/com/ubsconecta/repository/HorariosRepository.java[m
[1m+++ b/src/main/java/br/com/ubsconecta/repository/HorariosRepository.java[m
[36m@@ -2,12 +2,14 @@[m
 package br.com.ubsconecta.repository;[m
 [m
 import java.time.LocalDateTime;[m
[32m+[m[32mimport java.util.List;[m
 import java.util.Optional;[m
 [m
 import org.springframework.data.jpa.repository.JpaRepository;[m
 import org.springframework.data.jpa.repository.Lock;[m
 import org.springframework.data.jpa.repository.Query;[m
 import org.springframework.data.repository.query.Param;[m
[32m+[m[32mimport org.springframework.data.jpa.repository.Modifying;[m
 [m
 import jakarta.persistence.LockModeType;[m
 [m
[36m@@ -17,9 +19,53 @@[m [mpublic interface HorariosRepository extends JpaRepository<Horarios, Integer> {[m
 [m
     boolean existsByDataHora(LocalDateTime dataHora);[m
 [m
[31m-    boolean existsByDataHoraAndIdNot(LocalDateTime dataHora, Integer id);[m
[32m+[m[32m    boolean existsByDataHoraAndIdNot([m
[32m+[m[32m        LocalDateTime dataHora,[m
[32m+[m[32m        Integer id[m
[32m+[m[32m    );[m
 [m
[32m+[m
[32m+[m[32m    boolean existsByIdUbsAndDataHora([m
[32m+[m[32m        Integer idUbs,[m
[32m+[m[32m        LocalDateTime dataHora[m
[32m+[m[32m    );[m
[32m+[m
[32m+[m[32m    boolean existsByIdUbsAndDataHoraAndIdNot([m
[32m+[m[32m        Integer idUbs,[m
[32m+[m[32m        LocalDateTime dataHora,[m
[32m+[m[32m        Integer id[m
[32m+[m[32m    );[m
[32m+[m
[32m+[m[32m    List<Horarios> findByIdUbsAndDataHoraGreaterThanEqualAndDataHoraLessThanOrderByDataHoraAsc([m
[32m+[m[32m        Integer idUbs,[m
[32m+[m[32m        LocalDateTime inicio,[m
[32m+[m[32m        LocalDateTime fim[m
[32m+[m[32m    );[m
[32m+[m
[32m+[m[41m    [m
     @Lock(LockModeType.PESSIMISTIC_WRITE)[m
     @Query("SELECT h FROM Horarios h WHERE h.id = :id")[m
[31m-    Optional<Horarios> buscarPorIdComBloqueio(@Param("id") Integer id);[m
[32m+[m[32m    Optional<Horarios> buscarPorIdComBloqueio([m
[32m+[m[32m        @Param("id") Integer id[m
[32m+[m[32m    );[m
[32m+[m[41m    [m
[32m+[m[32m    @Modifying[m
[32m+[m[32m    @Query(value = """[m
[32m+[m[32m        INSERT INTO horarios ([m
[32m+[m[32m            id_ubs,[m
[32m+[m[32m            data_hora,[m
[32m+[m[32m            disponivel[m
[32m+[m[32m        )[m
[32m+[m[32m        VALUES ([m
[32m+[m[32m            :idUbs,[m
[32m+[m[32m            :dataHora,[m
[32m+[m[32m            TRUE[m
[32m+[m[32m        )[m
[32m+[m[32m        ON CONFLICT (id_ubs, data_hora)[m
[32m+[m[32m        DO NOTHING[m
[32m+[m[32m        """, nativeQuery = true)[m
[32m+[m[32m    int inserirHorarioSeNaoExistir([m
[32m+[m[32m        @Param("idUbs") Integer idUbs,[m
[32m+[m[32m        @Param("dataHora") LocalDateTime dataHora[m
[32m+[m[32m    );[m
 }[m
[1mdiff --git a/src/main/java/br/com/ubsconecta/service/ConsultaService.java b/src/main/java/br/com/ubsconecta/service/ConsultaService.java[m
[1mindex 9f9bd8a..e946f49 100644[m
[1m--- a/src/main/java/br/com/ubsconecta/service/ConsultaService.java[m
[1m+++ b/src/main/java/br/com/ubsconecta/service/ConsultaService.java[m
[36m@@ -35,6 +35,7 @@[m [mpublic class ConsultaService {[m
     private final PacienteService pacienteService;[m
     private final HorariosRepository horariosRepository;[m
     private final PacienteRepository pacienteRepository;[m
[32m+[m[32m    private final HorariosService horariosService;[m
     [m
     private static final Set<String> STATUS_EXIBIDOS =[m
             Set.of("solicitada", "agendada", "confirmada");[m
[36m@@ -48,7 +49,8 @@[m [mpublic class ConsultaService {[m
             HorariosRepository horariosRepository,[m
             ProfissionalRepository profissionalRepository,[m
             PacienteRepository pacienteRepository,[m
[31m-            UbsRepository ubsRepository) {[m
[32m+[m[32m            UbsRepository ubsRepository,[m
[32m+[m[32m            HorariosService horariosService) {[m
 [m
         this.consultaRepository = consultaRepository;[m
         this.pacienteService = pacienteService;[m
[36m@@ -56,6 +58,7 @@[m [mpublic class ConsultaService {[m
         this.profissionalRepository = profissionalRepository;[m
         this.pacienteRepository = pacienteRepository;[m
         this.ubsRepository = ubsRepository;[m
[32m+[m[32m        this.horariosService = horariosService;[m
     }[m
 [m
     public List<Consulta> listarTodos() {[m
[36m@@ -170,59 +173,117 @@[m [mpublic List<Consulta> buscarConsultasDaUbs(Integer idUsuario) {[m
                 .toList();[m
     }[m
 [m
[31m-    @Transactional[m
[31m-    public Consulta salvar(Consulta consulta) {[m
[31m-[m
[31m-        if (consulta.getIdHorarios() == null) {[m
[31m-            throw new ResponseStatusException([m
[31m-                    HttpStatus.BAD_REQUEST,[m
[31m-                    "Selecione um horário.");[m
[31m-        }[m
[31m-[m
[31m-        Horarios horario = horariosRepository[m
[31m-                .buscarPorIdComBloqueio(consulta.getIdHorarios())[m
[31m-                .orElseThrow(() -> new ResponseStatusException([m
[31m-                        HttpStatus.NOT_FOUND,[m
[31m-                        "Horário não encontrado."));[m
[31m-[m
[31m-        if (!Boolean.TRUE.equals(horario.getDisponivel())) {[m
[31m-            throw new ResponseStatusException([m
[31m-                    HttpStatus.CONFLICT,[m
[31m-                    "Este horário não está mais disponível.");[m
[31m-        }[m
[31m-[m
[31m-        if (horario.getDataHora() == null[m
[31m-                || !horario.getDataHora().isAfter(LocalDateTime.now())) {[m
[31m-            throw new ResponseStatusException([m
[31m-                    HttpStatus.BAD_REQUEST,[m
[31m-                    "Selecione um horário futuro.");[m
[31m-        }[m
[31m-[m
[31m-        boolean jaReservado =[m
[31m-                consultaRepository.existsByIdHorariosAndStatusIn([m
[31m-                        consulta.getIdHorarios(),[m
[31m-                        STATUS_OCUPADOS);[m
[31m-[m
[31m-        if (jaReservado) {[m
[31m-            throw new ResponseStatusException([m
[31m-                    HttpStatus.CONFLICT,[m
[31m-                    "Este horário já possui uma solicitação.");[m
[31m-        }[m
[31m-[m
[31m-        consulta.setDataAtendimento(horario.getDataHora());[m
[31m-[m
[31m-        if (consulta.getStatus() == null[m
[31m-                || consulta.getStatus().isBlank()) {[m
[31m-            consulta.setStatus("Solicitada");[m
[31m-        }[m
[31m-[m
[31m-        Consulta salva = consultaRepository.save(consulta);[m
[31m-[m
[31m-        horario.setDisponivel(false);[m
[31m-        horariosRepository.save(horario);[m
[32m+[m[41m	[m
[32m+[m	[32m@Transactional[m
[32m+[m	[32mpublic Consulta salvar(Consulta consulta) {[m
[32m+[m[41m	[m
[32m+[m[41m	 [m
[32m+[m[41m	[m
[32m+[m	[32m    if (!Objects.equals(consulta.getIdUbs(), 4)) {[m
[32m+[m	[32m        throw new ResponseStatusException([m
[32m+[m	[32m            HttpStatus.BAD_REQUEST,[m
[32m+[m	[32m            "O agendamento está disponível apenas para a UBS de Lagoa do Mato."[m
[32m+[m	[32m        );[m
[32m+[m	[32m    }[m
[32m+[m[41m	[m
[32m+[m[41m	    [m
[32m+[m[41m	[m
[32m+[m	[32m    if (consulta.getIdPaciente() == null[m
[32m+[m	[32m            || !pacienteRepository.existsById([m
[32m+[m	[32m                consulta.getIdPaciente())) {[m
[32m+[m[41m	[m
[32m+[m	[32m        throw new ResponseStatusException([m
[32m+[m	[32m            HttpStatus.BAD_REQUEST,[m
[32m+[m	[32m            "Paciente não encontrado."[m
[32m+[m	[32m        );[m
[32m+[m	[32m    }[m
[32m+[m[41m	[m
[32m+[m[41m	    [m
[32m+[m[41m	[m
[32m+[m	[32m    if (consulta.getIdHorarios() == null) {[m
[32m+[m	[32m        throw new ResponseStatusException([m
[32m+[m	[32m            HttpStatus.BAD_REQUEST,[m
[32m+[m	[32m            "Selecione um horário."[m
[32m+[m	[32m        );[m
[32m+[m	[32m    }[m
[32m+[m[41m	[m
[32m+[m[41m	    [m
[32m+[m[41m	[m
[32m+[m	[32m    Horarios horario = horariosRepository[m
[32m+[m	[32m        .buscarPorIdComBloqueio([m
[32m+[m	[32m            consulta.getIdHorarios()[m
[32m+[m	[32m        )[m
[32m+[m	[32m        .orElseThrow(() -> new ResponseStatusException([m
[32m+[m	[32m            HttpStatus.NOT_FOUND,[m
[32m+[m	[32m            "Horário não encontrado."[m
[32m+[m	[32m        ));[m
[32m+[m[41m	[m
[32m+[m[41m	    [m
[32m+[m[41m	[m
[32m+[m	[32m    if (!Objects.equals([m
[32m+[m	[32m            horario.getIdUbs(),[m
[32m+[m	[32m            consulta.getIdUbs())) {[m
[32m+[m[41m	[m
[32m+[m	[32m        throw new ResponseStatusException([m
[32m+[m	[32m            HttpStatus.BAD_REQUEST,[m
[32m+[m	[32m            "O horário selecionado pertence a outra UBS."[m
[32m+[m	[32m        );[m
[32m+[m	[32m    }[m
[32m+[m[41m	[m
[32m+[m[41m	    [m
[32m+[m[41m	[m
[32m+[m	[32m    if (!Boolean.TRUE.equals(horario.getDisponivel())) {[m
[32m+[m	[32m        throw new ResponseStatusException([m
[32m+[m	[32m            HttpStatus.CONFLICT,[m
[32m+[m	[32m            "Este horário não está mais disponível."[m
[32m+[m	[32m        );[m
[32m+[m	[32m    }[m
[32m+[m[41m	[m
[32m+[m[41m	  [m
[32m+[m[41m	[m
[32m+[m	[32m    horariosService.validarHorarioAgendamento(horario);[m
[32m+[m[41m	[m
[32m+[m[41m	    [m
[32m+[m[41m	[m
[32m+[m	[32m    boolean jaReservado =[m
[32m+[m	[32m        consultaRepository.existsByIdHorariosAndStatusIn([m
[32m+[m	[32m            consulta.getIdHorarios(),[m
[32m+[m	[32m            STATUS_OCUPADOS[m
[32m+[m	[32m        );[m
[32m+[m[41m	[m
[32m+[m	[32m    if (jaReservado) {[m
[32m+[m	[32m        throw new ResponseStatusException([m
[32m+[m	[32m            HttpStatus.CONFLICT,[m
[32m+[m	[32m            "Este horário já possui uma solicitação."[m
[32m+[m	[32m        );[m
[32m+[m	[32m    }[m
[32m+[m[41m	[m
[32m+[m[41m	    [m
[32m+[m[41m	[m
[32m+[m	[32m    consulta.setId(null);[m
[32m+[m	[32m    consulta.setIdProfissional(null);[m
[32m+[m	[32m    consulta.setDataAtendimento(horario.getDataHora());[m
[32m+[m[41m	[m
[32m+[m	[32m    consulta.setDataSolicitacao([m
[32m+[m	[32m        LocalDateTime.now([m
[32m+[m	[32m            java.time.ZoneId.of("America/Fortaleza")[m
[32m+[m	[32m        )[m
[32m+[m	[32m    );[m
[32m+[m[41m	[m
[32m+[m	[32m    consulta.setStatus("Solicitada");[m
[32m+[m[41m	[m
[32m+[m	[32m    // Salvar a consulta[m
[32m+[m[41m	[m
[32m+[m	[32m    Consulta salva = consultaRepository.save(consulta);[m
[32m+[m[41m	[m
[32m+[m[41m	   [m
[32m+[m[41m	[m
[32m+[m	[32m    horario.setDisponivel(false);[m
[32m+[m	[32m    horariosRepository.save(horario);[m
[32m+[m[41m	[m
[32m+[m	[32m    return salva;[m
[32m+[m	[32m}[m
 [m
[31m-        return salva;[m
[31m-    }[m
 [m
 [m
     @Transactional[m
[1mdiff --git a/src/main/java/br/com/ubsconecta/service/HorariosService.java b/src/main/java/br/com/ubsconecta/service/HorariosService.java[m
[1mindex 77ca2ab..de62d20 100644[m
[1m--- a/src/main/java/br/com/ubsconecta/service/HorariosService.java[m
[1m+++ b/src/main/java/br/com/ubsconecta/service/HorariosService.java[m
[36m@@ -1,57 +1,258 @@[m
[31m-[m
 package br.com.ubsconecta.service;[m
 [m
[32m+[m[32mimport java.time.LocalDate;[m
[32m+[m[32mimport java.time.LocalDateTime;[m
[32m+[m[32mimport java.time.YearMonth;[m
[32m+[m[32mimport java.time.ZoneId;[m
[32m+[m
[32m+[m[32mimport java.util.ArrayList;[m
 import java.util.List;[m
[32m+[m[32mimport java.util.Objects;[m
 [m
 import org.springframework.http.HttpStatus;[m
 import org.springframework.stereotype.Service;[m
[32m+[m[32mimport org.springframework.transaction.annotation.Transactional;[m
 import org.springframework.web.server.ResponseStatusException;[m
 [m
 import br.com.ubsconecta.model.Horarios;[m
[32m+[m[32mimport br.com.ubsconecta.model.FuncionamentoUbs;[m
[32m+[m
 import br.com.ubsconecta.repository.HorariosRepository;[m
[32m+[m[32mimport br.com.ubsconecta.repository.FuncionamentoUbsRepository;[m
[32m+[m[32mimport br.com.ubsconecta.repository.PeriodoAgendamentoRepository;[m
 [m
 @Service[m
 public class HorariosService {[m
 [m
[32m+[m[32m    private static final Integer UBS_PILOTO = 4;[m
[32m+[m
[32m+[m[32m    private static final ZoneId FUSO =[m
[32m+[m[32m        ZoneId.of("America/Fortaleza");[m
[32m+[m
     private final HorariosRepository horariosRepository;[m
 [m
[31m-    public HorariosService(HorariosRepository horariosRepository) {[m
[32m+[m[32m    private final FuncionamentoUbsRepository[m
[32m+[m[32m        funcionamentoUbsRepository;[m
[32m+[m
[32m+[m[32m    private final PeriodoAgendamentoRepository[m
[32m+[m[32m        periodoAgendamentoRepository;[m
[32m+[m
[32m+[m[32m    public HorariosService([m
[32m+[m[32m        HorariosRepository horariosRepository,[m
[32m+[m[32m        FuncionamentoUbsRepository funcionamentoUbsRepository,[m
[32m+[m[32m        PeriodoAgendamentoRepository periodoAgendamentoRepository[m
[32m+[m[32m    ) {[m
         this.horariosRepository = horariosRepository;[m
[32m+[m[32m        this.funcionamentoUbsRepository = funcionamentoUbsRepository;[m
[32m+[m[32m        this.periodoAgendamentoRepository = periodoAgendamentoRepository;[m
     }[m
 [m
[32m+[m
     public List<Horarios> listarTodos() {[m
         return horariosRepository.findAll();[m
     }[m
 [m
[31m-    public Horarios salvar(Horarios horarios) {[m
 [m
[31m-        if (horarios.getDataHora() == null) {[m
[32m+[m[32m    public boolean periodoAberto([m
[32m+[m[32m        Integer idUbs,[m
[32m+[m[32m        Integer mes,[m
[32m+[m[32m        Integer ano[m
[32m+[m[32m    ) {[m
[32m+[m[32m        return periodoAgendamentoRepository[m
[32m+[m[32m            .existsByIdUbsAndMesAndAnoAndAbertoTrue([m
[32m+[m[32m                idUbs,[m
[32m+[m[32m                mes,[m
[32m+[m[32m                ano[m
[32m+[m[32m            );[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m
[32m+[m[32m    private void validarUbs(Integer idUbs) {[m
[32m+[m
[32m+[m[32m        if (!Objects.equals(idUbs, UBS_PILOTO)) {[m
             throw new ResponseStatusException([m
                 HttpStatus.BAD_REQUEST,[m
[31m-                "A data e hora são obrigatórias."[m
[32m+[m[32m                "Agendamento disponível apenas para a UBS de Lagoa do Mato."[m
             );[m
         }[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m
[32m+[m[32m    private boolean dataBloqueada(LocalDate data) {[m
[32m+[m
[32m+[m[32m        return data.equals(LocalDate.of(2026, 12, 25));[m
[32m+[m[32m    }[m
 [m
[31m-        boolean duplicado;[m
 [m
[31m-        if (horarios.getId() == null) {[m
[31m-            duplicado = horariosRepository.existsByDataHora([m
[31m-                horarios.getDataHora()[m
[32m+[m[32m    private List<LocalDateTime> gerarHorariosValidos([m
[32m+[m[32m        Integer idUbs,[m
[32m+[m[32m        YearMonth periodo[m
[32m+[m[32m    ) {[m
[32m+[m
[32m+[m[32m        List<LocalDateTime> horariosValidos =[m
[32m+[m[32m            new ArrayList<>();[m
[32m+[m
[32m+[m[32m        List<FuncionamentoUbs> expedientes =[m
[32m+[m[32m            funcionamentoUbsRepository[m
[32m+[m[32m                .findByIdUbsAndAtivoTrue(idUbs);[m
[32m+[m
[32m+[m[32m        LocalDateTime agora =[m
[32m+[m[32m            LocalDateTime.now(FUSO);[m
[32m+[m
[32m+[m[32m        for ([m
[32m+[m[32m            LocalDate dia = periodo.atDay(1);[m
[32m+[m[32m            !dia.isAfter(periodo.atEndOfMonth());[m
[32m+[m[32m            dia = dia.plusDays(1)[m
[32m+[m[32m        ) {[m
[32m+[m
[32m+[m[32m            if ([m
[32m+[m[32m                dia.isBefore(agora.toLocalDate())[m
[32m+[m[32m                || dataBloqueada(dia)[m
[32m+[m[32m            ) {[m
[32m+[m[32m                continue;[m
[32m+[m[32m            }[m
[32m+[m
[32m+[m[32m            int diaSemana =[m
[32m+[m[32m                dia.getDayOfWeek().getValue();[m
[32m+[m
[32m+[m[32m            for (FuncionamentoUbs expediente : expedientes) {[m
[32m+[m
[32m+[m[32m                if (expediente.getDiaSemana() != diaSemana) {[m
[32m+[m[32m                    continue;[m
[32m+[m[32m                }[m
[32m+[m
[32m+[m[32m                int intervalo =[m
[32m+[m[32m                    expediente.getDuracaoConsulta()[m
[32m+[m[32m                    + expediente.getTolerancia();[m
[32m+[m
[32m+[m[32m                LocalDateTime inicio =[m
[32m+[m[32m                    dia.atTime(expediente.getHoraAbertura());[m
[32m+[m
[32m+[m[32m                LocalDateTime fechamento =[m
[32m+[m[32m                    dia.atTime(expediente.getHoraFechamento());[m
[32m+[m
[32m+[m[32m                while ([m
[32m+[m[32m                    !inicio.plusMinutes(intervalo)[m
[32m+[m[32m                        .isAfter(fechamento)[m
[32m+[m[32m                ) {[m
[32m+[m
[32m+[m[32m                    if (inicio.isAfter(agora)) {[m
[32m+[m[32m                        horariosValidos.add(inicio);[m
[32m+[m[32m                    }[m
[32m+[m
[32m+[m[32m                    inicio = inicio.plusMinutes(intervalo);[m
[32m+[m[32m                }[m
[32m+[m[32m            }[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        return horariosValidos;[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m
[32m+[m[32m    @Transactional[m
[32m+[m[32m    public List<Horarios> buscarDisponiveis([m
[32m+[m[32m        Integer idUbs,[m
[32m+[m[32m        Integer mes,[m
[32m+[m[32m        Integer ano[m
[32m+[m[32m    ) {[m
[32m+[m
[32m+[m[32m        validarUbs(idUbs);[m
[32m+[m
[32m+[m[32m        if ([m
[32m+[m[32m            mes == null || mes < 1 || mes > 12[m
[32m+[m[32m            || ano == null || ano < 2026 || ano > 2100[m
[32m+[m[32m        ) {[m
[32m+[m[32m            throw new ResponseStatusException([m
[32m+[m[32m                HttpStatus.BAD_REQUEST,[m
[32m+[m[32m                "Mês ou ano inválido."[m
             );[m
[31m-        } else {[m
[31m-            duplicado = horariosRepository.existsByDataHoraAndIdNot([m
[31m-                horarios.getDataHora(),[m
[31m-                horarios.getId()[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        YearMonth periodo = YearMonth.of(ano, mes);[m
[32m+[m
[32m+[m[32m        YearMonth periodoAtual =[m
[32m+[m[32m            YearMonth.now(FUSO);[m
[32m+[m
[32m+[m[32m        if ([m
[32m+[m[32m            periodo.isBefore(periodoAtual)[m
[32m+[m[32m            || !periodoAberto(idUbs, mes, ano)[m
[32m+[m[32m        ) {[m
[32m+[m[32m            return List.of();[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        List<LocalDateTime> horariosValidos =[m
[32m+[m[32m            gerarHorariosValidos(idUbs, periodo);[m
[32m+[m
[32m+[m[32m        for (LocalDateTime dataHora : horariosValidos) {[m
[32m+[m
[32m+[m[32m            horariosRepository.inserirHorarioSeNaoExistir([m
[32m+[m[32m                idUbs,[m
[32m+[m[32m                dataHora[m
[32m+[m[32m            );[m
[32m+[m[32m        }[m
[32m+[m
[32m+[m[32m        LocalDateTime inicio =[m
[32m+[m[32m            periodo.atDay(1).atStartOfDay();[m
[32m+[m
[32m+[m[32m        LocalDateTime fim =[m
[32m+[m[32m            periodo.plusMonths(1)[m
[32m+[m[32m                .atDay(1).atStartOfDay();[m
[32m+[m
[32m+[m[32m        LocalDateTime agora =[m
[32m+[m[32m            LocalDateTime.now(FUSO);[m
[32m+[m
[32m+[m[32m        return horariosRepository[m
[32m+[m[32m            .findByIdUbsAndDataHoraGreaterThanEqualAndDataHoraLessThanOrderByDataHoraAsc([m
[32m+[m[32m                idUbs,[m
[32m+[m[32m                inicio,[m
[32m+[m[32m                fim[m
[32m+[m[32m            )[m
[32m+[m[32m            .stream()[m
[32m+[m[32m            .filter(h -> Boolean.TRUE.equals(h.getDisponivel()))[m
[32m+[m[32m            .filter(h -> h.getDataHora().isAfter(agora))[m
[32m+[m[32m            .filter(h -> horariosValidos.contains(h.getDataHora()))[m
[32m+[m[32m            .toList();[m
[32m+[m[32m    }[m
[32m+[m
[32m+[m
[32m+[m[32m    public void validarHorarioAgendamento(Horarios horario) {[m
[32m+[m
[32m+[m[32m        validarUbs(horario.getIdUbs());[m
[32m+[m
[32m+[m[32m        LocalDateTime dataHora = horario.getDataHora();[m
[32m+[m
[32m+[m[32m        if ([m
[32m+[m[32m            dataHora == null[m
[32m+[m[32m            || !dataHora.isAfter(LocalDateTime.now(FUSO))[m
[32m+[m[32m        ) {[m
[32m+[m[32m            throw new ResponseStatusException([m
[32m+[m[32m                HttpStatus.BAD_REQUEST,[m
[32m+[m[32m                "O horário selecionado já passou."[m
             );[m
         }[m
 [m
[31m-        if (duplicado) {[m
[32m+[m[32m        YearMonth periodo = YearMonth.from(dataHora);[m
[32m+[m
[32m+[m[32m        if (!periodoAberto([m
[32m+[m[32m            horario.getIdUbs(),[m
[32m+[m[32m            periodo.getMonthValue(),[m
[32m+[m[32m            periodo.getYear()[m
[32m+[m[32m        )) {[m
             throw new ResponseStatusException([m
                 HttpStatus.CONFLICT,[m
[31m-                "Já existe um horário cadastrado para essa data e hora."[m
[32m+[m[32m                "O mês selecionado não está liberado."[m
             );[m
         }[m
 [m
[31m-        return horariosRepository.save(horarios);[m
[32m+[m[32m        if (!gerarHorariosValidos([m
[32m+[m[32m            horario.getIdUbs(),[m
[32m+[m[32m            periodo[m
[32m+[m[32m        ).contains(dataHora)) {[m
[32m+[m
[32m+[m[32m            throw new ResponseStatusException([m
[32m+[m[32m                HttpStatus.CONFLICT,[m
[32m+[m[32m                "Horário fora do expediente permitido."[m
[32m+[m[32m            );[m
[32m+[m[32m        }[m
     }[m
 }[m
