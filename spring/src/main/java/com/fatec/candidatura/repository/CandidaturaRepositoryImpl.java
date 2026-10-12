package com.fatec.candidatura.repository;

import com.fatec.candidatura.entity.Candidatura;
import com.fatec.candidatura.repository.adapter.CandidaturaRepositoryAdapter;
import com.fatec.candidatura.repository.mongo.CandidaturaRepositoryWithMongoDB;
import com.fatec.candidatura.service.exception.CandidaturaDuplicadaException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/** Implementa a porta do domain usando MongoDB . */
@Repository
public class CandidaturaRepositoryImpl implements CandidaturaRepository {

    private final CandidaturaRepositoryWithMongoDB mongo;

    public CandidaturaRepositoryImpl(CandidaturaRepositoryWithMongoDB mongo) {
        this.mongo = mongo;
    }

    @Override
    public Candidatura save(Candidatura candidatura) {
        try {
            return CandidaturaRepositoryAdapter.castOrm(
                    mongo.save(CandidaturaRepositoryAdapter.castEntity(candidatura)));
        } catch (DuplicateKeyException ex) {
            throw new CandidaturaDuplicadaException();
        }
    }

    @Override
    public void delete(Candidatura candidatura) {
        mongo.deleteById(candidatura.id());
    }

    @Override
    public Optional<Candidatura> findById(String id) {
        return mongo.findById(id).map(CandidaturaRepositoryAdapter::castOrm);
    }

    @Override
    public List<Candidatura> findByUsuarioOrderByCriadaEmDesc(String usuario) {
        return mongo.findByUsuarioOrderByCriadaEmDesc(usuario).stream()
                .map(CandidaturaRepositoryAdapter::castOrm).toList();
    }

    @Override
    public boolean existsByUsuarioAndVagaId(String usuario, Long vagaId) {
        return mongo.existsByUsuarioAndVagaId(usuario, vagaId);
    }

    @Override
    public List<Candidatura> findAll() {
        return mongo.findAll().stream().map(CandidaturaRepositoryAdapter::castOrm).toList();
    }
}
