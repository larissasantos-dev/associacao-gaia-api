package br.edu.ifsp.associacaogaia.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Registro de uma mudança de preço de um {@link Trabalho}.
 *
 * Cada vez que o artesão altera o preço, uma linha é gravada na tabela
 * historico_preco com o valor anterior, o novo valor e a data da alteração.
 * Valores nulos significam "sob consulta" (sem preço definido).
 */
@Entity
@Table(name = "historico_preco")
public class HistoricoPreco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historico")
    private Long idHistorico;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_trabalho", nullable = false)
    private Trabalho trabalho;

    @Column(name = "preco_anterior", precision = 10, scale = 2)
    private BigDecimal precoAnterior;

    @Column(name = "preco_novo", precision = 10, scale = 2)
    private BigDecimal precoNovo;

    @Column(name = "data_alteracao", nullable = false)
    private LocalDateTime dataAlteracao;

    protected HistoricoPreco() {
    }

    public HistoricoPreco(Trabalho trabalho, BigDecimal precoAnterior, BigDecimal precoNovo) {
        this.trabalho = trabalho;
        this.precoAnterior = precoAnterior;
        this.precoNovo = precoNovo;
        this.dataAlteracao = LocalDateTime.now();
    }

    public Long getIdHistorico() {
        return idHistorico;
    }

    public Trabalho getTrabalho() {
        return trabalho;
    }

    public BigDecimal getPrecoAnterior() {
        return precoAnterior;
    }

    public BigDecimal getPrecoNovo() {
        return precoNovo;
    }

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }
}
