package br.com.ubsconecta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.ubsconecta.model.Estado;
import br.com.ubsconecta.repository.EstadoRepository;

@Service
public class EstadoService {

    private final EstadoRepository estadoRepository;

    public EstadoService(EstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
    }

    public List<Estado> listarTodos() {
        return estadoRepository.findAll();
    }

    public Estado salvar(Estado estado) {
        return estadoRepository.save(estado);
    }
}