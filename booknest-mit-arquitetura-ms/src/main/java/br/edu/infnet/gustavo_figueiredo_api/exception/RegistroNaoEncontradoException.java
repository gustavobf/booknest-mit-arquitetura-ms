package br.edu.infnet.gustavo_figueiredo_api.exception;

public class RegistroNaoEncontradoException extends RuntimeException {
    public RegistroNaoEncontradoException (String message) {
        super(message);
    }
}
