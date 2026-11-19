package com.fatec.candidatura.service;

import com.fatec.candidatura.entity.Candidatura;
import com.fatec.candidatura.entity.StatusCandidatura;
import com.fatec.candidatura.event.CandidaturaPublicador;
import com.fatec.candidatura.event.dto.CandidaturaCriadaEvent;
import com.fatec.candidatura.event.dto.CandidaturaResultadoEvent;
import com.fatec.candidatura.repository.CandidaturaRepository;
import com.fatec.candidatura.service.exception.CandidaturaDuplicadaException;
import com.fatec.candidatura.service.exception.RegraNegocioException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Regras das candidaturas. Classe comum (sem @Service), como o LoginService: o módulo spring a cria em
 * config/CandidaturaServiceConfig.
 */
public class CandidaturaService {

    private static final Logger log = LoggerFactory.getLogger(CandidaturaService.class);

    private final CandidaturaRepository repository;
    private final CandidaturaPublicador publicador;

    public CandidaturaService(CandidaturaRepository repository, CandidaturaPublicador publicador) {
        this.repository = repository;
        this.publicador = publicador;
    }

    /**
     * "Aguarde, vou verificar": grava a candidatura como PENDENTE, publica o evento e responde na hora.
     * Quem decide se há vaga é o vagas-ms; o resultado volta por outro evento (aplicarResultado).
     */
    public Candidatura candidatar(String usuario, Long vagaId) {
        if (vagaId == null) {
            throw new RegraNegocioException("Informe a vaga");
        }
        if (repository.existsByUsuarioAndVagaId(usuario, vagaId)) {
            throw new RegraNegocioException("Você já se candidatou a esta vaga");
        }
        Candidatura candidatura;
        try {
            candidatura = repository.save(Candidatura.nova(usuario, vagaId));
        } catch (CandidaturaDuplicadaException ex) {
            // dois pedidos iguais ao mesmo tempo: o índice único do banco barra o segundo
            throw new RegraNegocioException("Você já se candidatou a esta vaga");
        }
        log.info("[CANDIDATURA] {} se candidatou à vaga #{} -> PENDENTE gravada no MongoDB (candidatura_db)", usuario, vagaId);

        try {
            publicador.enviar(new CandidaturaCriadaEvent(candidatura.id(), vagaId, usuario));
        } catch (RuntimeException ex) {
            repository.delete(candidatura); // não deixa candidatura "presa" em análise
            log.error("[CANDIDATURA] falha ao publicar no RabbitMQ; candidatura desfeita", ex);
            throw new RegraNegocioException("Não foi possível enviar sua candidatura agora. Tente novamente.");
        }
        return candidatura;
    }

    /** Chegou a resposta do vagas-ms: atualiza o status (e o valor cobrado) da candidatura. */
    public void aplicarResultado(CandidaturaResultadoEvent e) {
        repository.findById(e.candidaturaId()).ifPresentOrElse(c -> {
            if (c.status() != StatusCandidatura.PENDENTE) {
                log.warn("[CANDIDATURA] resultado repetido para {} ignorado (já está {})", c.id(), c.status());
                return;
            }
            Candidatura atualizada = repository.save(c.comResultado(e));
            log.info("[CANDIDATURA] {} agora está {} (vaga #{}){}", atualizada.usuario(), atualizada.status(),
                    atualizada.vagaId(),
                    atualizada.valor() != null ? " - valor cobrado R$ " + atualizada.valor() : "");
        }, () -> log.warn("[CANDIDATURA] resultado para candidatura desconhecida: {}", e.candidaturaId()));
    }

    public List<Candidatura> minhas(String usuario) {
        return repository.findByUsuarioOrderByCriadaEmDesc(usuario);
    }
}
