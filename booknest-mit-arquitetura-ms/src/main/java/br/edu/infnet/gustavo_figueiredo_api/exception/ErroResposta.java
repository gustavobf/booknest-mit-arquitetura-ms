package br.edu.infnet.gustavo_figueiredo_api.exception;

import java.time.*;

public record ErroResposta(LocalDateTime timestamp, Integer status, String error, String message, String path) {
}
