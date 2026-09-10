package br.edu.infnet.booknest.emprestimo.controller;

import br.edu.infnet.booknest.emprestimo.controller.dto.*;
import br.edu.infnet.booknest.emprestimo.model.*;
import br.edu.infnet.booknest.emprestimo.service.*;
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
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Payload do empréstimo a ser criado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmprestimoRequest.class), examples = @ExampleObject(name = "Emprestimo", value = "{\n  \"usuarioId\": 1,\n  \"exemplarId\": 4,\n  \"dataEmprestimo\": \"2026-08-18\",\n  \"dataEsperadaDevolucao\": \"2026-09-01\",\n  \"multa\": 0.0\n}")))
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Empréstimo criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmprestimoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")})
    public ResponseEntity<EmprestimoResponse> incluir (
            @Valid @org.springframework.web.bind.annotation.RequestBody EmprestimoRequest request) {
        Emprestimo entidade = emprestimoService.incluir(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(EmprestimoResponse.from(entidade));
    }

    @GetMapping
    @Operation(summary = "Listar empréstimos", description = "Retorna empréstimos com filtro opcional por situação de atraso.")
    @io.swagger.v3.oas.annotations.Parameters({
            @io.swagger.v3.oas.annotations.Parameter(name = "atrasado", in = ParameterIn.QUERY, description = "Filtro opcional: true para atrasados, false para não atrasados. Sem parâmetro retorna todos.", example = "true")})
    @ApiResponse(responseCode = "200", description = "Empréstimos retornados com sucesso", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EmprestimoResponse.class))))
    public ResponseEntity<List<EmprestimoResponse>> listar (
            @RequestParam(name = "atrasado", required = false) Boolean atrasado) {
        return ResponseEntity.ok(
                emprestimoService.listarPorSituacaoAtraso(atrasado).stream().map(EmprestimoResponse::from).toList());
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Listar empréstimos do usuário", description = "Retorna todos os empréstimos vinculados a um usuário específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empréstimos retornados com sucesso", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = EmprestimoResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")})
    public ResponseEntity<List<EmprestimoResponse>> listarPorUsuario (@PathVariable Long usuarioId) {
        return ResponseEntity.ok(
                emprestimoService.listarPorUsuario(usuarioId).stream().map(EmprestimoResponse::from).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter empréstimo por ID", description = "Retorna um empréstimo específico pelo identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empréstimo encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmprestimoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado")})
    public ResponseEntity<EmprestimoResponse> obterPorId (@PathVariable Long id) {
        return ResponseEntity.ok(EmprestimoResponse.from(emprestimoService.obterPorId(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar empréstimo", description = "Atualiza um empréstimo existente pelo ID informado.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Payload atualizado do empréstimo.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmprestimoRequest.class), examples = @ExampleObject(name = "Emprestimo", value = "{\n  \"usuarioId\": 2,\n  \"exemplarId\": 3,\n  \"dataEmprestimo\": \"2025-06-05\",\n  \"dataEsperadaDevolucao\": \"2025-06-20\",\n  \"multa\": 5.5\n}")))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empréstimo atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmprestimoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado")})
    public ResponseEntity<EmprestimoResponse> alterar (@PathVariable Long id,
                                                       @Valid @org.springframework.web.bind.annotation.RequestBody EmprestimoRequest request) {
        return ResponseEntity.ok(EmprestimoResponse.from(emprestimoService.alterar(request.toEntity(id))));
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
    @Operation(summary = "Registrar devolução", description = "Registra a devolução de um empréstimo existente.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Dados da devolução a ser registrada.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = RegistrarDevolucaoRequest.class), examples = @ExampleObject(value = "{\n  \"dataDevolucao\": \"2026-08-18\",\n  \"multa\": 2.5\n}")))
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Devolução registrada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EmprestimoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou empréstimo já devolvido"),
            @ApiResponse(responseCode = "404", description = "Empréstimo não encontrado")})
    public ResponseEntity<EmprestimoResponse> registrarDevolucao (
            @Parameter(description = "ID do empréstimo", example = "2") @PathVariable Long id,
            @Valid @org.springframework.web.bind.annotation.RequestBody RegistrarDevolucaoRequest request) {
        return ResponseEntity.ok(EmprestimoResponse.from(
                emprestimoService.registrarDevolucao(id, request.dataDevolucao(), request.multa())));
    }
}
