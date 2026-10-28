package br.edu.ifsp.associacaogaia.dto;

import br.edu.ifsp.associacaogaia.model.Evento;
import java.time.LocalDate;
import java.time.LocalTime;

public class EventoResponseDTO {
    
    private Long idEvento;
    private String imagemUrl;
    private String titulo;
    private String descricao;
    private String local;
    private LocalDate data;
    private LocalTime hora;
    private String categoria;

    public Long getIdEvento(){
        return idEvento;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getLocal() {
        return local;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getHora() {
        return hora;
    }
    public String getCategoria() {
        return categoria;
    }

    public EventoResponseDTO(Evento evento) {
        this.idEvento = evento.getIdEvento();
        this.imagemUrl = evento.getImagemUrl();
        this.titulo = evento.getTitulo();
        this.descricao = evento.getDescricao();
        this.local = evento.getLocal();
        this.data = evento.getData();
        this.hora = evento.getHora();
        this.categoria = evento.getCategoria();
    }

}
