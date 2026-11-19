package com.fatec.candidatura.config;

import com.fatec.candidatura.event.CandidaturaPublicador;
import com.fatec.candidatura.repository.CandidaturaRepository;
import com.fatec.candidatura.service.CandidaturaService;
import com.fatec.candidatura.service.RelatorioService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Cria os services (classes do domain) como beans do Spring, igual ao LoginServiceConfig do login. */
@Configuration
public class CandidaturaServiceConfig {

    @Bean
    public CandidaturaService candidaturaService(CandidaturaRepository repository, CandidaturaPublicador publicador) {
        return new CandidaturaService(repository, publicador);
    }

    @Bean
    public RelatorioService relatorioService(CandidaturaRepository repository) {
        return new RelatorioService(repository);
    }
}
