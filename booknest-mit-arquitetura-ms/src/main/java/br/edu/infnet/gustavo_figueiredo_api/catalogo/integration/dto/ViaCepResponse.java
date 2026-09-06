package br.edu.infnet.gustavo_figueiredo_api.catalogo.integration.dto;

import com.fasterxml.jackson.annotation.*;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ViaCepResponse(String cep, String logradouro, String complemento, String bairro,
                             @JsonProperty("localidade") String cidade, String uf, Boolean erro) {
}
