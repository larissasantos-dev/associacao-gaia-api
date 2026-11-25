package br.edu.ifsp.associacaogaia.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

/**
 * Dados enviados (multipart/form-data) pelo artesão ao criar ou editar um
 * trabalho com foto. Na criação a imagem é obrigatória; na edição ela é
 * opcional (se não vier, a foto atual é mantida).
 */
public class TrabalhoUploadDTO {

    private MultipartFile imagem;

    @NotBlank(message = "A legenda é obrigatória.")
    @Size(max = 150, message = "A legenda deve ter no máximo 150 caracteres.")
    private String legenda;

    private String descricao;

    @Size(max = 100, message = "A categoria deve ter no máximo 100 caracteres.")
    private String categoria;

    @DecimalMin(value = "0.00", message = "O preço não pode ser negativo.")
    @Digits(integer = 8, fraction = 2, message = "O preço deve ter no máximo 8 dígitos e 2 casas decimais.")
    private BigDecimal preco;

    public TrabalhoUploadDTO() {
    }

    public MultipartFile getImagem() {
        return imagem;
    }

    public String getLegenda() {
        return legenda;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setImagem(MultipartFile imagem) {
        this.imagem = imagem;
    }

    public void setLegenda(String legenda) {
        this.legenda = legenda;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }
}
