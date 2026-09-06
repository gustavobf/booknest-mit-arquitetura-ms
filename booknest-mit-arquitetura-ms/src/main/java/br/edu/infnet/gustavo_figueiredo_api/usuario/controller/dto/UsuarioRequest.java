package br.edu.infnet.gustavo_figueiredo_api.usuario.controller.dto;

import br.edu.infnet.gustavo_figueiredo_api.usuario.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record UsuarioRequest(
        @Schema(description = "Nome do usuário", example = "João Silva")
        @NotBlank(message = "Nome do usuário é obrigatório.")
        @Size(max = 120, message = "Nome do usuário deve ter no máximo 120 caracteres.")
        String nome,

        @Schema(description = "E-mail do usuário", example = "joao.silva@biblioteca.com")
        @NotBlank(message = "E-mail do usuário é obrigatório.")
        @Email(message = "E-mail do usuário inválido.")
        String email,

        @Schema(description = "Matrícula do usuário", example = "MAT2025001")
        @NotBlank(message = "Matrícula do usuário é obrigatória.")
        @Size(max = 30, message = "Matrícula deve ter no máximo 30 caracteres.")
        String matricula,

        @Schema(description = "Indica se o usuário está ativo", example = "true")
        @NotNull(message = "Situação de atividade do usuário é obrigatória.")
        Boolean ativo) {

    public Usuario toEntity () {
        return toEntity(null);
    }

    public Usuario toEntity (Long id) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setMatricula(matricula);
        usuario.setAtivo(ativo);
        return usuario;
    }
}
