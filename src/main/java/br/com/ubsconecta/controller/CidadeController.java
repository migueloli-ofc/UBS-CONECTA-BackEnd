package br.com.ubsconecta.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ubsconecta.model.Cidade;
import br.com.ubsconecta.service.CidadeService;

@RestController
@RequestMapping("/api/cidades")
public class CidadeController {

    private final CidadeService cidadeService;

    public CidadeController(CidadeService cidadeService) {
        this.cidadeService = cidadeService;
    }

    @GetMapping
    public List<Cidade> listarTodas() {
        return cidadeService.listarTodas();
    }

    @PostMapping
    public Cidade salvar(@RequestBody Cidade cidade) {
        return cidadeService.salvar(cidade);
    }

}