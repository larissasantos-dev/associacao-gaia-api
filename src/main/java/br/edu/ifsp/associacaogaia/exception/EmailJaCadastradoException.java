package br.edu.ifsp.associacaogaia.exception;

public class EmailJaCadastradoException extends RuntimeException{

    public EmailJaCadastradoException(String mensagem){
        super(mensagem);
    }
}