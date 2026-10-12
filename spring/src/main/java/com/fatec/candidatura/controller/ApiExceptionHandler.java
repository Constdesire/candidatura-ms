package com.fatec.candidatura.controller;

import com.fatec.candidatura.controller.dto.response.ErroResponse;
import com.fatec.candidatura.security.AcessoNegadoException;
import com.fatec.candidatura.security.NaoAutenticadoException;
import com.fatec.candidatura.service.exception.RegraNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(RegraNegocioException.class)
    public ErroResponse regraNegocio(RegraNegocioException ex) {
        return new ErroResponse(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AcessoNegadoException.class)
    public ErroResponse acessoNegado(AcessoNegadoException ex) {
        return new ErroResponse(ex.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(NaoAutenticadoException.class)
    public ErroResponse naoAutenticado(NaoAutenticadoException ex) {
        return new ErroResponse(ex.getMessage());
    }
}
