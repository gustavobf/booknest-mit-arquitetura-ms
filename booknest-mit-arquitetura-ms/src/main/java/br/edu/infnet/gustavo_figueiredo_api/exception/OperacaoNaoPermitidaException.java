package br.edu.infnet.gustavo_figueiredo_api.exception;

public class OperacaoNaoPermitidaException extends RuntimeException {
    public OperacaoNaoPermitidaException (String message) {
        super(message);
    }
}
