
package br.com.ubsconecta.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import br.com.ubsconecta.model.Profissional;
import br.com.ubsconecta.service.ProfissionalService;

@CrossOrigin(origins = {
	    "http://127.0.0.1:5500",
	    "http://localhost:5500"
	})
@RestController
@RequestMapping("/api/profissionais")
public class ProfissionalController {

    private final ProfissionalService profissionalService;

    public ProfissionalController(ProfissionalService profissionalService) {
        this.profissionalService = profissionalService;
    }

    @GetMapping
    public List<Profissional> listarTodos() {
        return profissionalService.listarTodos();
    }

    @GetMapping("/por-usuario")
    public ResponseEntity<Profissional> buscarPorUsuario(
            @RequestParam Integer idUsuario) {

        Profissional profissional =
                profissionalService.buscarPorUsuario(idUsuario);

        if (profissional == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(profissional);
    }

    @PostMapping
    public Profissional salvar(@RequestBody Profissional profissional) {
        return profissionalService.salvar(profissional);
    }
}
