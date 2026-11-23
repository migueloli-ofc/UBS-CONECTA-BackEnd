
package br.com.ubsconecta.service;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import br.com.ubsconecta.dto.ibge.EstadoIbgeDto;
import br.com.ubsconecta.dto.ibge.MunicipioIbgeDto;
import br.com.ubsconecta.model.Estado;
import br.com.ubsconecta.model.Cidade;
import br.com.ubsconecta.repository.EstadoRepository;
import br.com.ubsconecta.repository.CidadeRepository;


@Service
public class ImportacaoIbgeService {

    private final EstadoRepository estadoRepository;
    private final CidadeRepository cidadeRepository;
    private final RestClient restClient;

    public ImportacaoIbgeService(
            EstadoRepository estadoRepository,
            CidadeRepository cidadeRepository,
            RestClient.Builder restClientBuilder) {

        this.estadoRepository = estadoRepository;
        this.cidadeRepository = cidadeRepository;
        this.restClient = restClientBuilder.build();
    }

    @Transactional

    public String importarEstados() {

        List<EstadoIbgeDto> estadosIbge;

        try {
            estadosIbge = restClient.get()
                .uri("https://servicodados.ibge.gov.br/api/v1/localidades/estados?orderBy=nome")
                .retrieve()
                .body(new ParameterizedTypeReference<List<EstadoIbgeDto>>() {});
        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_GATEWAY,
                "Não foi possível consultar a API do IBGE."
            );
        }

        if (estadosIbge == null || estadosIbge.isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.BAD_GATEWAY,
                "A API do IBGE não retornou estados."
            );
        }

        int inseridos = 0;
        int atualizados = 0;

        for (EstadoIbgeDto dto : estadosIbge) {

            String codigo = String.valueOf(dto.id());

            Estado estado = estadoRepository
                .findByCodigoIbge(codigo)
                .or(() -> estadoRepository.findByUf(dto.sigla()))
                .orElseGet(Estado::new);

            boolean novo = estado.getId() == null;

            estado.setCodigoIbge(codigo);
            estado.setNomeEstado(dto.nome());
            estado.setUf(dto.sigla());

            estadoRepository.save(estado);

            if (novo) {
                inseridos++;
            } else {
                atualizados++;
            }
        }

        return "Importação concluída! Inseridos: "
            + inseridos + ". Atualizados: " + atualizados + ".";
    }
    

@Transactional
public String importarMunicipios() {

    List<Estado> estados = estadoRepository.findAll();

    if (estados.isEmpty()) {
        throw new ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "Importe os estados antes dos municípios."
        );
    }

    int inseridos = 0;
    int atualizados = 0;

    for (Estado estado : estados) {

        List<MunicipioIbgeDto> municipiosIbge;

        try {
            municipiosIbge = restClient.get()
                .uri("https://servicodados.ibge.gov.br/api/v1/localidades/estados/{codigo}/municipios",
                    estado.getCodigoIbge())
                .retrieve()
                .body(new ParameterizedTypeReference<List<MunicipioIbgeDto>>() {});

        } catch (Exception e) {
            throw new ResponseStatusException(
                HttpStatus.BAD_GATEWAY,
                "Não foi possível consultar os municípios do estado "
                    + estado.getNomeEstado() + "."
            );
        }

        if (municipiosIbge == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_GATEWAY,
                "A API do IBGE não retornou municípios."
            );
        }

        for (MunicipioIbgeDto dto : municipiosIbge) {

            String codigo = String.valueOf(dto.id());

            Cidade cidade = cidadeRepository
                .findByCodigoIbge(codigo)
                .orElseGet(Cidade::new);

            boolean nova = cidade.getId() == null;

            cidade.setCodigoIbge(codigo);
            cidade.setNomeCidade(dto.nome());
            cidade.setIdEstado(estado.getId());

            cidadeRepository.save(cidade);

            if (nova) {
                inseridos++;
            } else {
                atualizados++;
            }
        }
    }

    return "Importação de municípios concluída! Inseridos: "
        + inseridos + ". Atualizados: " + atualizados + ".";
}

}
