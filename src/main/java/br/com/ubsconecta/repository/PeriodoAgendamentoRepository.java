package br.com.ubsconecta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.ubsconecta.model.PeriodoAgendamento;

public interface PeriodoAgendamentoRepository
        extends JpaRepository<PeriodoAgendamento, Integer> {

    boolean existsByIdUbsAndMesAndAnoAndAbertoTrue(
        Integer idUbs,
        Integer mes,
        Integer ano
    );
}
