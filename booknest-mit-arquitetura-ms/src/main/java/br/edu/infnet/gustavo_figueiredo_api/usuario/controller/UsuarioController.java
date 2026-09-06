package br.edu.infnet.gustavo_figueiredo_api.usuario.controller;

import br.edu.infnet.gustavo_figueiredo_api.usuario.controller.dto.UsuarioRequest;
import br.edu.infnet.gustavo_figueiredo_api.usuario.controller.dto.UsuarioResponse;
import br.edu.infnet.gustavo_figueiredo_api.usuario.model.Usuario;
import br.edu.infnet.gustavo_figueiredo_api.usuario.service.*;
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
@RequestMapping("/usuarios")
@Tag(name = "Usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController (UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    @Operation(summary = "Criar usuário", description = "Cria um novo usuário.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Payload do usuário a ser criado.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioRequest.class), examples = @ExampleObject(name = "Usuario", value = "{\n  \"nome\": \"Fernanda Alves\",\n  \"email\": \"fernanda.alves@biblioteca.com\",\n  \"matricula\": \"MAT2026999\",\n  \"ativo\": true\n}")))
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Usuário criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")})
    public ResponseEntity<UsuarioResponse> incluir (@Valid @RequestBody UsuarioRequest request) {
        Usuario entidadeCriada = usuarioService.incluir(request.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioResponse.from(entidadeCriada));
    }

    @GetMapping
    @Operation(summary = "Listar usuários", description = "Retorna usuários com filtro opcional por status de atividade. Sem parâmetro, retorna todos.")
    @Parameters({
            @Parameter(name = "ativo", in = ParameterIn.QUERY, description = "Filtro opcional: true para ativos, false para inativos. Sem parâmetro retorna todos.", example = "true")})
    @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioResponse.class))))
    public ResponseEntity<List<UsuarioResponse>> listar (@RequestParam(name = "ativo", required = false) Boolean ativo) {
        return ResponseEntity.ok(usuarioService.listarPorAtivo(ativo).stream().map(UsuarioResponse::from).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obter usuário por ID", description = "Retorna um usuário específico pelo identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")})
    public ResponseEntity<UsuarioResponse> obterPorId (@PathVariable Long id) {
        return ResponseEntity.ok(UsuarioResponse.from(usuarioService.obterPorId(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza um usuário existente pelo ID informado.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "Payload atualizado do usuário.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioRequest.class), examples = @ExampleObject(name = "Usuário", value = "{\n  \"nome\": \"João Silva\",\n  \"email\": \"joao.silva@biblioteca.com\",\n  \"matricula\": \"MAT2025001\",\n  \"ativo\": true\n}")))
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")})
    public ResponseEntity<UsuarioResponse> alterar (@PathVariable Long id, @Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(UsuarioResponse.from(usuarioService.alterar(request.toEntity(id))));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir usuário", description = "Remove um usuário existente pelo ID informado.")
    @ApiResponses({@ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")})
    public ResponseEntity<Void> excluir (@PathVariable Long id) {
        usuarioService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/emprestimos")
    @Operation(summary = "Listar empréstimos do usuário", description = "Retorna o histórico de empréstimos de um usuário específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Empréstimos retornados com sucesso", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = br.edu.infnet.gustavo_figueiredo_api.emprestimo.controller.dto.EmprestimoResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")})
    public ResponseEntity<List<br.edu.infnet.gustavo_figueiredo_api.emprestimo.controller.dto.EmprestimoResponse>> listarEmprestimosDoUsuario (
            @Parameter(description = "ID do usuário", example = "1") @PathVariable Long id) {
        return ResponseEntity.ok(usuarioService.obterPorId(id).getEmprestimos().stream()
                .map(br.edu.infnet.gustavo_figueiredo_api.emprestimo.controller.dto.EmprestimoResponse::from).toList());
    }
}
