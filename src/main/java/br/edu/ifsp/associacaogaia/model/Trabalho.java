package br.edu.ifsp.associacaogaia.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Trabalho (card) publicado por um artesão no site da Associação Gaia.
 *
 * Cada trabalho tem uma foto (enviada ao Cloudinary), uma legenda, uma
 * descrição opcional, uma categoria opcional e um preço opcional. Quando o
 * preço é alterado, a mudança fica registrada em {@link HistoricoPreco}.
 */
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

    /** Identificador da imagem no Cloudinary (usado para apagar a foto antiga). */
    @Column(name = "imagem_public_id", length = 255)
    private String imagemPublicId;

    @Column(name = "legenda", nullable = false, length = 150)
    private String legenda;

    @Lob
    @Column(name = "descricao")
    private String descricao;

    @Column(name = "categoria", length = 100)
    private String categoria;

    /** Preço em reais. Nulo significa "sob consulta". */
    @Column(name = "preco", precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "preco_atualizado_em")
    private LocalDateTime precoAtualizadoEm;

    @Column(name = "data_publicacao", nullable = false)
    private LocalDateTime dataPublicacao;

    @OneToMany(
            mappedBy = "trabalho",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<HistoricoPreco> historicoPrecos = new ArrayList<>();

    public Trabalho() {
    }

    /**
     * Altera o preço do trabalho e registra a mudança no histórico.
     * Se o novo preço for igual ao atual, nada é feito.
     *
     * @param novoPreco novo preço (nulo = sob consulta)
     * @return true se o preço mudou, false se continuou o mesmo
     */
    public boolean alterarPreco(BigDecimal novoPreco) {
        // Guarda sempre com 2 casas decimais (centavos)
        if (novoPreco != null) {
            novoPreco = novoPreco.setScale(2, RoundingMode.HALF_UP);
        }

        if (precosIguais(this.preco, novoPreco)) {
            return false;
        }

        HistoricoPreco registro = new HistoricoPreco(this, this.preco, novoPreco);
        this.historicoPrecos.add(registro);
        this.preco = novoPreco;
        this.precoAtualizadoEm = registro.getDataAlteracao();
        return true;
    }

    private static boolean precosIguais(BigDecimal a, BigDecimal b) {
        if (a == null || b == null) {
            return a == null && b == null;
        }
        return a.compareTo(b) == 0;
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

    public String getImagemPublicId() {
        return imagemPublicId;
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

    public List<HistoricoPreco> getHistoricoPrecos() {
        return historicoPrecos;
    }

    public void setArtesao(Artesao artesao) {
        this.artesao = artesao;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public void setImagemPublicId(String imagemPublicId) {
        this.imagemPublicId = imagemPublicId;
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
