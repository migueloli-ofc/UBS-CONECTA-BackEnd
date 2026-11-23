
package br.com.ubsconecta.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ubsconecta.dto.LoginResposta;
import br.com.ubsconecta.model.Usuario;
import br.com.ubsconecta.service.UsuarioService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> listarTodos() {
        return usuarioService.listarTodos();
    }

    @PostMapping
    public Usuario salvar(@RequestBody Usuario usuario) {
        return usuarioService.salvar(usuario);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResposta> login(@RequestBody Usuario usuario) {

        Usuario usuarioEncontrado =
                usuarioService.buscarPorLogin(usuario.getLogin());

        if (usuarioEncontrado == null ||
                !usuarioEncontrado.getSenha().equals(usuario.getSenha())) {

            return ResponseEntity.status(401).body(
                new LoginResposta(
                    "Login ou senha incorretos.",
                    null
                )
            );
        }

        String tipoUsuario =
                usuarioService.identificarTipoUsuario(usuarioEncontrado.getId());

        if (tipoUsuario.equals("AMBOS") ||
                tipoUsuario.equals("SEM_CADASTRO")) {

            return ResponseEntity.status(403).body(
                new LoginResposta(
                    "A conta não possui um tipo de cadastro válido.",
                    tipoUsuario
                )
            );
        }

        return ResponseEntity.ok(
        	    new LoginResposta(
        	        "Login realizado com sucesso!",
        	        tipoUsuario,
        	        usuarioEncontrado.getId()
        	    )
        	);
    }
}