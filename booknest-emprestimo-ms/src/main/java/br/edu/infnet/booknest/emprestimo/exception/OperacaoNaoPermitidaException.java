package br.edu.infnet.booknest.emprestimo.exception;

public class OperacaoNaoPermitidaException extends RuntimeException {

    public OperacaoNaoPermitidaException (String mensagem) {
        super(mensagem);
    }
}
