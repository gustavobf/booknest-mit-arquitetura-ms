package br.edu.infnet.gustavo_figueiredo_api.catalogo.controller.dto;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.Categoria;

public record CategoriaResponse(Long id, String nome, String descricao, long totalLivros) {
    public static CategoriaResponse from (Categoria categoria) {
        return new CategoriaResponse(categoria.getId(), categoria.getNome(), categoria.getDescricao(),
                categoria.getLivros() != null ? categoria.getLivros().size() : 0);
    }
}
