package br.edu.ifsp.associacaogaia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ArtesaoAtualizacaoDTO {
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

    public ArtesaoAtualizacaoDTO(){
    }

    @NotBlank
    @Size(max = 150)
    public String getNomeExibicao() {
        return nomeExibicao;
    }

    @Size(max = 200)
    public String getTituloCurto() {
        return tituloCurto;
    }

    public String getBiografia() {
        return biografia;
    }

    public String getLead() {
        return lead;
    }

    @Size(max = 100)
    public String getCidade() {
        return cidade;
    }

    @Size(max = 50)
    public String getMembroDesde() {
        return membroDesde;
    }

    @Size(max = 255)
    public String getLocaisAtendimento() {
        return locaisAtendimento;
    }

    @Size(max = 255)
    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Size(max = 100)
    public String getInstagram() {
        return instagram;
    }

    @Size(max = 255)
    public String getWhatsappUrl() {
        return whatsappUrl;
    }

    @Size(max = 255)
    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setNomeExibicao(String nomeExibicao) {
        this.nomeExibicao = nomeExibicao;
    }

    public void setTituloCurto(String tituloCurto) {
        this.tituloCurto = tituloCurto;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }

    public void setLead(String lead) {
        this.lead = lead;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setMembroDesde(String membroDesde) {
        this.membroDesde = membroDesde;
    }

    public void setLocaisAtendimento(String locaisAtendimento) {
        this.locaisAtendimento = locaisAtendimento;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public void setWhatsappUrl(String whatsappUrl) {
        this.whatsappUrl = whatsappUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }
}
