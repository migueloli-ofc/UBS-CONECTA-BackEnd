package br.com.ubsconecta.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.ubsconecta.dto.UbsCnesDto;
import br.com.ubsconecta.model.Cidade;
import br.com.ubsconecta.model.Estado;
import br.com.ubsconecta.model.Ubs;
import br.com.ubsconecta.repository.CidadeRepository;
import br.com.ubsconecta.repository.EstadoRepository;
import br.com.ubsconecta.repository.UbsRepository;

@Service
public class ImportacaoUbsCnesService {

    private final CidadeRepository cidadeRepository;
    private final EstadoRepository estadoRepository;
    private final UbsRepository ubsRepository;
    private final ObjectMapper objectMapper;

    public ImportacaoUbsCnesService(
            CidadeRepository cidadeRepository,
            EstadoRepository estadoRepository,
            UbsRepository ubsRepository,
            ObjectMapper objectMapper) {

        this.cidadeRepository = cidadeRepository;
        this.estadoRepository = estadoRepository;
        this.ubsRepository = ubsRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public String importar() throws IOException {

        Path caminho = Path.of(
            "dados",
            "Unidades_Basicas_Saude-UBS.json"
        );

        List<UbsCnesDto> unidades;

        try (var leitor = Files.newBufferedReader(caminho)) {
            unidades = objectMapper.readValue(
                leitor,
                new TypeReference<List<UbsCnesDto>>() {}
            );
        }

        Map<String, Estado> estados = estadoRepository.findAll()
            .stream()
            .filter(e -> e.getCodigoIbge() != null)
            .collect(Collectors.toMap(
                Estado::getCodigoIbge,
                Function.identity(),
                (a, b) -> a
            ));

        List<Cidade> cidades = cidadeRepository.findAll();

        Map<String, List<Cidade>> cidadesPorCodigo = cidades.stream()
            .filter(c -> c.getCodigoIbge() != null)
            .collect(Collectors.groupingBy(
                c -> c.getCodigoIbge().substring(
                    0, Math.min(6, c.getCodigoIbge().length())
                )
            ));

        Map<String, Ubs> ubsExistentes = new HashMap<>();

        for (Ubs ubs : ubsRepository.findAll()) {
            if (ubs.getCodigoCnes() != null) {
                ubsExistentes.putIfAbsent(
                    ubs.getCodigoCnes().trim(),
                    ubs
                );
            }
        }

        int importadas = 0;
        int ignoradas = 0;

        for (UbsCnesDto dto : unidades) {

            if (dto.cnes() == null || dto.ibge() == null
                    || dto.uf() == null || dto.nome() == null
                    || dto.cnes().isBlank() || dto.ibge().isBlank()
                    || dto.uf().isBlank() || dto.nome().isBlank()) {
                ignoradas++;
                continue;
            }

            String cnes = dto.cnes().trim();
            String codigoMunicipio = dto.ibge().trim();
            String codigoEstado = dto.uf().trim();

            Estado estado = estados.get(codigoEstado);

            if (estado == null) {
                ignoradas++;
                continue;
            }

            List<Cidade> candidatas = cidadesPorCodigo.getOrDefault(
                codigoMunicipio,
                List.of()
            );

            List<Cidade> correspondentes = candidatas.stream()
                .filter(c -> estado.getId().equals(c.getIdEstado()))
                .toList();

            if (correspondentes.size() != 1) {
                ignoradas++;
                continue;
            }

            Cidade cidade = correspondentes.get(0);

            Ubs ubs = ubsExistentes.get(cnes);

            if (ubs == null) {
                ubs = new Ubs();
                ubs.setCodigoCnes(cnes);
            }

            ubs.setNomeUbs(dto.nome().trim());
            ubs.setIdCidade(cidade.getId());

            String logradouro = dto.logradouro() == null
                ? ""
                : dto.logradouro().trim();

            String bairro = dto.bairro() == null
                ? ""
                : dto.bairro().trim();

            String endereco;

            if (logradouro.isBlank()) {
                endereco = bairro;
            } else if (bairro.isBlank()) {
                endereco = logradouro;
            } else {
                endereco = logradouro + " - " + bairro;
            }

            ubs.setEnderecoUbs(endereco);

            Ubs salva = ubsRepository.save(ubs);
            ubsExistentes.put(cnes, salva);

            importadas++;
        }

        return "Processadas: " + unidades.size()
            + ". Importadas/atualizadas: " + importadas
            + ". Ignoradas: " + ignoradas + ".";
    }
}
