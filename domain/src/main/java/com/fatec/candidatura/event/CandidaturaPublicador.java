package com.fatec.candidatura.event;

import com.fatec.candidatura.event.dto.CandidaturaCriadaEvent;

/** Porta de saída: o domain pede "publique"; o RabbitMQ fica no módulo spring. */
public interface CandidaturaPublicador {
    void enviar(CandidaturaCriadaEvent evento);
}
