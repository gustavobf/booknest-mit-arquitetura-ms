package br.edu.infnet.booknest.emprestimo.exception;

public class RegistroNaoEncontradoException extends RuntimeException {

    public RegistroNaoEncontradoException (String mensagem) {
        super(mensagem);
    }
}
