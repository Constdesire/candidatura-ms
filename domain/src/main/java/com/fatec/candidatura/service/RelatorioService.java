package com.fatec.candidatura.service;

import com.fatec.candidatura.entity.Candidatura;
import com.fatec.candidatura.entity.Relatorio;
import com.fatec.candidatura.entity.Relatorio.PorMes;
import com.fatec.candidatura.entity.Relatorio.PorVaga;
import com.fatec.candidatura.entity.StatusCandidatura;
import com.fatec.candidatura.repository.CandidaturaRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

/** Relatório de "vendas das vagas": quanto cada vaga e cada mês renderam. */
public class RelatorioService {

    private static final ZoneId FUSO = ZoneId.of("America/Sao_Paulo");

    private final CandidaturaRepository repository;

    public RelatorioService(CandidaturaRepository repository) {
        this.repository = repository;
    }

    public Relatorio gerar() {
        List<Candidatura> todas = repository.findAll();

        int confirmadas = contar(todas, StatusCandidatura.CONFIRMADA);
        int recusadas = contar(todas, StatusCandidatura.RECUSADA);
        int emAnalise = contar(todas, StatusCandidatura.PENDENTE);

        List<PorVaga> porVaga = todas.stream()
                .collect(Collectors.groupingBy(Candidatura::vagaId))
                .entrySet().stream()
                .map(en -> resumirVaga(en.getKey(), en.getValue()))
                .sorted(Comparator.comparing((PorVaga v) -> v.receita()).reversed()
                        .thenComparing(PorVaga::vagaId))
                .toList();

        // por mês da aula: só entram candidaturas já respondidas (têm a data da vaga)
        Map<YearMonth, List<Candidatura>> deCadaMes = todas.stream()
                .filter(c -> c.vagaInicioEm() != null)
                .collect(Collectors.groupingBy(
                        c -> YearMonth.from(Instant.ofEpochMilli(c.vagaInicioEm()).atZone(FUSO)),
                        TreeMap::new, Collectors.toList()));
        List<PorMes> porMes = deCadaMes.entrySet().stream()
                .map(en -> new PorMes(en.getKey().toString(),
                        (int) en.getValue().stream().map(Candidatura::vagaId).distinct().count(),
                        contar(en.getValue(), StatusCandidatura.CONFIRMADA),
                        receita(en.getValue())))
                .toList();

        return new Relatorio(todas.size(), confirmadas, recusadas, emAnalise, receita(todas), porVaga, porMes);
    }

    private PorVaga resumirVaga(Long vagaId, List<Candidatura> candidaturas) {
        Candidatura referencia = candidaturas.stream().filter(c -> c.vagaTitulo() != null).findFirst().orElse(null);
        String titulo = referencia != null ? referencia.vagaTitulo() : "Vaga #" + vagaId;
        Long inicioEm = candidaturas.stream().map(Candidatura::vagaInicioEm).filter(Objects::nonNull).findFirst().orElse(null);
        BigDecimal valor = candidaturas.stream().map(Candidatura::valor).filter(Objects::nonNull).findFirst().orElse(null);
        return new PorVaga(vagaId, titulo, inicioEm, valor, candidaturas.size(),
                contar(candidaturas, StatusCandidatura.CONFIRMADA),
                contar(candidaturas, StatusCandidatura.RECUSADA),
                contar(candidaturas, StatusCandidatura.PENDENTE),
                receita(candidaturas));
    }

    private int contar(List<Candidatura> lista, StatusCandidatura status) {
        return (int) lista.stream().filter(c -> c.status() == status).count();
    }

    // receita = soma do valor cobrado de quem CONFIRMOU a vaga
    private BigDecimal receita(List<Candidatura> lista) {
        return lista.stream()
                .filter(c -> c.status() == StatusCandidatura.CONFIRMADA && c.valor() != null)
                .map(Candidatura::valor)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
