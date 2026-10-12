package com.fatec.candidatura.event;

import com.fatec.candidatura.event.dto.CandidaturaResultadoEvent;
import com.fatec.candidatura.service.CandidaturaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class ResultadoListener {

    private static final Logger log = LoggerFactory.getLogger(ResultadoListener.class);

    private final CandidaturaService service;

    public ResultadoListener(CandidaturaService service) {
        this.service = service;
    }

    @RabbitListener(queues = "${event.candidatura-resultado.queue}")
    public void receber(CandidaturaResultadoEvent evento) {
        log.info("[RABBIT <<] RECEBIDO resultado de {}: {}", evento.usuario(), evento.status());
        service.aplicarResultado(evento);
    }
}
