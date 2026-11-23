package br.com.ubsconecta.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.ubsconecta.model.Cidade;
import br.com.ubsconecta.repository.CidadeRepository;

@Service
public class CidadeService {

    private final CidadeRepository cidadeRepository;

    public CidadeService(CidadeRepository cidadeRepository) {
        this.cidadeRepository = cidadeRepository;
    }

    public List<Cidade> listarTodas() {
        return cidadeRepository.findAll();
    }

    public Cidade salvar(Cidade cidade) {
        return cidadeRepository.save(cidade);
    }
}