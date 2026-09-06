package br.edu.infnet.gustavo_figueiredo_api.catalogo.controller.dto;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record ExemplarRequest(
        @Schema(description = "Código interno do exemplar", example = "DOM-001")
        @NotBlank(message = "Código do exemplar é obrigatório.")
        @Size(max = 30, message = "Código do exemplar deve ter no máximo 30 caracteres.")
        String codigo,

        @Schema(description = "Estado de conservação", example = "BOM")
        @NotNull(message = "Estado de conservação é obrigatório.")
        EstadoConservacao estadoConservacao,

        @Schema(description = "Disponibilidade do exemplar", example = "true")
        @NotNull(message = "Disponibilidade do exemplar é obrigatória.")
        Boolean disponivel,

        @Schema(description = "ID do livro", example = "1")
        @NotNull(message = "Exemplar deve estar associado a um livro.")
        Long livroId) {

    public Exemplar toEntity () {
        return toEntity(null);
    }

    public Exemplar toEntity (Long id) {
        Exemplar exemplar = new Exemplar();
        exemplar.setId(id);
        exemplar.setCodigo(codigo);
        exemplar.setEstadoConservacao(estadoConservacao);
        exemplar.setDisponivel(disponivel);
        Livro livro = new Livro();
        livro.setId(livroId);
        exemplar.setLivro(livro);
        return exemplar;
    }
}
