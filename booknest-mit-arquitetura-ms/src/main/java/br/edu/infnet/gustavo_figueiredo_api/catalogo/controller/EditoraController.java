package br.edu.infnet.gustavo_figueiredo_api.catalogo.controller;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.controller.dto.EditoraRequest;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.controller.dto.EditoraResponse;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.Editora;
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
@RequestMapping("/editoras")
@Tag(name = "Editoras")
public class EditoraController {

    private final EditoraService editoraService;

    public EditoraController (EditoraService editoraService) {
        this.editoraService = editoraService;
    }

    @PostMapping
    @Operation(summary = "Criar editora", description = "Cria uma nova editora.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Payload da editora a ser criada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EditoraRequest.class), examples = @ExampleObject(name = "Editora", value = "{\n  \"nome\": \"Intrínseca\",\n  \"cidade\": \"Rio de Janeiro\",\n  \"emailContato\": \"contato@intrinseca.com.br\",\n  \"ativa\": true\n}")))
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Editora criada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EditoraResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")})
    public ResponseEntity<EditoraResponse> incluir (@Valid @RequestBody EditoraRequest request) {
        Editora entidadeCriada = editoraService.incluir(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(EditoraResponse.from(entidadeCriada));
    }

    @GetMapping
    @Operation(summary = "Listar editoras", description = "Retorna editoras com filtro opcional por status de atividade. Sem parâmetro, retorna todas.")
    @Parameters({
            @Parameter(name = "ativa", in = ParameterIn.QUERY, description = "Filtro opcional: true para ativas, false para inativas. Sem parâmetro retorna todas.", example = "true")})
    @ApiResponse(responseCode = "200", description = "Editoras listadas com sucesso", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EditoraResponse.class))))
    public ResponseEntity<List<EditoraResponse>> listar (@RequestParam(name = "ativa", required = false) Boolean ativa) {
        return ResponseEntity.ok(editoraService.listarPorAtiva(ativa).stream().map(EditoraResponse::from).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter editora por ID", description = "Retorna uma editora específica pelo identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Editora encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EditoraResponse.class))),
            @ApiResponse(responseCode = "404", description = "Editora não encontrada")})
    public ResponseEntity<EditoraResponse> obterPorId (@PathVariable Long id) {
        return ResponseEntity.ok(EditoraResponse.from(editoraService.obterPorId(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar editora", description = "Atualiza uma editora existente pelo ID informado.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Payload atualizado da editora.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EditoraRequest.class), examples = @ExampleObject(name = "Editora", value = "{\n  \"nome\": \"Companhia das Letras\",\n  \"cidade\": \"São Paulo\",\n  \"emailContato\": \"atendimento@companhiadasletras.com.br\",\n  \"ativa\": true\n}")))
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Editora atualizada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EditoraResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Editora não encontrada")})
    public ResponseEntity<EditoraResponse> alterar (@PathVariable Long id, @Valid @RequestBody EditoraRequest request) {
        return ResponseEntity.ok(EditoraResponse.from(editoraService.alterar(request.toEntity(id))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir editora", description = "Remove uma editora existente pelo ID informado.")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Editora excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Editora não encontrada")})
    public ResponseEntity<Void> excluir (@PathVariable Long id) {
        editoraService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/cidade-por-cep")
    @Operation(summary = "Atualizar cidade da editora por CEP", description = "Consulta uma API externa de CEP e atualiza a cidade da editora com o resultado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cidade da editora atualizada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EditoraResponse.class))),
            @ApiResponse(responseCode = "400", description = "CEP inválido ou API externa sem cidade"),
            @ApiResponse(responseCode = "404", description = "Editora ou CEP não encontrado")})
    public ResponseEntity<EditoraResponse> atualizarCidadePorCep (
            @Parameter(description = "ID da editora", example = "1") @PathVariable Long id,
            @Parameter(description = "CEP com 8 dígitos", example = "01001000") @RequestParam String cep) {
        return ResponseEntity.ok(EditoraResponse.from(editoraService.atualizarCidadePorCep(id, cep)));
    }
}
