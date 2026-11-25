package br.edu.ifsp.associacaogaia.exception;

/**
 * Lançada quando o artesão tenta publicar um trabalho, mas já atingiu
 * o limite máximo de trabalhos permitido pela associação.
 * Tratada em {@link GlobalExceptionHandler} com HTTP 409 (Conflict).
 */
public class LimiteTrabalhosException extends RuntimeException {

    public LimiteTrabalhosException(int limite) {
        super("Você atingiu o limite de " + limite + " trabalhos. Exclua um trabalho para publicar outro.");
    }
}
