package br.edu.ifsp.associacaogaia.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "trabalho")
public class Trabalho {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_trabalho")
    private Long idTrabalho;

    @ManyToOne
    @JoinColumn(name = "id_artesao", nullable = false)
    private Artesao artesao;

    @Column(name = "imagem_url", nullable = false, length = 255)
    private String imagemUrl;

    @Column(name = "legenda", nullable = false, length = 150)
    private String legenda;

    @Lob
    @Column(name = "descricao")
    private String descricao;

    @Column(name = "categoria", length = 100)
    private String categoria;

    @Column(name = "data_publicacao", nullable = false)
    private LocalDateTime dataPublicacao;

    public Trabalho() {
    }

    public Long getIdTrabalho() {
        return idTrabalho;
    }

    public Artesao getArtesao() {
        return artesao;
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

    public LocalDateTime getDataPublicacao() {
        return dataPublicacao;
    }

    public void setArtesao(Artesao artesao) {
        this.artesao = artesao;
    }

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

    public void setDataPublicacao(LocalDateTime dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }
}