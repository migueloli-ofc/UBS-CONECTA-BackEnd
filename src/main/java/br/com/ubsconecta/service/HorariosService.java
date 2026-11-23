
package br.com.ubsconecta.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.ubsconecta.model.Horarios;
import br.com.ubsconecta.repository.HorariosRepository;

@Service
public class HorariosService {

    private final HorariosRepository horariosRepository;

    public HorariosService(HorariosRepository horariosRepository) {
        this.horariosRepository = horariosRepository;
    }

    public List<Horarios> listarTodos() {
        return horariosRepository.findAll();
    }

    public Horarios salvar(Horarios horarios) {

        if (horarios.getDataHora() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "A data e hora são obrigatórias."
            );
        }

        boolean duplicado;

        if (horarios.getId() == null) {
            duplicado = horariosRepository.existsByDataHora(
                horarios.getDataHora()
            );
        } else {
            duplicado = horariosRepository.existsByDataHoraAndIdNot(
                horarios.getDataHora(),
                horarios.getId()
            );
        }

        if (duplicado) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Já existe um horário cadastrado para essa data e hora."
            );
        }

        return horariosRepository.save(horarios);
    }
}
