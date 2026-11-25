package br.edu.ifsp.associacaogaia.dto;

import br.edu.ifsp.associacaogaia.model.Artesao;

import java.time.LocalDateTime;

/**
 * Linha da tabela "Artesãos" do painel administrativo.
 * Inclui dados da conta (e-mail, status) — por isso só o administrador acessa.
 */
public class ArtesaoAdminResponseDTO {

    private final Long idArtesao;
    private final Long idUsuario;
    private final String nomeExibicao;
    private final String tituloCurto;
    private final String cidade;
    private final String email;
    private final boolean ativo;
    private final LocalDateTime dataCadastro;
    private final long totalTrabalhos;

    public ArtesaoAdminResponseDTO(Artesao artesao, long totalTrabalhos) {
        this.idArtesao = artesao.getIdArtesao();
        this.idUsuario = artesao.getUsuario().getIdUsuario();
        this.nomeExibicao = artesao.getNomeExibicao();
        this.tituloCurto = artesao.getTituloCurto();
        this.cidade = artesao.getCidade();
        this.email = artesao.getUsuario().getEmail();
        this.ativo = artesao.getUsuario().isAtivo();
        this.dataCadastro = artesao.getUsuario().getDataCadastro();
        this.totalTrabalhos = totalTrabalhos;
    }

    public Long getIdArtesao() { return idArtesao; }
    public Long getIdUsuario() { return idUsuario; }
    public String getNomeExibicao() { return nomeExibicao; }
    public String getTituloCurto() { return tituloCurto; }
    public String getCidade() { return cidade; }
    public String getEmail() { return email; }
    public boolean isAtivo() { return ativo; }
    public LocalDateTime getDataCadastro() { return dataCadastro; }
    public long getTotalTrabalhos() { return totalTrabalhos; }
}
