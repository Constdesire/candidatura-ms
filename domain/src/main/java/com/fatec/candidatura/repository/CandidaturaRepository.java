package com.fatec.candidatura.repository;

import com.fatec.candidatura.entity.Candidatura;

import java.util.List;
import java.util.Optional;

/** Porta de saída do domain */
public interface CandidaturaRepository {

    /** Grava gera o id se for nova. Se já existe candidatura da pessoa para a vaga, lança CandidaturaDuplicadaException. */
    Candidatura save(Candidatura candidatura);

    void delete(Candidatura candidatura);

    Optional<Candidatura> findById(String id);

    List<Candidatura> findByUsuarioOrderByCriadaEmDesc(String usuario);

    boolean existsByUsuarioAndVagaId(String usuario, Long vagaId);

    List<Candidatura> findAll();
}
