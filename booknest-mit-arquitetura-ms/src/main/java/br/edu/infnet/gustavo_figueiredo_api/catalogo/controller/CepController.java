package br.edu.infnet.gustavo_figueiredo_api.catalogo.controller;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.integration.dto.*;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.service.CepService;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ceps")
@Tag(name = "Integração CEP")
public class CepController {
    private final CepService cepService;

    public CepController (CepService cepService) {
        this.cepService = cepService;
    }

    @GetMapping("/{cep}")
    @Operation(summary = "Consultar CEP externo", description = "Consulta dados de endereço em API externa usando OpenFeign.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "CEP consultado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ViaCepResponse.class))),
            @ApiResponse(responseCode = "400", description = "CEP inválido"),
            @ApiResponse(responseCode = "404", description = "CEP não encontrado")})
    public ResponseEntity<ViaCepResponse> consultarCep (
            @Parameter(description = "CEP com 8 dígitos", example = "01001000") @PathVariable String cep) {
        return ResponseEntity.ok(cepService.consultar(cep));
    }
}
