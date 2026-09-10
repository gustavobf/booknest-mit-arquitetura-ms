package br.edu.infnet.gustavo_figueiredo_api.emprestimo.integration.dto;

import java.time.*;

public record RegistrarDevolucaoClientRequest(LocalDate dataDevolucao, Double multa) {
}
