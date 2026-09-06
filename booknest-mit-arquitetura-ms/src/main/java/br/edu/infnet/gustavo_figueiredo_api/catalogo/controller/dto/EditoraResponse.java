package br.edu.infnet.gustavo_figueiredo_api.catalogo.controller.dto;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.Editora;

public record EditoraResponse(Long id, String nome, String cidade, String emailContato, Boolean ativa, long totalLivros) {
    public static EditoraResponse from (Editora editora) {
        return new EditoraResponse(editora.getId(), editora.getNome(), editora.getCidade(), editora.getEmailContato(),
                editora.getAtiva(), editora.getLivros() != null ? editora.getLivros().size() : 0);
    }
}
