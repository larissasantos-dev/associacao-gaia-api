package br.edu.ifsp.associacaogaia.dto;

import br.edu.ifsp.associacaogaia.model.HistoricoPreco;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** Uma mudança de preço de um trabalho, devolvida ao artesão. */
public class HistoricoPrecoResponseDTO {

    private final Long idHistorico;
    private final BigDecimal precoAnterior;
    private final BigDecimal precoNovo;
    private final LocalDateTime dataAlteracao;

    public HistoricoPrecoResponseDTO(HistoricoPreco historico) {
        this.idHistorico = historico.getIdHistorico();
        this.precoAnterior = historico.getPrecoAnterior();
        this.precoNovo = historico.getPrecoNovo();
        this.dataAlteracao = historico.getDataAlteracao();
    }

    public Long getIdHistorico() {
        return idHistorico;
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
