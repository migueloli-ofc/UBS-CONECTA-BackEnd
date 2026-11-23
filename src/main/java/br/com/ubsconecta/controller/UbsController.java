package br.com.ubsconecta.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import br.com.ubsconecta.model.Ubs;
import br.com.ubsconecta.service.UbsService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/ubs")
public class UbsController {
	
    private final UbsService ubsService;

    public UbsController(UbsService ubsService) {
        this.ubsService = ubsService;
    }

    @GetMapping
    public List<Ubs> listarTodas() {
        return ubsService.listarTodas();
    }

    @PostMapping
    public Ubs salvar(@RequestBody Ubs ubs) {
        return ubsService.salvar(ubs);
    }

}