
package br.com.ubsconecta.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.ubsconecta.model.Cidade;

public interface CidadeRepository extends JpaRepository<Cidade, Integer> {

    Optional<Cidade> findByCodigoIbge(String codigoIbge);

    Optional<Cidade> findFirstByCodigoIbgeStartingWith(String codigoIbge);

    Optional<Cidade> findFirstByCodigoIbgeStartingWithAndIdEstado(
            String codigoIbge,
            Integer idEstado
    );
}
