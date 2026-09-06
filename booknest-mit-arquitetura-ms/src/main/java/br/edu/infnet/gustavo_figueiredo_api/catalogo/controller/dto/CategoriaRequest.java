package br.edu.infnet.gustavo_figueiredo_api.catalogo.controller.dto;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.Categoria;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record CategoriaRequest(
        @Schema(description = "Nome da categoria", example = "Ficção")
        @NotBlank(message = "Nome da categoria é obrigatório.")
        @Size(max = 80, message = "Nome da categoria deve ter no máximo 80 caracteres.")
        String nome,

        @Schema(description = "Descrição da categoria", example = "Narrativas de ficção literária")
        @NotBlank(message = "Descrição da categoria é obrigatória.")
        @Size(max = 255, message = "Descrição da categoria deve ter no máximo 255 caracteres.")
        String descricao) {

    public Categoria toEntity () {
        return toEntity(null);
    }

    public Categoria toEntity (Long id) {
        Categoria categoria = new Categoria();
        categoria.setId(id);
        categoria.setNome(nome);
        categoria.setDescricao(descricao);
        return categoria;
    }
}
