
package br.com.ubsconecta.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import br.com.ubsconecta.model.Consulta;

public interface ConsultaRepository extends JpaRepository<Consulta, Integer> {

    List<Consulta> findByIdPaciente(Integer idPaciente);

    List<Consulta> findByIdPacienteAndDataAtendimentoAfterOrderByDataAtendimentoAsc(
            Integer idPaciente,
            LocalDateTime agora
    );
    boolean existsByIdHorariosAndStatusIn(
    	    Integer idHorarios,
    	    List<String> status
    	);
    
    List<Consulta> findByIdUbsOrderByDataAtendimentoAsc(Integer idUbs);

}
