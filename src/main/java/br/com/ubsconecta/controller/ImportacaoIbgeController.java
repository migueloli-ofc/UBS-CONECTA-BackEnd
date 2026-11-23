package br.com.ubsconecta.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ubsconecta.service.ImportacaoIbgeService;

@RestController
@RequestMapping("/api/importacao/ibge")
public class ImportacaoIbgeController {

    private final ImportacaoIbgeService importacaoIbgeService;

    public ImportacaoIbgeController(
            ImportacaoIbgeService importacaoIbgeService) {
        this.importacaoIbgeService = importacaoIbgeService;
    }

    @PostMapping("/estados")
    public String importarEstados() {
        return importacaoIbgeService.importarEstados();
    }
    
    @PostMapping("/municipios")
    public String importarMunicipios() {
    	return importacaoIbgeService.importarMunicipios();
    }
    
}
