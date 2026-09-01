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
@RequestMapping("/autores")
@Tag(name = "Autores")
public class AutorController {

    private final AutorService autorService;

    public AutorController (AutorService autorService) {
        this.autorService = autorService;
    }

    @PostMapping
    @Operation(summary = "Criar autor", description = "Cria um novo autor.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Payload do autor a ser criado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Autor.class), examples = @ExampleObject(name = "Autor", value = "{\n  \"nome\": \"Lygia Fagundes Telles\",\n  \"nacionalidade\": \"Brasileira\",\n  \"anoNascimento\": 1923\n}")))
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Autor criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")})
    public ResponseEntity<Autor> incluir (@Valid @RequestBody Autor entidade) {
        Autor entidadeCriada = autorService.incluir(entidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(entidadeCriada);
    }

    @GetMapping
    @Operation(summary = "Listar autores", description = "Retorna autores, com ordenação opcional por nome.")
    @Parameters({
            @Parameter(name = "ordenarPorNome", in = ParameterIn.QUERY, description = "Quando true, retorna autores ordenados alfabeticamente por nome.", example = "true")})
    @ApiResponse(responseCode = "200", description = "Autores listados com sucesso", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Autor.class))))
    public ResponseEntity<List<Autor>> listar (
            @RequestParam(name = "ordenarPorNome", required = false) Boolean ordenarPorNome) {
        return ResponseEntity.ok(autorService.listar(ordenarPorNome));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter autor por ID", description = "Retorna um autor específico pelo identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autor encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Autor.class))),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado")})
    public ResponseEntity<Autor> obterPorId (@PathVariable Long id) {
        return ResponseEntity.ok(autorService.obterPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar autor", description = "Atualiza um autor existente pelo ID informado.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Payload atualizado do autor.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Autor.class), examples = @ExampleObject(name = "Autor", value = "{\n  \"nome\": \"Machado de Assis (Atualizado)\",\n  \"nacionalidade\": \"Brasileiro\",\n  \"anoNascimento\": 1839\n}")))
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Autor atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado")})
    public ResponseEntity<Autor> alterar (@PathVariable Long id, @Valid @RequestBody Autor entidade) {
        entidade.setId(id);
        return ResponseEntity.ok(autorService.alterar(entidade));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir autor", description = "Remove um autor existente pelo ID informado.")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Autor excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Autor não encontrado")})
    public ResponseEntity<Void> excluir (@PathVariable Long id) {
        autorService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nacionalidade/{nacionalidade}")
    @Operation(summary = "Buscar autores por nacionalidade", description = "Filtra autores pela nacionalidade informada.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autores retornados com sucesso", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Autor.class)))),
            @ApiResponse(responseCode = "400", description = "Nacionalidade inválida")})
    public ResponseEntity<List<Autor>> buscarPorNacionalidade (
            @Parameter(description = "Nacionalidade a filtrar", example = "Brasileiro") @PathVariable String nacionalidade) {
        return ResponseEntity.ok(autorService.buscarPorNacionalidade(nacionalidade));
    }
}
