package br.edu.ifsp.associacaogaia.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artesao")
public class Artesao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_artesao")
    private Long idArtesao;

    @OneToOne
    @JoinColumn(name = "id_usuario", nullable = false, unique = true)
    private Usuario usuario;

    @Column(name = "nome_exibicao", nullable = false, length = 150)
    private String nomeExibicao;

    @Column(name = "titulo_curto", length = 200)
    private String tituloCurto;

    @Lob
    @Column(name = "biografia")
    private String biografia;

    @Lob
    @Column(name = "`lead`")
    private String lead;

    @Column(name = "cidade", length = 100)
    private String cidade;

    @Column(name = "membro_desde", length = 50)
    private String membroDesde;

    @Column(name = "locais_atendimento", length = 255)
    private String locaisAtendimento;

    @Column(name = "avatar_url", length = 255)
    private String avatarUrl;

    @Column(name = "instagram", length = 100)
    private String instagram;

    @Column(name = "whatsapp_url", length = 255)
    private String whatsappUrl;

    @Column(name = "facebook_url", length = 255)
    private String facebookUrl;

    @OneToMany(
            mappedBy = "artesao",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Trabalho> trabalhos = new ArrayList<>();

    protected Artesao() {
    }

    public Long getIdArtesao() {
        return idArtesao;
    }

    public Usuario getUsuario() {
        return usuario;
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

    public List<Trabalho> getTrabalhos() {
        return trabalhos;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    public void setTrabalhos(List<Trabalho> trabalhos) {
        this.trabalhos = trabalhos;
    }
}