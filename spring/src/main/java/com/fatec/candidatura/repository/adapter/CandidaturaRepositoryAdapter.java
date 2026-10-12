package com.fatec.candidatura.repository.adapter;

import com.fatec.candidatura.entity.Candidatura;
import com.fatec.candidatura.repository.orm.CandidaturaOrmMongo;

import java.util.UUID;

public class CandidaturaRepositoryAdapter {

    private CandidaturaRepositoryAdapter() {
    }

    public static Candidatura castOrm(CandidaturaOrmMongo orm) {
        return new Candidatura(
                orm.id(),
                orm.usuario(),
                orm.vagaId(),
                orm.status(),
                orm.motivo(),
                orm.vagaTitulo(),
                orm.valor(),
                orm.vagaInicioEm(),
                orm.criadaEm(),
                orm.atualizadaEm()
        );
    }

    /** Candidatura nova ainda não tem id geramos um UUID */
    public static CandidaturaOrmMongo castEntity(Candidatura entity) {
        return new CandidaturaOrmMongo(
                entity.id() != null ? entity.id() : UUID.randomUUID().toString(),
                entity.usuario(),
                entity.vagaId(),
                entity.status(),
                entity.motivo(),
                entity.vagaTitulo(),
                entity.valor(),
                entity.vagaInicioEm(),
                entity.criadaEm(),
                entity.atualizadaEm()
        );
    }
}
