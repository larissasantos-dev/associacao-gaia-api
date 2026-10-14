package br.edu.ifsp.associacaogaia.exception;

public class EventoNaoEncontradoException extends RuntimeException {

    public EventoNaoEncontradoException() {
        super("Evento não encontrado.");
    }
}