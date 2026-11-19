package com.fatec.candidatura.controller;

import com.fatec.candidatura.controller.adapter.CandidaturaControllerAdapter;
import com.fatec.candidatura.controller.dto.request.CandidaturaRequest;
import com.fatec.candidatura.controller.dto.response.CandidaturaResponse;
import com.fatec.candidatura.security.JwtLeitor;
import com.fatec.candidatura.service.CandidaturaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/candidaturas")
public class CandidaturaController {

    private final CandidaturaService service;
    private final JwtLeitor jwtLeitor;

    public CandidaturaController(CandidaturaService service, JwtLeitor jwtLeitor) {
        this.service = service;
        this.jwtLeitor = jwtLeitor;
    }

    // 202 Accepted: o pedido foi aceito, mas ainda será verificado (assíncrono)
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public CandidaturaResponse candidatar(@RequestBody CandidaturaRequest request,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        String usuario = jwtLeitor.getUsername(authorization);
        return CandidaturaControllerAdapter.castResponse(service.candidatar(usuario, request.vagaId()));
    }

    // tela "status da minha vaga"
    @GetMapping("/minhas")
    public List<CandidaturaResponse> minhas(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        return service.minhas(jwtLeitor.getUsername(authorization)).stream()
                .map(CandidaturaControllerAdapter::castResponse).toList();
    }
}
