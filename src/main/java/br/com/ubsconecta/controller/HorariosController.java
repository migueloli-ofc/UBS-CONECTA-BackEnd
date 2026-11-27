
package br.com.ubsconecta.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ubsconecta.model.Horarios;
import br.com.ubsconecta.service.HorariosService;

@CrossOrigin(origins = {
    "http://127.0.0.1:5500",
    "http://localhost:5500"
})
@RestController
@RequestMapping("/api/horarios")
public class HorariosController {

    private final HorariosService horariosService;

    public HorariosController(
        HorariosService horariosService
    ) {
        this.horariosService = horariosService;
    }

    @GetMapping
    public List<Horarios> listarTodos() {
        return horariosService.listarTodos();
    }

    @GetMapping("/disponiveis")
    public List<Horarios> buscarDisponiveis(
        @RequestParam Integer idUbs,
        @RequestParam Integer mes,
        @RequestParam Integer ano
    ) {

        return horariosService.buscarDisponiveis(
            idUbs,
            mes,
            ano
        );
    }
}
