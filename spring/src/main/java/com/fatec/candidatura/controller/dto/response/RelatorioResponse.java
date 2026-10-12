package com.fatec.candidatura.controller.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record RelatorioResponse(
        int totalCandidaturas,
        int confirmadas,
        int recusadas,
        int emAnalise,
        BigDecimal receitaTotal,
        List<PorVaga> porVaga,
        List<PorMes> porMes) {

    public record PorVaga(Long vagaId, String titulo, Long inicioEm, BigDecimal valor, int candidaturas,
                          int confirmadas, int recusadas, int emAnalise, BigDecimal receita) {
    }

    public record PorMes(String mes, int vagas, int confirmadas, BigDecimal receita) {
    }
}
