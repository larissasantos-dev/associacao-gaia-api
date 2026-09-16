package br.edu.ifsp.associacaogaia.exception;

public class UsuarioInativoException extends RuntimeException {
    public UsuarioInativoException(String message) {
        super(message);
    }
}