package br.edu.ifsp.associacaogaia.exception;

/**
 * Lançada quando o administrador tenta remover uma inscrição da newsletter
 * que não existe. Tratada em {@link GlobalExceptionHandler} com HTTP 404.
 */
public class InscricaoNaoEncontradaException extends RuntimeException {

    public InscricaoNaoEncontradaException() {
        super("Inscrição não encontrada.");
    }
}
