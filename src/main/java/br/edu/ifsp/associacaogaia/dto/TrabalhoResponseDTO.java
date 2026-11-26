package br.edu.ifsp.associacaogaia.dto;

import br.edu.ifsp.associacaogaia.model.Trabalho;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de saída para os dados de um Trabalho (peça do portfólio do artesão).
 *
 * Usado nas respostas de POST, GET (listagem) e PUT de /api/trabalhos, no lugar
 * da entidade Trabalho diretamente. Isso evita expor o relacionamento
 * Trabalho -> Artesao -> List<Trabalho> na serialização JSON, que causaria
 * recursão infinita (Jackson tentaria serializar o artesão dono do trabalho,
 * que por sua vez lista todos os seus trabalhos, e assim por diante).
 */
public class TrabalhoResponseDTO {

    private Long idTrabalho;
    private String imagemUrl;
    private String legenda;
    private String descricao;
    private String categoria;
    private BigDecimal preco;
    private LocalDateTime precoAtualizadoEm;
    private LocalDateTime dataPublicacao;

    public TrabalhoResponseDTO(Trabalho trabalho) {
        this.idTrabalho = trabalho.getIdTrabalho();
        this.imagemUrl = trabalho.getImagemUrl();
        this.legenda = trabalho.getLegenda();
        this.descricao = trabalho.getDescricao();
        this.categoria = trabalho.getCategoria();
        this.preco = trabalho.getPreco();
        this.precoAtualizadoEm = trabalho.getPrecoAtualizadoEm();
        this.dataPublicacao = trabalho.getDataPublicacao();
    }

    public Long getIdTrabalho() {
        return idTrabalho;
    }

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

    public BigDecimal getPreco() {
        return preco;
    }

    public LocalDateTime getPrecoAtualizadoEm() {
        return precoAtualizadoEm;
    }

    public LocalDateTime getDataPublicacao() {
        return dataPublicacao;
    }
}
