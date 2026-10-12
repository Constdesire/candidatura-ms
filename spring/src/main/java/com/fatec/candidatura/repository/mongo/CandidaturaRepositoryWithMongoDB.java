package com.fatec.candidatura.repository.mongo;

import com.fatec.candidatura.repository.orm.CandidaturaOrmMongo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CandidaturaRepositoryWithMongoDB extends MongoRepository<CandidaturaOrmMongo, String> {
    List<CandidaturaOrmMongo> findByUsuarioOrderByCriadaEmDesc(String usuario);

    boolean existsByUsuarioAndVagaId(String usuario, Long vagaId);
}
