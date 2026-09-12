package br.edu.ifsp.associacaogaia.exception;

public class CredenciaisInvalidasException extends RuntimeException{

    public CredenciaisInvalidasException(String mensagem){
        super(mensagem);
    }
}