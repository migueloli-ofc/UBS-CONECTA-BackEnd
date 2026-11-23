/*
 * package br.com.ubsconecta.controller;
 * 
 * import java.io.IOException; import java.util.Map;
 * 
 * import org.springframework.http.ResponseEntity; import
 * org.springframework.web.bind.annotation.PostMapping; import
 * org.springframework.web.bind.annotation.RequestMapping; import
 * org.springframework.web.bind.annotation.RestController;
 * 
 * import br.com.ubsconecta.service.ImportacaoUbsCnesService;
 * 
 * @RestController
 * 
 * @RequestMapping("/api/importacao/cnes") public class
 * ImportacaoUbsCnesController {
 * 
 * private final ImportacaoUbsCnesService importacaoService;
 * 
 * public ImportacaoUbsCnesController( ImportacaoUbsCnesService
 * importacaoService) { this.importacaoService = importacaoService; }
 * 
 * @PostMapping("/ubs") public ResponseEntity<Map<String, String>> importar() {
 * try { String resultado = importacaoService.importar();
 * 
 * return ResponseEntity.ok( Map.of("mensagem", resultado) );
 * 
 * } catch (IOException e) { return ResponseEntity.internalServerError().body(
 * Map.of( "erro", "Não foi possível ler o arquivo JSON do CNES." ) ); } } }
 */