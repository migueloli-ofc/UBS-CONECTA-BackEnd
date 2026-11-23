
package br.com.ubsconecta.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.ubsconecta.model.Ubs;

public interface UbsRepository extends JpaRepository<Ubs, Integer> {

    Optional<Ubs> findByCodigoCnes(String codigoCnes);

}
