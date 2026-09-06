package br.edu.infnet.gustavo_figueiredo_api.catalogo.controller;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.controller.dto.CategoriaRequest;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.controller.dto.CategoriaResponse;
import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.Categoria;
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
@RequestMapping("/categorias")
@Tag(name = "Categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController (CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    @Operation(summary = "Criar categoria", description = "Cria uma nova categoria.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Payload da categoria a ser criada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoriaRequest.class), examples = @ExampleObject(name = "Categoria", value = "{\n  \"nome\": \"Fantasia\",\n  \"descricao\": \"Obras de fantasia épica e urbana\"\n}")))
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Categoria criada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoriaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")})
    public ResponseEntity<CategoriaResponse> incluir (@Valid @RequestBody CategoriaRequest request) {
        Categoria entidadeCriada = categoriaService.incluir(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(CategoriaResponse.from(entidadeCriada));
    }

    @GetMapping
    @Operation(summary = "Listar categorias", description = "Retorna categorias, com ordenação opcional por quantidade de livros.")
    @Parameters({
            @Parameter(name = "ordenarPorQuantidadeLivros", in = ParameterIn.QUERY, description = "Quando true, retorna categorias ordenadas da maior para a menor quantidade de livros.", example = "true")})
    @ApiResponse(responseCode = "200", description = "Categorias listadas com sucesso", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CategoriaResponse.class))))
    public ResponseEntity<List<CategoriaResponse>> listar (
            @RequestParam(name = "ordenarPorQuantidadeLivros", required = false) Boolean ordenarPorQuantidadeLivros) {
        return ResponseEntity.ok(categoriaService.listar(ordenarPorQuantidadeLivros).stream().map(CategoriaResponse::from).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter categoria por ID", description = "Retorna uma categoria específica pelo identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoria encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoriaResponse.class))),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")})
    public ResponseEntity<CategoriaResponse> obterPorId (@PathVariable Long id) {
        return ResponseEntity.ok(CategoriaResponse.from(categoriaService.obterPorId(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar categoria", description = "Atualiza uma categoria existente pelo ID informado.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Payload atualizado da categoria.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoriaRequest.class), examples = @ExampleObject(name = "Categoria", value = "{\n  \"nome\": \"Ficção\",\n  \"descricao\": \"Narrativas de ficção literária (revisada)\"\n}")))
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CategoriaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")})
    public ResponseEntity<CategoriaResponse> alterar (@PathVariable Long id, @Valid @RequestBody CategoriaRequest request) {
        return ResponseEntity.ok(CategoriaResponse.from(categoriaService.alterar(request.toEntity(id))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir categoria", description = "Remove uma categoria existente pelo ID informado.")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")})
    public ResponseEntity<Void> excluir (@PathVariable Long id) {
        categoriaService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
