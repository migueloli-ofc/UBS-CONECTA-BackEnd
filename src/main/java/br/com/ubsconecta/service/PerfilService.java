
package br.com.ubsconecta.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.ubsconecta.dto.PerfilRequest;
import br.com.ubsconecta.model.Paciente;
import br.com.ubsconecta.model.Profissional;
import br.com.ubsconecta.repository.PacienteRepository;
import br.com.ubsconecta.repository.ProfissionalRepository;
import br.com.ubsconecta.repository.UsuarioRepository;

@Service
public class PerfilService {

    private final UsuarioRepository usuarioRepository;
    private final PacienteRepository pacienteRepository;
    private final ProfissionalRepository profissionalRepository;

    public PerfilService(
            UsuarioRepository usuarioRepository,
            PacienteRepository pacienteRepository,
            ProfissionalRepository profissionalRepository) {

        this.usuarioRepository = usuarioRepository;
        this.pacienteRepository = pacienteRepository;
        this.profissionalRepository = profissionalRepository;
    }

    @Transactional
    public String completarPerfil(PerfilRequest dados) {

        if (dados.getIdUsuario() == null) {
            throw new IllegalArgumentException("ID do usuário não informado.");
        }

        var usuario = usuarioRepository.findById(dados.getIdUsuario())
                .orElseThrow(() ->
                    new IllegalArgumentException("Usuário não encontrado."));
        String tipoUsuario = usuario.getTipoUsuario();

        if (tipoUsuario == null ||
                (!tipoUsuario.equals("PACIENTE")
                && !tipoUsuario.equals("PROFISSIONAL"))) {
            throw new IllegalArgumentException(
                "A conta não possui um tipo de usuário válido.");
        }

        if (pacienteRepository.findByIdUsuario(usuario.getId()) != null) {
            throw new IllegalArgumentException(
                "Esta conta já possui um perfil de paciente.");
        }

        if (profissionalRepository.findByIdUsuario(usuario.getId()) != null) {
            throw new IllegalArgumentException(
                "Esta conta já possui um perfil de profissional.");
        }

        if (dados.getNome() == null || dados.getNome().isBlank()
                || dados.getCpf() == null || dados.getCpf().isBlank()
                || dados.getEmail() == null || dados.getEmail().isBlank()) {
            throw new IllegalArgumentException(
                "Preencha nome, CPF e e-mail.");
        }

        Paciente paciente = new Paciente();
        paciente.setNome(dados.getNome());
        paciente.setCpf(dados.getCpf());
        paciente.setRg(dados.getRg());
        paciente.setDataNascimento(dados.getDataNascimento());
        paciente.setTelefone(dados.getTelefone());
        paciente.setEmail(dados.getEmail());
        paciente.setIdUsuario(usuario.getId());

        pacienteRepository.save(paciente);

        if (tipoUsuario.equals("PROFISSIONAL")) {

            if (dados.getCargo() == null || dados.getCargo().isBlank()
                    || dados.getConselhoProfissional() == null
                    || dados.getConselhoProfissional().isBlank()
                    || dados.getRegistroProfissional() == null
                    || dados.getRegistroProfissional().isBlank()
                    || dados.getIdUbs() == null) {

                throw new IllegalArgumentException(
                    "Preencha os dados profissionais e selecione uma UBS.");
            }

            Profissional profissional = new Profissional();
            profissional.setNome(dados.getNome());
            profissional.setCpf(dados.getCpf());
            profissional.setCargo(dados.getCargo());
            profissional.setConselhoProfissional(
                dados.getConselhoProfissional());
            profissional.setRegistroProfissional(
                dados.getRegistroProfissional());
            profissional.setEspecialidade(dados.getEspecialidade());
            profissional.setTelefone(dados.getTelefone());
            profissional.setEmail(dados.getEmail());
            profissional.setIdUsuario(usuario.getId());
            profissional.setIdUbs(dados.getIdUbs());

            profissionalRepository.save(profissional);
        }

        return "Perfil completado com sucesso!";
    }
}
