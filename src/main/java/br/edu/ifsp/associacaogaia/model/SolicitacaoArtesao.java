package br.edu.ifsp.associacaogaia.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "solicitacao_artesao")
public class SolicitacaoArtesao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitacao")
    private Long idSolicitacao;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "justificativa", nullable = false, length = 1000)
    private String justificativa;

    @Column(name = "nome_exibicao", nullable = false, length = 150)
    private String nomeExibicao;

    @Column(name = "titulo_curto", nullable = false, length = 200)
    private String tituloCurto;

    @Column(name = "cidade", nullable = false, length = 100)
    private String cidade;

    @Lob
    @Column(name = "biografia")
    private String biografia;

    @Column(name = "instagram", length = 100)
    private String instagram;

    @Column(name = "whatsapp_url", length = 255)
    private String whatsappUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusSolicitacao status;

    @Column(name = "data_solicitacao", nullable = false)
    private LocalDateTime dataSolicitacao;

    @Column(name = "data_resposta")
    private LocalDateTime dataResposta;

    @Column(name = "motivo_rejeicao", length = 500)
    private String motivoRejeicao;

    protected SolicitacaoArtesao(){}

    public SolicitacaoArtesao(Usuario usuario, String justificativa,
                              String nomeExibicao, String tituloCurto, String cidade){
        this.usuario = usuario;
        this.justificativa = justificativa;
        this.nomeExibicao = nomeExibicao;
        this.tituloCurto = tituloCurto;
        this.cidade = cidade;
        this.status = StatusSolicitacao.PENDENTE;
        this.dataSolicitacao = LocalDateTime.now();
    }

    public Long getIdSolicitacao(){
        return idSolicitacao;

    }
    public Usuario getUsuario(){
        return usuario;
    }

    public String getJustificativa(){
        return justificativa;
    }

    public String getNomeExibicao(){
        return nomeExibicao;
    }

    public String getTituloCurto(){
        return tituloCurto;
    }

    public String getCidade(){
        return cidade;
    }

    public String getBiografia(){
        return biografia;
    }

    public String getInstagram(){
        return instagram;
    }

    public String getWhatsappUrl(){
        return whatsappUrl;
    }

    public StatusSolicitacao getStatus(){
        return status;
    }

    public LocalDateTime getDataSolicitacao(){
        return dataSolicitacao;
    }

    public LocalDateTime getDataResposta(){
        return dataResposta;
    }

    public String getMotivoRejeicao(){
        return motivoRejeicao;
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
