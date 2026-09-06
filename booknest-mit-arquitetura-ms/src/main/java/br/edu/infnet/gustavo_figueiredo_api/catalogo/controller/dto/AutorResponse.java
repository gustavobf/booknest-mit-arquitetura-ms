package br.edu.infnet.gustavo_figueiredo_api.catalogo.controller.dto;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.Autor;

public record AutorResponse(Long id, String nome, String nacionalidade, Integer anoNascimento, long totalLivros) {
    public static AutorResponse from (Autor autor) {
        return new AutorResponse(autor.getId(), autor.getNome(), autor.getNacionalidade(), autor.getAnoNascimento(),
                autor.getLivros() != null ? autor.getLivros().size() : 0);
    }
}
