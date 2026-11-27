
package br.com.ubsconecta.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Modifying;

import jakarta.persistence.LockModeType;

import br.com.ubsconecta.model.Horarios;

public interface HorariosRepository extends JpaRepository<Horarios, Integer> {

    boolean existsByDataHora(LocalDateTime dataHora);

    boolean existsByDataHoraAndIdNot(
        LocalDateTime dataHora,
        Integer id
    );


    boolean existsByIdUbsAndDataHora(
        Integer idUbs,
        LocalDateTime dataHora
    );

    boolean existsByIdUbsAndDataHoraAndIdNot(
        Integer idUbs,
        LocalDateTime dataHora,
        Integer id
    );

    List<Horarios> findByIdUbsAndDataHoraGreaterThanEqualAndDataHoraLessThanOrderByDataHoraAsc(
        Integer idUbs,
        LocalDateTime inicio,
        LocalDateTime fim
    );

    
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT h FROM Horarios h WHERE h.id = :id")
    Optional<Horarios> buscarPorIdComBloqueio(
        @Param("id") Integer id
    );
    
    @Modifying
    @Query(value = """
        INSERT INTO horarios (
            id_ubs,
            data_hora,
            disponivel
        )
        VALUES (
            :idUbs,
            :dataHora,
            TRUE
        )
        ON CONFLICT (id_ubs, data_hora)
        DO NOTHING
        """, nativeQuery = true)
    int inserirHorarioSeNaoExistir(
        @Param("idUbs") Integer idUbs,
        @Param("dataHora") LocalDateTime dataHora
    );
}
