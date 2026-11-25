package br.edu.ifsp.associacaogaia.exception;

/**
 * Lançada quando a imagem enviada é inválida (não foi selecionada ou o
 * formato não é JPG, PNG ou WEBP).
 * Tratada em {@link GlobalExceptionHandler} com HTTP 400 (Bad Request).
 *
 * Estende IllegalArgumentException para continuar compatível com o código
 * que já tratava esse tipo de erro.
 */
public class ImagemInvalidaException extends IllegalArgumentException {

    public ImagemInvalidaException(String mensagem) {
        super(mensagem);
    }
}
