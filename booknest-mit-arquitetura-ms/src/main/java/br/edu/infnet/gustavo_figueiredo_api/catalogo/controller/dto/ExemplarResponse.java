package br.edu.infnet.gustavo_figueiredo_api.catalogo.controller.dto;

import br.edu.infnet.gustavo_figueiredo_api.catalogo.model.Exemplar;

public record ExemplarResponse(Long id, String codigo, String estadoConservacao, Boolean disponivel, Long livroId,
                               String livroTitulo) {
    public static ExemplarResponse from (Exemplar exemplar) {
        return new ExemplarResponse(exemplar.getId(), exemplar.getCodigo(),
                exemplar.getEstadoConservacao() != null ? exemplar.getEstadoConservacao().name() : null,
                exemplar.getDisponivel(), exemplar.getLivro() != null ? exemplar.getLivro().getId() : null,
                exemplar.getLivro() != null ? exemplar.getLivro().getTitulo() : null);
    }
}
