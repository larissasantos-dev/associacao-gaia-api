package br.edu.ifsp.associacaogaia.dto;

import br.edu.ifsp.associacaogaia.model.Artesao;

public class ArtesaoResponseDTO {
    private Long idArtesao;
    private String nomeExibicao;
    private String tituloCurto;
    private String biografia;
    private String lead;
    private String cidade;
    private String membroDesde;
    private String locaisAtendimento;
    private String avatarUrl;
    private String instagram;
    private String whatsappUrl;
    private String facebookUrl;

    public ArtesaoResponseDTO(Artesao artesao){
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
    }

    public Long getIdArtesao() {
        return idArtesao;
    }

    public String getNomeExibicao() {
        return nomeExibicao;
    }

    public String getTituloCurto() {
        return tituloCurto;
    }

    public String getBiografia() {
        return biografia;
    }

    public String getLead() {
        return lead;
    }

    public String getCidade() {
        return cidade;
    }

    public String getMembroDesde() {
        return membroDesde;
    }

    public String getLocaisAtendimento() {
        return locaisAtendimento;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getWhatsappUrl() {
        return whatsappUrl;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }
}
