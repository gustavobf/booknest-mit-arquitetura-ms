package br.edu.infnet.booknest.emprestimo.messaging;

import org.slf4j.*;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.stereotype.*;

@Component
@ConditionalOnProperty(name = "app.rabbitmq.consumidor.habilitado", havingValue = "true", matchIfMissing = true)
public class NotificacaoEmprestimoConsumer {

    private static final Logger log = LoggerFactory.getLogger(NotificacaoEmprestimoConsumer.class);

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NOTIFICACAO)
    public void receber (NotificacaoEmprestimoMessage mensagem) {
        switch (mensagem.tipo()) {
            case "EMPRESTIMO_REALIZADO" -> log.info(
                    "[NOTIFICACAO] Empréstimo {} realizado para usuário {} (exemplar {}). Devolução prevista para {}. Enviando aviso ao usuário...",
                    mensagem.emprestimoId(), mensagem.usuarioId(), mensagem.exemplarId(), mensagem.dataReferencia());
            case "DEVOLUCAO_REGISTRADA" -> log.info(
                    "[NOTIFICACAO] Devolução do empréstimo {} registrada em {} pelo usuário {}. Atualizando histórico...",
                    mensagem.emprestimoId(), mensagem.dataReferencia(), mensagem.usuarioId());
            default -> log.info("[NOTIFICACAO] Mensagem recebida: {}", mensagem);
        }
    }
}
