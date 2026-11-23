package br.com.ubsconecta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.ubsconecta.model.Usuario;
import br.com.ubsconecta.model.Paciente;
import br.com.ubsconecta.model.Profissional;
import br.com.ubsconecta.repository.UsuarioRepository;
import br.com.ubsconecta.repository.PacienteRepository;
import br.com.ubsconecta.repository.ProfissionalRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final ProfissionalRepository profissionalRepository;

    public UsuarioService(UsuarioRepository usuarioRepository,
    		PacienteRepository pacienteRepository,
    		ProfissionalRepository profissionalRepository) {
        this.usuarioRepository = usuarioRepository;
        this.pacienteRepository = pacienteRepository;
        this.profissionalRepository = profissionalRepository;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }
    
    public Usuario salvar(Usuario usuario) {
    	///findByEmail(usuario.getLogin());
    	return usuarioRepository.save(usuario);
    }
    
    public Usuario buscarPorLogin(String login) {
    	return usuarioRepository.findByLogin(login);
    }
    

public String identificarTipoUsuario(Integer idUsuario) {

    Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

    if (usuario != null
            && usuario.getTipoUsuario() != null
            && !usuario.getTipoUsuario().isBlank()) {

        return usuario.getTipoUsuario();
    }

    // Aceita contas antigas, mesmo o tipo de usuario
    Paciente paciente = pacienteRepository.findByIdUsuario(idUsuario);
    Profissional profissional = profissionalRepository.findByIdUsuario(idUsuario);

    if (paciente != null && profissional == null) {
        return "PACIENTE";
    }

    if (profissional != null && paciente == null) {
        return "PROFISSIONAL";
    }

    if (paciente != null && profissional != null) {
        return "AMBOS";
    }

    return "SEM_CADASTRO";
}

public String buscarTipoUsuario(Integer idUsuario) {
    Usuario usuario = usuarioRepository.findById(idUsuario)
        .orElseThrow(() ->
            new IllegalArgumentException("Usuário não encontrado."));

    return usuario.getTipoUsuario();
}


}