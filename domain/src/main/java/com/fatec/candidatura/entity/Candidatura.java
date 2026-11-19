package com.fatec.candidatura.entity;

import com.fatec.candidatura.event.dto.CandidaturaResultadoEvent;

import java.math.BigDecimal;

/**
 * Uma candidatura. Domain puro (sem MongoDB): a versão com @Document fica no módulo spring.
 * Cada pessoa só pode ter UMA candidatura por vaga (garantido por regra no service e por índice único no banco).
 */
public record Candidatura(
        String id,
        String usuario,
        Long vagaId,
        StatusCandidatura status,
        String motivo,
        // preenchidos quando o resultado chega (vêm da API de vagas)
        String vagaTitulo,
        BigDecimal valor,
        Long vagaInicioEm,
        long criadaEm,
        long atualizadaEm
) {

    /** Candidatura recém-criada: PENDENTE, ainda sem id (quem grava gera o id). */
    public static Candidatura nova(String usuario, Long vagaId) {
        long agora = System.currentTimeMillis();
        return new Candidatura(null, usuario, vagaId, StatusCandidatura.PENDENTE, null, null, null, null, agora, agora);
    }

    /** Aplica a resposta do vagas-ms (record imutável: devolve uma cópia atualizada). */
    public Candidatura comResultado(CandidaturaResultadoEvent e) {
        StatusCandidatura novoStatus = StatusCandidatura.valueOf(e.status());
        return new Candidatura(id, usuario, vagaId, novoStatus, e.motivo(), e.vagaTitulo(),
                // o valor só é cobrado de quem conseguiu a vaga
                novoStatus == StatusCandidatura.CONFIRMADA ? e.valor() : null,
                e.vagaInicioEm(), criadaEm, System.currentTimeMillis());
    }
}
