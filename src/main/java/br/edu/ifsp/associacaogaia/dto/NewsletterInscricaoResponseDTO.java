package br.edu.ifsp.associacaogaia.dto;

import br.edu.ifsp.associacaogaia.model.NewsletterInscricao;

import java.time.LocalDateTime;

/** Linha da lista de inscritos da newsletter no painel administrativo. */
public class NewsletterInscricaoResponseDTO {

    private final Long idInscricao;
    private final String email;
    private final boolean ativo;
    private final LocalDateTime dataInscricao;
    private final LocalDateTime dataCancelamento;

    public NewsletterInscricaoResponseDTO(NewsletterInscricao inscricao) {
        this.idInscricao = inscricao.getIdInscricao();
        this.email = inscricao.getEmail();
        this.ativo = inscricao.isAtivo();
        this.dataInscricao = inscricao.getDataInscricao();
        this.dataCancelamento = inscricao.getDataCancelamento();
    }

    public Long getIdInscricao() { return idInscricao; }
    public String getEmail() { return email; }
    public boolean isAtivo() { return ativo; }
    public LocalDateTime getDataInscricao() { return dataInscricao; }
    public LocalDateTime getDataCancelamento() { return dataCancelamento; }
}
