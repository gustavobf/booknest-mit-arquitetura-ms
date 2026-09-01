package br.edu.infnet.gustavo_figueiredo_api.catalogo.integration.client;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.integration.dto.*;
import feign.*;

public interface ViaCepClient {
    @RequestLine("GET /ws/{cep}/json/")
    ViaCepResponse consultarCep (@Param("cep") String cep);
}
