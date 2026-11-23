package br.com.ubsconecta.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;

import br.com.ubsconecta.model.Paciente;
import br.com.ubsconecta.service.PacienteService;


@CrossOrigin(origins = {
	    "http://127.0.0.1:5500",
	    "http://localhost:5500"
	})
@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @GetMapping("/por-usuario")
    public ResponseEntity<Paciente> buscarPorUsuario(
            @RequestParam Integer idUsuario) {

        Paciente paciente = pacienteService.buscarPorUsuario(idUsuario);

        if (paciente == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(paciente);
    }
    
    @PostMapping
    public Paciente salvar(@RequestBody Paciente paciente) {
        return pacienteService.salvar(paciente);
    }

}