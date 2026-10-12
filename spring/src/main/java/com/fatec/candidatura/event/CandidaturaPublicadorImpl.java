package com.fatec.candidatura.event;

import com.fatec.candidatura.event.dto.CandidaturaCriadaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CandidaturaPublicadorImpl implements CandidaturaPublicador {

    private static final Logger log = LoggerFactory.getLogger(CandidaturaPublicadorImpl.class);

    public final String queue;
    public final String exchange;
    public final RabbitTemplate rabbitTemplate;

    public CandidaturaPublicadorImpl(
            RabbitTemplate rabbitTemplate,
            @Value("${event.candidatura-criada.queue}") String queue,
            @Value("${event.candidatura-criada.exchange}") String exchange) {
        this.queue = queue;
        this.exchange = exchange;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void enviar(CandidaturaCriadaEvent evento) {
        try {
            rabbitTemplate.convertAndSend(exchange, queue, evento);
            log.info("[RABBIT >>] PUBLICADO exchange={} fila={} candidatura de {} para a vaga #{}", exchange, queue,
                    evento.usuario(), evento.vagaId());
        } catch (Exception ex) {
            throw new AmqpRejectAndDontRequeueException(ex);
        }
    }
}
