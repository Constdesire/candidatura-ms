package com.fatec.candidatura.controller;

import com.fatec.candidatura.controller.adapter.CandidaturaControllerAdapter;
import com.fatec.candidatura.controller.dto.response.RelatorioResponse;
import com.fatec.candidatura.security.JwtLeitor;
import com.fatec.candidatura.service.RelatorioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Relatório de vendas das vagas: SÓ O ADMIN.
 * Barreira 1: o gateway só expõe esta rota em /fatec/admin/relatorio (exige ROLE_ADMIN).
 * Barreira 2: o serviço confere o perfil no token (exigirAdmin).
 */
@RestController
@RequestMapping("/relatorio")
public class RelatorioController {

    private final RelatorioService service;
    private final JwtLeitor jwtLeitor;

    public RelatorioController(RelatorioService service, JwtLeitor jwtLeitor) {
        this.service = service;
        this.jwtLeitor = jwtLeitor;
    }

    @GetMapping
    public RelatorioResponse relatorio(@RequestHeader(value = "Authorization", required = false) String authorization) {
        jwtLeitor.exigirAdmin(authorization);
        return CandidaturaControllerAdapter.castResponse(service.gerar());
    }
}
