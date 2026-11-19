package com.fatec.candidatura;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/** Recebe as candidaturas, guarda o status de cada uma e gera o relatório do ADMIN. */
@SpringBootApplication
public class CandidaturaApplication {
    public static void main(String[] args) {
        SpringApplication.run(CandidaturaApplication.class, args);
    }
}
