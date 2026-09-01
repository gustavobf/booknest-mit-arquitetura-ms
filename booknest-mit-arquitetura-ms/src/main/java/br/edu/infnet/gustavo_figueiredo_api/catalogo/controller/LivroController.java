package br.edu.infnet.gustavo_figueiredo_api.catalogo.controller;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.*;
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
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Payload do livro a ser criado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Livro.class), examples = @ExampleObject(name = "Livro", value = "{\n  \"titulo\": \"A Paixão Segundo G.H.\",\n  \"isbn\": \"978-8520926658\",\n  \"autor\": { \"id\": 2 },\n  \"categoria\": { \"id\": 1 },\n  \"editora\": { \"id\": 1 }\n}")))
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Livro criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")})
    public ResponseEntity<Livro> incluir (@Valid @RequestBody Livro entidade) {
        Livro entidadeCriada = livroService.incluir(entidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(entidadeCriada);
    }

    @GetMapping
    @Operation(summary = "Listar livros", description = "Retorna livros com filtro opcional por disponibilidade. Sem parâmetro, retorna todos.")
    @Parameters({
            @Parameter(name = "disponivel", in = ParameterIn.QUERY, description = "Filtro opcional: true para disponíveis, false para indisponíveis. Sem parâmetro retorna todos.", example = "true")})
    @ApiResponse(responseCode = "200", description = "Livros listados com sucesso", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Livro.class))))
    public ResponseEntity<List<Livro>> listar (
            @RequestParam(name = "disponivel", required = false) Boolean disponivel) {
        return ResponseEntity.ok(livroService.listarPorDisponibilidade(disponivel));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter livro por ID", description = "Retorna um livro específico pelo identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Livro encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Livro.class))),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")})
    public ResponseEntity<Livro> obterPorId (@PathVariable Long id) {
        return ResponseEntity.ok(livroService.obterPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar livro", description = "Atualiza um livro existente pelo ID informado.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Payload atualizado do livro.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Livro.class), examples = @ExampleObject(name = "Livro", value = "{\n  \"titulo\": \"Dom Casmurro (Edição Revisada)\",\n  \"isbn\": \"978-8535905571\",\n  \"autor\": { \"id\": 1 },\n  \"categoria\": { \"id\": 2 },\n  \"editora\": { \"id\": 1 }\n}")))
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")})
    public ResponseEntity<Livro> alterar (@PathVariable Long id, @Valid @RequestBody Livro entidade) {
        entidade.setId(id);
        return ResponseEntity.ok(livroService.alterar(entidade));
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
