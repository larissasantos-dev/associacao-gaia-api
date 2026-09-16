package br.edu.ifsp.associacaogaia.exception;

/**
 * Lançada quando não existe usuário com o ID informado.
 * Resulta em HTTP 404 Not Found (tratado no GlobalExceptionHandler).
 */
public class UsuarioNaoEncontradoException extends RuntimeException {

    public UsuarioNaoEncontradoException() {
        super("Usuário não encontrado.");
    }

    public UsuarioNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
