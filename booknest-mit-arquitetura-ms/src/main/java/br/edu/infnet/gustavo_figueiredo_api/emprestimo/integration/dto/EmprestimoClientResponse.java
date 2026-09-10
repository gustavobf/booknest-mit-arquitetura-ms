package br.edu.infnet.gustavo_figueiredo_api.emprestimo.integration.dto;

import java.time.*;

public record EmprestimoClientResponse(Long id, Long usuarioId, Long exemplarId, LocalDate dataEmprestimo,
                                       LocalDate dataEsperadaDevolucao, LocalDate dataDevolucao, Double multa) {
}
