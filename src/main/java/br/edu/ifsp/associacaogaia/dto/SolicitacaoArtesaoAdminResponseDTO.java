package br.edu.ifsp.associacaogaia.dto;

import br.edu.ifsp.associacaogaia.model.SolicitacaoArtesao;
import br.edu.ifsp.associacaogaia.model.StatusSolicitacao;

import java.time.LocalDateTime;

public class SolicitacaoArtesaoAdminResponseDTO {
    private final Long idSolicitacao;
    private final StatusSolicitacao status;
    private final String justificativa;
    private final String nomeExibicao;
    private final String tituloCurto;
    private final String cidade;
    private final String biografia;
    private final String instagram;
    private final String whatsappUrl;
    private final LocalDateTime dataSolicitacao;
    private final LocalDateTime dataResposta;
    private final String motivoRejeicao;
    private final Long idUsuario;
    private final String nomeUsuario;
    private final String emailUsuario;
    private final String telefoneUsuario;

    public SolicitacaoArtesaoAdminResponseDTO(SolicitacaoArtesao s) {
        this.idSolicitacao = s.getIdSolicitacao();
        this.status = s.getStatus();
        this.justificativa = s.getJustificativa();
        this.nomeExibicao = s.getNomeExibicao();
        this.tituloCurto = s.getTituloCurto();
        this.cidade = s.getCidade();
        this.biografia = s.getBiografia();
        this.instagram = s.getInstagram();
        this.whatsappUrl = s.getWhatsappUrl();
        this.dataSolicitacao = s.getDataSolicitacao();
        this.dataResposta = s.getDataResposta();
        this.motivoRejeicao = s.getMotivoRejeicao();
        this.idUsuario = s.getUsuario().getIdUsuario();
        this.nomeUsuario = s.getUsuario().getNome();
        this.emailUsuario = s.getUsuario().getEmail();
        this.telefoneUsuario = s.getUsuario().getTelefone();
    }

    public Long getIdSolicitacao() {
        return idSolicitacao;
    }

    public StatusSolicitacao getStatus() {
        return status;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public String getNomeExibicao() {
        return nomeExibicao;
    }

    public String getTituloCurto() {
        return tituloCurto;
    }

    public String getCidade() {
        return cidade;
    }

    public String getBiografia() {
        return biografia;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getWhatsappUrl() {
        return whatsappUrl;
    }

    public LocalDateTime getDataSolicitacao() {
        return dataSolicitacao;
    }

    public LocalDateTime getDataResposta() {
        return dataResposta;
    }

    public String getMotivoRejeicao() {
        return motivoRejeicao;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public String getTelefoneUsuario() {
        return telefoneUsuario;
    }
}