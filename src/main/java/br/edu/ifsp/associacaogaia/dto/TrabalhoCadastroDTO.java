package br.edu.ifsp.associacaogaia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO que transporta os dados do formulário de cadastro de trabalho.
 *
 * Campos enviados pelo front-end (POST /api/trabalhos):
 *   - imagemUrl  : URL pública da imagem do trabalho (obrigatório)
 *   - legenda    : legenda curta exibida no portfólio   (obrigatório, máx 150 chars)
 *   - descricao  : descrição livre do trabalho          (opcional)
 *   - categoria  : categoria artesanal                  (opcional)
 *
 * Campos NÃO enviados pelo front-end (resolvidos no back-end):
 *   - idArtesao     → extraído do token JWT (segurança)
 *   - dataPublicacao → preenchida automaticamente com LocalDateTime.now()
 */
public class TrabalhoCadastroDTO {

    @NotBlank(message = "A URL da imagem é obrigatória.")
    @Size(max = 255, message = "A URL da imagem deve ter no máximo 255 caracteres.")
    private String imagemUrl;

    @NotBlank(message = "A legenda é obrigatória.")
    @Size(max = 150, message = "A legenda deve ter no máximo 150 caracteres.")
    private String legenda;

    // Opcional — nenhuma anotação de validação obrigatória
    private String descricao;

    // Opcional — nenhuma anotação de validação obrigatória
    @Size(max = 100, message = "A categoria deve ter no máximo 100 caracteres.")
    private String categoria;

    // --- Construtores ---

    /** Construtor padrão exigido pelo Jackson para desserialização do JSON */
    public TrabalhoCadastroDTO() {}

    // --- Getters ---

    public String getImagemUrl() {
        return imagemUrl;
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

    // --- Setters (necessários para o Jackson preencher o objeto) ---

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
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
}
