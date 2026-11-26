package br.edu.ifsp.associacaogaia.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;

import java.math.BigDecimal;

/**
 * Corpo da requisição PATCH /api/trabalhos/{id}/preco.
 * Preço nulo significa "sob consulta" (remove o preço).
 */
public class TrabalhoPrecoDTO {

    @DecimalMin(value = "0.00", message = "O preço não pode ser negativo.")
    @Digits(integer = 8, fraction = 2, message = "O preço deve ter no máximo 8 dígitos e 2 casas decimais.")
    private BigDecimal preco;

    public TrabalhoPrecoDTO() {
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
}
