package br.edu.infnet.gustavo_figueiredo_api.catalogo.controller;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.controller.dto.LivroRequest;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.controller.dto.LivroResponse;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.Livro;
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
@RequestMapping("/livros")
@Tag(name = "Livros")
public class LivroController {

    private final LivroService livroService;

    public LivroController (LivroService livroService) {
        this.livroService = livroService;
    }

    @PostMapping
    @Operation(summary = "Criar livro", description = "Cria um novo livro.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Payload do livro a ser criado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LivroRequest.class), examples = @ExampleObject(name = "Livro", value = "{\n  \"titulo\": \"A Paixão Segundo G.H.\",\n  \"isbn\": \"978-8520926658\",\n  \"autorId\": 2,\n  \"categoriaId\": 1,\n  \"editoraId\": 1\n}")))
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Livro criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LivroResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")})
    public ResponseEntity<LivroResponse> incluir (@Valid @RequestBody LivroRequest request) {
        Livro entidadeCriada = livroService.incluir(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(LivroResponse.from(entidadeCriada));
    }

    @GetMapping
    @Operation(summary = "Listar livros", description = "Retorna livros com filtro opcional por disponibilidade. Sem parâmetro, retorna todos.")
    @Parameters({
            @Parameter(name = "disponivel", in = ParameterIn.QUERY, description = "Filtro opcional: true para disponíveis, false para indisponíveis. Sem parâmetro retorna todos.", example = "true")})
    @ApiResponse(responseCode = "200", description = "Livros listados com sucesso", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LivroResponse.class))))
    public ResponseEntity<List<LivroResponse>> listar (
            @RequestParam(name = "disponivel", required = false) Boolean disponivel) {
        return ResponseEntity.ok(livroService.listarPorDisponibilidade(disponivel).stream().map(LivroResponse::from).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter livro por ID", description = "Retorna um livro específico pelo identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LivroResponse.class))),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")})
    public ResponseEntity<LivroResponse> obterPorId (@PathVariable Long id) {
        return ResponseEntity.ok(LivroResponse.from(livroService.obterPorId(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar livro", description = "Atualiza um livro existente pelo ID informado.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Payload atualizado do livro.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LivroRequest.class), examples = @ExampleObject(name = "Livro", value = "{\n  \"titulo\": \"Dom Casmurro (Edição Revisada)\",\n  \"isbn\": \"978-8535905571\",\n  \"autorId\": 1,\n  \"categoriaId\": 2,\n  \"editoraId\": 1\n}")))
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LivroResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")})
    public ResponseEntity<LivroResponse> alterar (@PathVariable Long id, @Valid @RequestBody LivroRequest request) {
        return ResponseEntity.ok(LivroResponse.from(livroService.alterar(request.toEntity(id))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir livro", description = "Remove um livro existente pelo ID informado.")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Livro excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")})
    public ResponseEntity<Void> excluir (@PathVariable Long id) {
        livroService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
