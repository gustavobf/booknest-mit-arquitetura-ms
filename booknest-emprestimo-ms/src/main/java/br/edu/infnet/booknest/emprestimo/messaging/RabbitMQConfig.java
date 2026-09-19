package br.edu.infnet.booknest.emprestimo.messaging;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.*;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "booknest.emprestimo.exchange";
    public static final String QUEUE_NOTIFICACAO = "booknest.emprestimo.notificacao.queue";
    public static final String ROUTING_KEY_NOTIFICACAO = "emprestimo.notificacao";

    @Bean
    public DirectExchange emprestimoExchange () {
        return new DirectExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue notificacaoQueue () {
        return new Queue(QUEUE_NOTIFICACAO, true);
    }

    @Bean
    public Binding notificacaoBinding (Queue notificacaoQueue, DirectExchange emprestimoExchange) {
        return BindingBuilder.bind(notificacaoQueue).to(emprestimoExchange).with(ROUTING_KEY_NOTIFICACAO);
    }

    @Bean
    public MessageConverter jsonMessageConverter () {
        return new Jackson2JsonMessageConverter();
    }
}
