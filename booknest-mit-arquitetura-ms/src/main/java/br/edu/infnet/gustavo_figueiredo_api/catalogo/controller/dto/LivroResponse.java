package br.edu.infnet.gustavo_figueiredo_api.catalogo.controller.dto;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.Livro;

public record LivroResponse(Long id, String titulo, String isbn, Long autorId, String autorNome, Long categoriaId,
                            String categoriaNome, Long editoraId, String editoraNome, Boolean disponivel,
                            long quantidadeExemplaresDisponiveis, long quantidadeExemplaresEmprestados) {
    public static LivroResponse from (Livro livro) {
        return new LivroResponse(livro.getId(), livro.getTitulo(), livro.getIsbn(),
                livro.getAutor() != null ? livro.getAutor().getId() : null,
                livro.getAutor() != null ? livro.getAutor().getNome() : null,
                livro.getCategoria() != null ? livro.getCategoria().getId() : null,
                livro.getCategoria() != null ? livro.getCategoria().getNome() : null,
                livro.getEditora() != null ? livro.getEditora().getId() : null,
                livro.getEditora() != null ? livro.getEditora().getNome() : null, livro.getDisponivel(),
                livro.getQuantidadeExemplaresDisponiveis(), livro.getQuantidadeExemplaresEmprestados());
    }
}
