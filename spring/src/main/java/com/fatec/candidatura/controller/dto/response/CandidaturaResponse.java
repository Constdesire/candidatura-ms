package com.fatec.candidatura.controller.dto.response;

import java.math.BigDecimal;

public record CandidaturaResponse(
        String id,
        Long vagaId,
        String vagaTitulo,
        String status,
        String motivo,
        BigDecimal valor,
        Long vagaInicioEm,
        long criadaEm,
        long atualizadaEm) {
}
