package com.fatec.candidatura.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Este serviço PUBLICA em candidatura-criada e CONSOME candidatura-resultado.
 */
@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue candidaturaCriadaQueue(@Value("${event.candidatura-criada.queue}") String queue) {
        return new Queue(queue, true);
    }

    @Bean
    public DirectExchange candidaturaCriadaExchange(@Value("${event.candidatura-criada.exchange}") String exchange) {
        return new DirectExchange(exchange, true, false);
    }

    @Bean
    public Binding candidaturaCriadaBinding(@Qualifier("candidaturaCriadaQueue") Queue queue,
                                            @Qualifier("candidaturaCriadaExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queue.getName());
    }

    @Bean
    public Queue candidaturaResultadoQueue(@Value("${event.candidatura-resultado.queue}") String queue) {
        return new Queue(queue, true);
    }

    @Bean
    public DirectExchange candidaturaResultadoExchange(@Value("${event.candidatura-resultado.exchange}") String exchange) {
        return new DirectExchange(exchange, true, false);
    }

    @Bean
    public Binding candidaturaResultadoBinding(@Qualifier("candidaturaResultadoQueue") Queue queue,
                                               @Qualifier("candidaturaResultadoExchange") DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queue.getName());
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jsonMessageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter);
        return rabbitTemplate;
    }
}
