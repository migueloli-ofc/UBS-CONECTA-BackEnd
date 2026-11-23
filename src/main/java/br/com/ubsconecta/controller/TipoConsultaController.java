package br.com.ubsconecta.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ubsconecta.model.TipoConsulta;
import br.com.ubsconecta.service.TipoConsultaService;

@RestController
@RequestMapping("/api/tipos-consultas")
public class TipoConsultaController {

    private final TipoConsultaService tipoConsultaService;

    public TipoConsultaController(TipoConsultaService tipoConsultaService) {
        this.tipoConsultaService = tipoConsultaService;
    }

    @GetMapping
    public List<TipoConsulta> listarTodas() {
        return tipoConsultaService.listarTodas();
    }

    @PostMapping
    public TipoConsulta salvar(@RequestBody TipoConsulta tipoConsulta) {
        return tipoConsultaService.salvar(tipoConsulta);
    }
}