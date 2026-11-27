
package br.com.ubsconecta.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ubsconecta.model.FuncionamentoUbs;

public interface FuncionamentoUbsRepository
        extends JpaRepository<FuncionamentoUbs, Integer> {

    List<FuncionamentoUbs> findByIdUbsAndAtivoTrue(
        Integer idUbs
    );

    List<FuncionamentoUbs> findByIdUbsAndDiaSemanaAndAtivoTrue(
        Integer idUbs,
        Integer diaSemana
    );
}
