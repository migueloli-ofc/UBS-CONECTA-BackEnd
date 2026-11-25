
package br.com.ubsconecta.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.ubsconecta.model.Consulta;
import br.com.ubsconecta.service.ConsultaService;

@CrossOrigin(origins = {
    "http://127.0.0.1:5500",
    "http://localhost:5500"
})
@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @GetMapping
    public List<Consulta> listarTodos() {
        return consultaService.listarTodos();
    }
    
    @GetMapping("/ubs")
    public ResponseEntity<List<Consulta>> buscarConsultasDaUbs(
            @RequestParam Integer idUsuario) {

        return ResponseEntity.ok(
                consultaService.buscarConsultasDaUbs(idUsuario)
        );
    }
    
    

    @GetMapping("/proximas")
    public ResponseEntity<List<Consulta>> buscarProximas(
            @RequestParam Integer idUsuario) {

        return ResponseEntity.ok(
            consultaService.buscarProximasPorUsuario(idUsuario)
        );
    }
    
    @GetMapping("/paciente")
    public ResponseEntity<List<Consulta>> buscarConsultasPorPaciente( 
    		@RequestParam Integer idUsuario){
    	return ResponseEntity.ok(
    			consultaService.buscarConsultasPorPaciente(idUsuario));
    }
    

    @PostMapping
    public Consulta salvar(@RequestBody Consulta consulta) {
        return consultaService.salvar(consulta);
    }
    
    @PutMapping("/{idConsulta}/confirmar")
    public ResponseEntity<Consulta> confirmarConsulta(
            @PathVariable Integer idConsulta,
            @RequestParam Integer idUsuario) {

        return ResponseEntity.ok(
                consultaService.confirmarConsulta(idConsulta, idUsuario)
        );
    }
    
    @PutMapping("/{idConsulta}/recusar")
    public ResponseEntity<Consulta> recusarConsulta(
            @PathVariable Integer idConsulta,
            @RequestParam Integer idUsuario) {

        return ResponseEntity.ok(
            consultaService.recusarConsulta(idConsulta, idUsuario)
        );
    }
    
    
}
