package br.edu.infnet.gustavo_figueiredo_api.emprestimo.controller.dto;

import br.edu.infnet.gustavo_figueiredo_api.emprestimo.model.Emprestimo;

import java.time.LocalDate;

public record EmprestimoResponse(Long id, Long usuarioId, String usuarioNome, Long exemplarId, String exemplarCodigo,
                                 String livroTitulo, LocalDate dataEmprestimo, LocalDate dataEsperadaDevolucao,
                                 LocalDate dataDevolucao, Double multa, Boolean devolvido, Boolean atrasado) {
    public static EmprestimoResponse from (Emprestimo emprestimo) {
        return new EmprestimoResponse(emprestimo.getId(),
                emprestimo.getUsuario() != null ? emprestimo.getUsuario().getId() : null,
                emprestimo.getUsuario() != null ? emprestimo.getUsuario().getNome() : null,
                emprestimo.getExemplar() != null ? emprestimo.getExemplar().getId() : null,
                emprestimo.getExemplar() != null ? emprestimo.getExemplar().getCodigo() : null,
                emprestimo.getExemplar() != null && emprestimo.getExemplar().getLivro() != null
                        ? emprestimo.getExemplar().getLivro().getTitulo() : null,
                emprestimo.getDataEmprestimo(), emprestimo.getDataEsperadaDevolucao(), emprestimo.getDataDevolucao(),
                emprestimo.getMulta(), emprestimo.estaDevolvido(), emprestimo.estaAtrasado());
    }
}
