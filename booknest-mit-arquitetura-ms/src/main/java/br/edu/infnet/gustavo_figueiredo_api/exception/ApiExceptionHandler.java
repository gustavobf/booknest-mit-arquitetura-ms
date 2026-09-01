package br.edu.infnet.gustavo_figueiredo_api.exception;

import jakarta.servlet.http.*;
import jakarta.validation.*;
import org.springframework.http.*;
import org.springframework.http.converter.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.*;
import java.util.stream.*;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(RegistroNaoEncontradoException.class)
    public ResponseEntity<ErroResposta> handleNotFound (RegistroNaoEncontradoException ex, HttpServletRequest request) {
        return construirResposta(HttpStatus.NOT_FOUND, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler({DadosInvalidosException.class, MethodArgumentTypeMismatchException.class,
            HttpMessageNotReadableException.class, MissingPathVariableException.class})
    public ResponseEntity<ErroResposta> handleBadRequest (Exception ex, HttpServletRequest request) {
        return construirResposta(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(OperacaoNaoPermitidaException.class)
    public ResponseEntity<ErroResposta> handleBusinessRule (OperacaoNaoPermitidaException ex,
                                                            HttpServletRequest request) {
        return construirResposta(HttpStatus.BAD_REQUEST, ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResposta> handleValidation (MethodArgumentNotValidException ex,
                                                          HttpServletRequest request) {
        String mensagem = ex.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage()).collect(Collectors.joining("; "));
        return construirResposta(HttpStatus.BAD_REQUEST, mensagem, request.getRequestURI());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroResposta> handleConstraintViolation (ConstraintViolationException ex,
                                                                  HttpServletRequest request) {
        String mensagem = ex.getConstraintViolations().stream().map(violacao -> violacao.getPropertyPath() + ": " + violacao.getMessage()).collect(Collectors.joining("; "));
        return construirResposta(HttpStatus.BAD_REQUEST, mensagem, request.getRequestURI());
    }

    private ResponseEntity<ErroResposta> construirResposta (HttpStatus status, String mensagem, String path) {
        ErroResposta erro = new ErroResposta(LocalDateTime.now(), status.value(), status.getReasonPhrase(), mensagem,
                path);
        return ResponseEntity.status(status).body(erro);
    }
}
