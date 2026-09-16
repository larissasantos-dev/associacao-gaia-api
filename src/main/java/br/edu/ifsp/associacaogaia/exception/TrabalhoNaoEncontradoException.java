package br.edu.ifsp.associacaogaia.exception;

/**
 * Lançada quando um trabalho com o ID informado não existe no banco de dados.
 * Resulta em HTTP 404 Not Found (tratado no GlobalExceptionHandler).
 */
public class TrabalhoNaoEncontradoException extends RuntimeException {

    public TrabalhoNaoEncontradoException() {
        super("Trabalho não encontrado.");
    }

    public TrabalhoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
