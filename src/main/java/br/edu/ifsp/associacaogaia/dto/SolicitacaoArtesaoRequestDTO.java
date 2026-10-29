package br.edu.ifsp.associacaogaia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SolicitacaoArtesaoRequestDTO{
    private String justificativa;
    private String nomeExibicao;
    private String tituloCurto;
    private String cidade;
    private String biografia;
    private String instagram;
    private String whatsappUrl;

    public SolicitacaoArtesaoRequestDTO(){
    }

    @NotBlank
    @Size(max = 1000)
    public String getJustificativa(){
        return justificativa;
    }

    @NotBlank
    @Size(max = 150)
    public String getNomeExibicao(){
        return nomeExibicao;
    }

    @NotBlank
    @Size(max = 200)
    public String getTituloCurto(){
        return tituloCurto;
    }

    @NotBlank
    @Size(max = 100)
    public String getCidade(){
        return cidade;
    }

    public String getBiografia(){
        return biografia;
    }

    @Size(max = 100)
    public String getInstagram(){
        return instagram;
    }

    @Size(max = 255)
    public String getWhatsappUrl(){
        return whatsappUrl;
    }

    public void setJustificativa(String justificativa){
        this.justificativa = justificativa;
    }

    public void setNomeExibicao(String nomeExibicao){
        this.nomeExibicao = nomeExibicao;
    }

    public void setTituloCurto(String tituloCurto){
        this.tituloCurto = tituloCurto;
    }

    public void setCidade(String cidade){
        this.cidade = cidade;
    }
    public void setBiografia(String biografia){
        this.biografia = biografia;
    }

    public void setInstagram(String instagram){
        this.instagram = instagram;
    }

    public void setWhatsappUrl(String whatsappUrl){
        this.whatsappUrl = whatsappUrl;
    }
}