package br.edu.infnet.booknest.emprestimo.messaging;

import java.io.*;
import java.time.*;

public record NotificacaoEmprestimoMessage(
        String tipo,
        Long emprestimoId,
        Long usuarioId,
        Long exemplarId,
        LocalDate dataReferencia) implements Serializable {

    public static NotificacaoEmprestimoMessage emprestimoRealizado (Long emprestimoId, Long usuarioId,
                                                                      Long exemplarId, LocalDate dataEsperadaDevolucao) {
        return new NotificacaoEmprestimoMessage("EMPRESTIMO_REALIZADO", emprestimoId, usuarioId, exemplarId,
                dataEsperadaDevolucao);
    }

    public static NotificacaoEmprestimoMessage devolucaoRegistrada (Long emprestimoId, Long usuarioId,
                                                                      Long exemplarId, LocalDate dataDevolucao) {
        return new NotificacaoEmprestimoMessage("DEVOLUCAO_REGISTRADA", emprestimoId, usuarioId, exemplarId,
                dataDevolucao);
    }
}
