package br.com.ubsconecta.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.ubsconecta.model.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Integer> {

    Optional<Estado> findByCodigoIbge(String codigoIbge);

    Optional<Estado> findByUf(String uf);
}
