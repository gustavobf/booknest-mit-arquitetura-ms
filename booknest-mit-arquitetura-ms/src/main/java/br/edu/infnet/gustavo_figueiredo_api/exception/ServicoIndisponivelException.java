package br.edu.infnet.gustavo_figueiredo_api.exception;

public class ServicoIndisponivelException extends RuntimeException {

    public ServicoIndisponivelException (String mensagem) {
        super(mensagem);
    }
}
