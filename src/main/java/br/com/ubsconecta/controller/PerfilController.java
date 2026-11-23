
package br.com.ubsconecta.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ubsconecta.dto.PerfilRequest;
import br.com.ubsconecta.service.PerfilService;

@RestController
@RequestMapping("/api/perfis")
@CrossOrigin(origins = {
	    "http://127.0.0.1:5500",
	    "http://localhost:5500"
	})
public class PerfilController {

    private final PerfilService perfilService;

    public PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }

    @PostMapping("/completar")
    public ResponseEntity<Map<String, String>> completarPerfil(
            @RequestBody PerfilRequest dados) {

        try {

        	String mensagem = perfilService.completarPerfil(dados);


            return ResponseEntity.ok(
                Map.of("mensagem", mensagem));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(
                Map.of("erro", e.getMessage()));
        }
    }
}
