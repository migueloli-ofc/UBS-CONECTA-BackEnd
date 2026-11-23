package br.com.ubsconecta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.ubsconecta.model.TipoConsulta;
import br.com.ubsconecta.repository.TipoConsultaRepository;

@Service
public class TipoConsultaService {

    private final TipoConsultaRepository tipoConsultaRepository;

    public TipoConsultaService(TipoConsultaRepository tipoConsultaRepository) {
        this.tipoConsultaRepository = tipoConsultaRepository;
    }

    public List<TipoConsulta> listarTodas() {
        return tipoConsultaRepository.findAll();
    }
    
    public TipoConsulta salvar(TipoConsulta tipoConsulta) {
    	return tipoConsultaRepository.save(tipoConsulta);
    }
}