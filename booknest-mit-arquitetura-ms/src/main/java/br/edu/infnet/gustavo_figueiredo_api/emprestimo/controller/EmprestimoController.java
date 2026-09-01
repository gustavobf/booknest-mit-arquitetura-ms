package br.edu.infnet.gustavo_figueiredo_api.emprestimo.controller;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.Exemplar;
import br.edu.infnet.gustavo_figueiredo_api.emprestimo.controller.dto.RegistrarDevolucaoRequest;
import br.edu.infnet.gustavo_figueiredo_api.emprestimo.model.Emprestimo;
import br.edu.infnet.gustavo_figueiredo_api.emprestimo.service.EmprestimoService;
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
@RequestMapping("/emprestimos")
@Tag(name = "Empréstimos")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    public EmprestimoController (EmprestimoService emprestimoService) {
        this.emprestimoService = emprestimoService;
    }

    @PostMapping
    @Operation(summary = "Criar empréstimo", description = "Cria um novo empréstimo.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Payload do empréstimo a ser criado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Emprestimo.class), examples = @ExampleObject(name = "Emprestimo", value = "{\n  \"usuario\": { \"id\": 1 },\n  \"exemplar\": { \"id\": 4 },\n  \"dataEmprestimo\": \"2026-08-18\",\n  \"dataEsperadaDevolucao\": \"2026-09-01\",\n  \"dataDevolucao\": null,\n  \"multa\": 0.0\n}")))
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Empréstimo criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")})
    public ResponseEntity<Emprestimo> incluir (@Valid @RequestBody Emprestimo entidade) {
        Emprestimo entidadeCriada = emprestimoService.incluir(entidade);
        return ResponseEntity.status(HttpStatus.CREATED).body(entidadeCriada);
    }

    @GetMapping
    @Operation(summary = "Listar empréstimos", description = "Retorna empréstimos com filtro opcional por situação de atraso. Sem parâmetro, retorna todos.")
    @Parameters({
            @Parameter(name = "atrasado", in = ParameterIn.QUERY, description = "Filtro opcional: true para atrasados, false para não atrasados. Sem parâmetro retorna todos.", example = "true")})
    @ApiResponse(responseCode = "200", description = "Empréstimos retornados com sucesso", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Emprestimo.class))))
    public ResponseEntity<List<Emprestimo>> listar (
            @RequestParam(name = "atrasado", required = false) Boolean atrasado) {
        return ResponseEntity.ok(emprestimoService.listarPorSituacaoAtraso(atrasado));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter empréstimo por ID", description = "Retorna um empréstimo específico pelo identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empréstimo encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Emprestimo.class))),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado")})
    public ResponseEntity<Emprestimo> obterPorId (@PathVariable Long id) {
        return ResponseEntity.ok(emprestimoService.obterPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar empréstimo", description = "Atualiza um empréstimo existente pelo ID informado.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Payload atualizado do empréstimo.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Emprestimo.class), examples = @ExampleObject(name = "Empréstimo", value = "{\n  \"usuario\": { \"id\": 2 },\n  \"exemplar\": { \"id\": 3 },\n  \"dataEmprestimo\": \"2025-06-05\",\n  \"dataEsperadaDevolucao\": \"2025-06-20\",\n  \"dataDevolucao\": \"2025-06-22\",\n  \"multa\": 5.5\n}")))
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Empréstimo atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado")})
    public ResponseEntity<Emprestimo> alterar (@PathVariable Long id, @Valid @RequestBody Emprestimo entidade) {
        entidade.setId(id);
        return ResponseEntity.ok(emprestimoService.alterar(entidade));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir empréstimo", description = "Remove um empréstimo existente pelo ID informado.")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Empréstimo excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado")})
    public ResponseEntity<Void> excluir (@PathVariable Long id) {
        emprestimoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/devolucao")
    @Operation(summary = "Registrar devolução", description = "Registra a devolução de um empréstimo existente, atualizando data de devolução e multa.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Dados da devolução a ser registrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegistrarDevolucaoRequest.class), examples = @ExampleObject(value = "{\n  \"dataDevolucao\": \"2026-08-18\",\n  \"multa\": 2.5\n}")))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Devolução registrada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Emprestimo.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou empréstimo já devolvido"),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado")})
    public ResponseEntity<Emprestimo> registrarDevolucao (
            @Parameter(description = "ID do empréstimo", example = "2") @PathVariable Long id,
            @Valid @RequestBody RegistrarDevolucaoRequest request) {
        return ResponseEntity.ok(emprestimoService.registrarDevolucao(id, request.dataDevolucao(), request.multa()));
    }
}
