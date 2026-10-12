package com.fatec.candidatura.repository.orm;

import com.fatec.candidatura.entity.StatusCandidatura;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * Versão do banco da Candidatura candidaturas no MongoDB.
 * O índice único usuario + vagaId garante UMA candidatura por pessoa por vaga,
 * mesmo que dois pedidos iguais cheguem ao mesmo tempo.
 */
@Document(collection = "candidaturas")
@CompoundIndex(name = "usuario_vaga_unico", def = "{'usuario': 1, 'vagaId': 1}", unique = true)
public record CandidaturaOrmMongo(
        @Id
        String id,
        String usuario,
        Long vagaId,
        StatusCandidatura status,
        String motivo,
        String vagaTitulo,
        BigDecimal valor,
        Long vagaInicioEm,
        long criadaEm,
        long atualizadaEm
) {
}
