package br.edu.infnet.booknest.emprestimo.controller.dto;

import br.edu.infnet.booknest.emprestimo.model.*;
import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.constraints.*;

import java.time.*;

public record EmprestimoRequest(
        @Schema(description = "ID do usuário", example = "1") @NotNull(message = "Empréstimo deve possuir usuário.") Long usuarioId,

        @Schema(description = "ID do exemplar", example = "4") @NotNull(message = "Empréstimo deve possuir exemplar.") Long exemplarId,

        @Schema(description = "Data de início do empréstimo", example = "2026-08-18") @NotNull(message = "Data do empréstimo é obrigatória.") LocalDate dataEmprestimo,

        @Schema(description = "Data prevista para devolução", example = "2026-09-01") @NotNull(message = "Data esperada de devolução é obrigatória.") LocalDate dataEsperadaDevolucao,

        @Schema(description = "Valor da multa", example = "0.0") @NotNull(message = "Multa é obrigatória.") @DecimalMin(value = "0.0", message = "Multa não pode ser negativa.") Double multa) {

    public Emprestimo toEntity () {
        return toEntity(null);
    }

    public Emprestimo toEntity (Long id) {
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setId(id);
        emprestimo.setUsuarioId(usuarioId);
        emprestimo.setExemplarId(exemplarId);
        emprestimo.setDataEmprestimo(dataEmprestimo);
        emprestimo.setDataEsperadaDevolucao(dataEsperadaDevolucao);
        emprestimo.setMulta(multa);
        return emprestimo;
    }
}
