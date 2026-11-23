
package br.com.ubsconecta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.ubsconecta.model.Profissional;
import br.com.ubsconecta.repository.ProfissionalRepository;

@Service
public class ProfissionalService {

    private final ProfissionalRepository profissionalRepository;

    public ProfissionalService(ProfissionalRepository profissionalRepository) {
        this.profissionalRepository = profissionalRepository;
    }

    public List<Profissional> listarTodos() {
        return profissionalRepository.findAll();
    }

    public Profissional buscarPorUsuario(Integer idUsuario) {
        return profissionalRepository.findByIdUsuario(idUsuario);
    }

    public Profissional salvar(Profissional profissional) {
        return profissionalRepository.save(profissional);
    }
}
