package br.edu.infnet.booknest.emprestimo.exception;

public class DadosInvalidosException extends RuntimeException {

    public DadosInvalidosException (String mensagem) {
        super(mensagem);
    }
}
