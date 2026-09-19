package br.edu.infnet.booknest.emprestimo.messaging;

import org.slf4j.*;
import org.springframework.amqp.rabbit.core.*;
import org.springframework.stereotype.*;

@Component
public class NotificacaoEmprestimoProducer {

    private static final Logger log = LoggerFactory.getLogger(NotificacaoEmprestimoProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public NotificacaoEmprestimoProducer (RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicar (NotificacaoEmprestimoMessage mensagem) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY_NOTIFICACAO, mensagem);
            log.info("Mensagem publicada na fila '{}': {}", RabbitMQConfig.QUEUE_NOTIFICACAO, mensagem);
        } catch (Exception ex) {
            log.warn("Falha ao publicar mensagem de notificação: {}", ex.getMessage());
        }
    }
}
