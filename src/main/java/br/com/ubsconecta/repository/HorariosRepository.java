
package br.com.ubsconecta.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.persistence.LockModeType;

import br.com.ubsconecta.model.Horarios;

public interface HorariosRepository extends JpaRepository<Horarios, Integer> {

    boolean existsByDataHora(LocalDateTime dataHora);

    boolean existsByDataHoraAndIdNot(LocalDateTime dataHora, Integer id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT h FROM Horarios h WHERE h.id = :id")
    Optional<Horarios> buscarPorIdComBloqueio(@Param("id") Integer id);
}
