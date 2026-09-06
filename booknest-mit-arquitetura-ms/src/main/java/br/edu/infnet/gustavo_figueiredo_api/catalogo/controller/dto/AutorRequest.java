package br.edu.infnet.gustavo_figueiredo_api.catalogo.controller.dto;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.Autor;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record AutorRequest(
        @Schema(description = "Nome completo do autor", example = "Machado de Assis")
        @NotBlank(message = "Nome do autor é obrigatório.")
        @Size(max = 120, message = "Nome do autor deve ter no máximo 120 caracteres.")
        String nome,

        @Schema(description = "Nacionalidade do autor", example = "Brasileiro")
        @NotBlank(message = "Nacionalidade do autor é obrigatória.")
        @Size(max = 80, message = "Nacionalidade do autor deve ter no máximo 80 caracteres.")
        String nacionalidade,

        @Schema(description = "Ano de nascimento", example = "1839")
        @NotNull(message = "Ano de nascimento é obrigatório.")
        @Min(value = 1000, message = "Ano de nascimento inválido.")
        Integer anoNascimento) {

    public Autor toEntity () {
        return toEntity(null);
    }

    public Autor toEntity (Long id) {
        Autor autor = new Autor();
        autor.setId(id);
        autor.setNome(nome);
        autor.setNacionalidade(nacionalidade);
        autor.setAnoNascimento(anoNascimento);
        return autor;
    }
}
