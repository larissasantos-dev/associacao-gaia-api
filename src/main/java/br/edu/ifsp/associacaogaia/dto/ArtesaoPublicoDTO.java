package br.edu.ifsp.associacaogaia.dto;

import br.edu.ifsp.associacaogaia.model.Artesao;

/**
 * Perfil público de um artesão (páginas abertas a qualquer visitante).
 * Não inclui e-mail, telefone nem qualquer dado da conta de usuário.
 */
public class ArtesaoPublicoDTO {

    private final Long idArtesao;
    private final String nomeExibicao;
    private final String tituloCurto;
    private final String biografia;
    private final String lead;
    private final String cidade;
    private final String membroDesde;
    private final String locaisAtendimento;
    private final String avatarUrl;
    private final String instagram;
    private final String whatsappUrl;
    private final String facebookUrl;
    private final long totalTrabalhos;

    public ArtesaoPublicoDTO(Artesao artesao, long totalTrabalhos) {
        this.idArtesao = artesao.getIdArtesao();
        this.nomeExibicao = artesao.getNomeExibicao();
        this.tituloCurto = artesao.getTituloCurto();
        this.biografia = artesao.getBiografia();
        this.lead = artesao.getLead();
        this.cidade = artesao.getCidade();
        this.membroDesde = artesao.getMembroDesde();
        this.locaisAtendimento = artesao.getLocaisAtendimento();
        this.avatarUrl = artesao.getAvatarUrl();
        this.instagram = artesao.getInstagram();
        this.whatsappUrl = artesao.getWhatsappUrl();
        this.facebookUrl = artesao.getFacebookUrl();
        this.totalTrabalhos = totalTrabalhos;
    }

    public Long getIdArtesao() { return idArtesao; }
    public String getNomeExibicao() { return nomeExibicao; }
    public String getTituloCurto() { return tituloCurto; }
    public String getBiografia() { return biografia; }
    public String getLead() { return lead; }
    public String getCidade() { return cidade; }
    public String getMembroDesde() { return membroDesde; }
    public String getLocaisAtendimento() { return locaisAtendimento; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getInstagram() { return instagram; }
    public String getWhatsappUrl() { return whatsappUrl; }
    public String getFacebookUrl() { return facebookUrl; }
    public long getTotalTrabalhos() { return totalTrabalhos; }
}
