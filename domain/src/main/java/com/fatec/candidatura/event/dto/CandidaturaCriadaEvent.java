package com.fatec.candidatura.event.dto;

/** Evento publicado quando alguém se candidata (consumido pelo vagas-ms). */
public record CandidaturaCriadaEvent(String candidaturaId, Long vagaId, String usuario) {
}
