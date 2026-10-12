package com.fatec.candidatura.event.dto;

/** Evento publicado quando alguém se candidata . */
public record CandidaturaCriadaEvent(String candidaturaId, Long vagaId, String usuario) {
}
