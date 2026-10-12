package com.fatec.candidatura.controller.adapter;

import com.fatec.candidatura.controller.dto.response.CandidaturaResponse;
import com.fatec.candidatura.controller.dto.response.RelatorioResponse;
import com.fatec.candidatura.entity.Candidatura;
import com.fatec.candidatura.entity.Relatorio;

/** Converte as classes do domain no JSON da API (dto) */
public class CandidaturaControllerAdapter {
    private CandidaturaControllerAdapter() {
    }

    public static CandidaturaResponse castResponse(Candidatura c) {
        return new CandidaturaResponse(
                c.id(),
                c.vagaId(),
                c.vagaTitulo(),
                c.status().name(),
                c.motivo(),
                c.valor(),
                c.vagaInicioEm(),
                c.criadaEm(),
                c.atualizadaEm()
        );
    }

    public static RelatorioResponse castResponse(Relatorio r) {
        return new RelatorioResponse(
                r.totalCandidaturas(),
                r.confirmadas(),
                r.recusadas(),
                r.emAnalise(),
                r.receitaTotal(),
                r.porVaga().stream()
                        .map(v -> new RelatorioResponse.PorVaga(v.vagaId(), v.titulo(), v.inicioEm(), v.valor(),
                                v.candidaturas(), v.confirmadas(), v.recusadas(), v.emAnalise(), v.receita()))
                        .toList(),
                r.porMes().stream()
                        .map(m -> new RelatorioResponse.PorMes(m.mes(), m.vagas(), m.confirmadas(), m.receita()))
                        .toList()
        );
    }
}
