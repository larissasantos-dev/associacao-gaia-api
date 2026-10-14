package br.edu.ifsp.associacaogaia.model;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;

@Entity // Aqui fica a área do Evento no Banco de dados, com todas as informações necessarias para criar um evento.
@Table(name = "evento")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private Long idEvento;

    @Column(name = "imagem_url", nullable = false, length = 255)
    private String imagemUrl;

    @Column(name = "titulo", nullable = false, length = 150)
    private String titulo;

    @Lob 
    @Column(name = "descricao")
    private String descricao;

    @Column(name = "local", nullable = false, length = 255)
    private String local;

    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    public Evento(){
    }

    public Long getIdEvento(){
        return idEvento;
    }

    public String getImagemUrl(){
        return imagemUrl;
    }

    public String getTitulo(){
        return titulo;
    }

    public String getDescricao(){
        return descricao;
    }

    public String getLocal(){
        return local;
    }

    public LocalDate getData(){
        return data;
    }

    public LocalTime getHora(){
        return hora;
    }

    public void setImagemUrl(String imagemUrl) {
        this.imagemUrl = imagemUrl;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }
}