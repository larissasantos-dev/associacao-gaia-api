package br.edu.ifsp.associacaogaia.exception;

public class UsuarioNaoEncontradoException extends RuntimeException{
    public UsuarioNaoEncontradoException(String message){
        super(message);
    }
}