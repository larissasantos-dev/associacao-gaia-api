package br.edu.ifsp.associacaogaia.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailJaCadastradoException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String tratarEmailJaCadastrado(EmailJaCadastradoException exception){
        return exception.getMessage();
    }

    @ExceptionHandler(CredenciaisInvalidasException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String tratarCredenciaisInvalidas(CredenciaisInvalidasException exception){
        return exception.getMessage();
    }

    @ExceptionHandler(UsuarioInativoException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String tratarUsuarioInativo(UsuarioInativoException exception){
        return exception.getMessage();
    }

    @ExceptionHandler(ArtesaoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String tratarArtesaoNaoEncontrado(ArtesaoNaoEncontradoException exception){
        return exception.getMessage();
    }

    @ExceptionHandler(TrabalhoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String tratarTrabalhoNaoEncontrado(TrabalhoNaoEncontradoException exception){
        return exception.getMessage();
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String tratarUsuarioNaoEncontrado(UsuarioNaoEncontradoException exception){
        return exception.getMessage();
    }
}