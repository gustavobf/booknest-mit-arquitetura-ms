package br.edu.infnet.gustavo_figueiredo_api.emprestimo.controller.dto;

import io.swagger.v3.oas.annotations.media.*;
import jakarta.validation.constraints.*;

import java.time.*;

@Schema(description = "Payload para registro de devolução de empréstimo.")
public record RegistrarDevolucaoRequest(
        @NotNull(message = "Data de devolução é obrigatória.")
        @Schema(description = "Data efetiva da devolução", example = "2026-08-18") LocalDate dataDevolucao,
        @NotNull(message = "Multa é obrigatória.")
        @PositiveOrZero(message = "Multa não pode ser negativa.")
        @Schema(description = "Valor de multa aplicado na devolução", example = "2.50") Double multa) {
}
