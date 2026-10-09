package br.edu.ifsp.associacaogaia.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.Size;

public class EventoAtualizacaoDTO {
    
    @Size(max = 255, message = "A URL da imagem deve ter no máximo 255 caracteres.")
    private String imagemUrl;

    @Size(max = 150, message = "O título deve ter no máximo 150 caracteres.")
    private String titulo;

    private String descricao;

    @Size(max = 255, message = "O local deve ter no máximo 255 caracteres.")
    private String local;

    private LocalDate data;

    private LocalTime hora;

    public EventoAtualizacaoDTO(){
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

    public void setImagemUrl(String imagemUrl){
        this.imagemUrl = imagemUrl;
    }

    public void setTitulo(String titulo){
        this.titulo = titulo;
    }

    public void setDescricao(String descricao){
        this.descricao = descricao;
    }
    
    public void setLocal(String local){
        this.local = local;
    }

    public void setData(LocalDate data){
        this.data = data;
    }

    public void setHora(LocalTime hora){
        this.hora = hora;
    }
}
