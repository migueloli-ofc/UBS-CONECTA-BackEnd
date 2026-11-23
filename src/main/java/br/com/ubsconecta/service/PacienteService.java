package br.com.ubsconecta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.ubsconecta.model.Paciente;
import br.com.ubsconecta.repository.PacienteRepository;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    public List<Paciente> listarTodos() {
        return pacienteRepository.findAll();
    }
    
    public Paciente buscarPorUsuario(Integer idUsuario) {
        return pacienteRepository.findByIdUsuario(idUsuario);
    }

    
    public Paciente salvar(Paciente paciente) {
    	return pacienteRepository.save(paciente);
    }
}