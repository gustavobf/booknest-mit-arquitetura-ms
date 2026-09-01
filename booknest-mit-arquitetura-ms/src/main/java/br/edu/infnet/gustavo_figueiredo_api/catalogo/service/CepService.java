package br.edu.infnet.gustavo_figueiredo_api.catalogo.service;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.integration.client.*;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.integration.dto.*;
import br.edu.infnet.gustavo_figueiredo_api.exception.*;
import feign.*;
import org.springframework.stereotype.*;

@Service
public class CepService {

    private final ViaCepClient viaCepClient;

    public CepService (ViaCepClient viaCepClient) {
        this.viaCepClient = viaCepClient;
    }

    public ViaCepResponse consultar (String cep) {
        String cepLimpo = normalizarCep(cep);

        try {
            ViaCepResponse response = viaCepClient.consultarCep(cepLimpo);
            if (response == null || Boolean.TRUE.equals(response.erro())) {
                throw new RegistroNaoEncontradoException("CEP " + cep + " não encontrado.");
            }
            return response;
        } catch (FeignException ex) {
            throw new DadosInvalidosException("Falha ao consultar CEP externo: " + ex.getMessage());
        }
    }

    private String normalizarCep (String cep) {
        if (cep == null || cep.isBlank()) {
            throw new DadosInvalidosException("CEP deve ser informado.");
        }
        String cepLimpo = cep.replaceAll("\\D", "");
        if (cepLimpo.length() != 8) {
            throw new DadosInvalidosException("CEP deve conter 8 dígitos.");
        }
        return cepLimpo;
    }
}
