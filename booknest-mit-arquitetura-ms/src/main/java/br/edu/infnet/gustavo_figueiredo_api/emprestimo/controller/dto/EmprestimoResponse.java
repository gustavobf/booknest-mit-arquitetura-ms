package br.edu.infnet.gustavo_figueiredo_api.emprestimo.controller.dto;

import br.edu.infnet.gustavo_figueiredo_api.emprestimo.integration.dto.*;

import java.time.*;

public record EmprestimoResponse(Long id, Long usuarioId, Long exemplarId, LocalDate dataEmprestimo,
                                 LocalDate dataEsperadaDevolucao, LocalDate dataDevolucao, Double multa) {

    public static EmprestimoResponse fromClientDto (EmprestimoClientResponse response) {
        return new EmprestimoResponse(response.id(), response.usuarioId(), response.exemplarId(),
                response.dataEmprestimo(), response.dataEsperadaDevolucao(), response.dataDevolucao(),
                response.multa());
    }
}
