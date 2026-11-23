package br.com.ubsconecta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.ubsconecta.model.Ubs;
import br.com.ubsconecta.repository.UbsRepository;

@Service
public class UbsService {

    private final UbsRepository ubsRepository;

    public UbsService(UbsRepository ubsRepository) {
        this.ubsRepository = ubsRepository;
    }

    public List<Ubs> listarTodas() {
        return ubsRepository.findAll();
    }
    
    public Ubs salvar(Ubs ubs) {
    	return ubsRepository.save(ubs);
    }
}