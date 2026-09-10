package br.edu.infnet.booknest.emprestimo.exception;

import java.time.*;

public record ErroResposta(LocalDateTime timestamp, int status, String error, String message, String path) {
}
