package br.com.ubsconecta.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ubsconecta.model.Horarios;
import br.com.ubsconecta.service.HorariosService;

@RestController
@RequestMapping("/api/horarios")
public class HorariosController {

    private final HorariosService horariosService;

    public HorariosController(HorariosService horariosService) {
        this.horariosService = horariosService;
    }

    @GetMapping
    public List<Horarios> listarTodos() {
        return horariosService.listarTodos();
    }

    @PostMapping
    public Horarios salvar(@RequestBody Horarios horarios) {
        return horariosService.salvar(horarios);
    }
}