package br.edu.infnet.gustavo_figueiredo_api.catalogo.controller;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.controller.dto.ExemplarRequest;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.controller.dto.ExemplarResponse;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.Exemplar;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.service.*;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.enums.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/exemplares")
@Tag(name = "Exemplares")
public class ExemplarController {

    private final ExemplarService exemplarService;

    public ExemplarController (ExemplarService exemplarService) {
        this.exemplarService = exemplarService;
    }

    @PostMapping
    @Operation(summary = "Criar exemplar", description = "Cria um novo exemplar.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Payload do exemplar a ser criado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExemplarRequest.class), examples = @ExampleObject(name = "Exemplar", value = "{\n  \"codigo\": \"DOM-003\",\n  \"estadoConservacao\": \"BOM\",\n  \"disponivel\": true,\n  \"livroId\": 1\n}")))
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Exemplar criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExemplarResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")})
    public ResponseEntity<ExemplarResponse> incluir (@Valid @RequestBody ExemplarRequest request) {
        Exemplar entidadeCriada = exemplarService.incluir(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(ExemplarResponse.from(entidadeCriada));
    }

    @GetMapping
    @Operation(summary = "Listar exemplares", description = "Retorna exemplares com filtro opcional por disponibilidade. Sem parâmetro, retorna todos.")
    @Parameters({
            @Parameter(name = "disponivel", in = ParameterIn.QUERY, description = "Filtro opcional: true para disponíveis, false para indisponíveis. Sem parâmetro retorna todos.", example = "true")})
    @ApiResponse(responseCode = "200", description = "Exemplares listados com sucesso", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ExemplarResponse.class))))
    public ResponseEntity<List<ExemplarResponse>> listar (
            @RequestParam(name = "disponivel", required = false) Boolean disponivel) {
        return ResponseEntity.ok(exemplarService.listarPorDisponibilidade(disponivel).stream().map(ExemplarResponse::from).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter exemplar por ID", description = "Retorna um exemplar específico pelo identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exemplar encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExemplarResponse.class))),
            @ApiResponse(responseCode = "404", description = "Exemplar não encontrado")})
    public ResponseEntity<ExemplarResponse> obterPorId (@PathVariable Long id) {
        return ResponseEntity.ok(ExemplarResponse.from(exemplarService.obterPorId(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar exemplar", description = "Atualiza um exemplar existente pelo ID informado.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Payload atualizado do exemplar.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExemplarRequest.class), examples = @ExampleObject(name = "Exemplar", value = "{\n  \"codigo\": \"DOM-001\",\n  \"estadoConservacao\": \"EXCELENTE\",\n  \"disponivel\": true,\n  \"livroId\": 1\n}")))
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Exemplar atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExemplarResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Exemplar não encontrado")})
    public ResponseEntity<ExemplarResponse> alterar (@PathVariable Long id, @Valid @RequestBody ExemplarRequest request) {
        return ResponseEntity.ok(ExemplarResponse.from(exemplarService.alterar(request.toEntity(id))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir exemplar", description = "Remove um exemplar existente pelo ID informado.")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Exemplar excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Exemplar não encontrado")})
    public ResponseEntity<Void> excluir (@PathVariable Long id) {
        exemplarService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/livro/{idLivro}")
    @Operation(summary = "Listar exemplares por livro", description = "Retorna os exemplares associados a um livro específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Exemplares do livro retornados com sucesso", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ExemplarResponse.class)))),
            @ApiResponse(responseCode = "400", description = "ID do livro inválido")})
    public ResponseEntity<List<ExemplarResponse>> listarPorLivro (
            @Parameter(description = "ID do livro", example = "1") @PathVariable Long idLivro) {
        return ResponseEntity.ok(exemplarService.listarPorLivro(idLivro).stream().map(ExemplarResponse::from).toList());
    }
}
