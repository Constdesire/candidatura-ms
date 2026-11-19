package com.fatec.candidatura.event.dto;

import java.math.BigDecimal;

/** Resposta do vagas-ms: CONFIRMADA (vaga alocada, valor cobrado) ou RECUSADA. */
public record CandidaturaResultadoEvent(
        String candidaturaId,
        Long vagaId,
        String usuario,
        String status,
        String motivo,
        String vagaTitulo,
        BigDecimal valor,
        Long vagaInicioEm) {
}
