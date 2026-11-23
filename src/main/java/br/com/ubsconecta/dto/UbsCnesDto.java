
package br.com.ubsconecta.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UbsCnesDto(
    @JsonProperty("CNES") String cnes,
    @JsonProperty("UF") String uf,
    @JsonProperty("IBGE") String ibge,
    @JsonProperty("NOME") String nome,
    @JsonProperty("LOGRADOURO") String logradouro,
    @JsonProperty("BAIRRO") String bairro,
    @JsonProperty("LATITUDE") String latitude,
    @JsonProperty("LONGITUDE") String longitude
) {}
