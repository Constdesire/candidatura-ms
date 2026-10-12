package com.fatec.candidatura.security;

public class NaoAutenticadoException extends RuntimeException {
    public NaoAutenticadoException(String mensagem) {
        super(mensagem);
    }
}
