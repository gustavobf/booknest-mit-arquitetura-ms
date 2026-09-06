package br.edu.infnet.gustavo_figueiredo_api.catalogo.controller.dto;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record LivroRequest(
        @Schema(description = "Título do livro", example = "Dom Casmurro")
        @NotBlank(message = "Título do livro é obrigatório.")
        @Size(max = 160, message = "Título do livro deve ter no máximo 160 caracteres.")
        String titulo,

        @Schema(description = "ISBN do livro", example = "978-8535905571")
        @NotBlank(message = "ISBN do livro é obrigatório.")
        @Size(max = 20, message = "ISBN deve ter no máximo 20 caracteres.")
        String isbn,

        @Schema(description = "ID do autor", example = "2")
        @NotNull(message = "Livro deve possuir autor.")
        Long autorId,

        @Schema(description = "ID da categoria", example = "1")
        @NotNull(message = "Livro deve possuir categoria.")
        Long categoriaId,

        @Schema(description = "ID da editora", example = "1")
        @NotNull(message = "Livro deve possuir editora.")
        Long editoraId) {

    public Livro toEntity () {
        return toEntity(null);
    }

    public Livro toEntity (Long id) {
        Livro livro = new Livro();
        livro.setId(id);
        livro.setTitulo(titulo);
        livro.setIsbn(isbn);

        Autor autor = new Autor();
        autor.setId(autorId);
        livro.setAutor(autor);

        Categoria categoria = new Categoria();
        categoria.setId(categoriaId);
        livro.setCategoria(categoria);

        Editora editora = new Editora();
        editora.setId(editoraId);
        livro.setEditora(editora);

        return livro;
    }
}
