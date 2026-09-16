package br.edu.ifsp.associacaogaia.exception;

/**
 * Lançada quando um artesão não é encontrado para o usuário autenticado.
 * Resulta em HTTP 404 Not Found (tratado no GlobalExceptionHandler).
 */
public class ArtesaoNaoEncontradoException extends RuntimeException {

    public ArtesaoNaoEncontradoException() {
        super("Nenhum perfil de artesão encontrado para este usuário.");
    }

    public ArtesaoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
