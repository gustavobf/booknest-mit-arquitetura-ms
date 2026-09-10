package br.edu.infnet.booknest.emprestimo.controller.dto;

import br.edu.infnet.booknest.emprestimo.model.*;

import java.time.*;

public record EmprestimoResponse(Long id, Long usuarioId, Long exemplarId, LocalDate dataEmprestimo,
                                 LocalDate dataEsperadaDevolucao, LocalDate dataDevolucao, Double multa) {

    public static EmprestimoResponse from (Emprestimo emprestimo) {
        return new EmprestimoResponse(emprestimo.getId(), emprestimo.getUsuarioId(), emprestimo.getExemplarId(),
                emprestimo.getDataEmprestimo(), emprestimo.getDataEsperadaDevolucao(), emprestimo.getDataDevolucao(),
                emprestimo.getMulta());
    }
}
