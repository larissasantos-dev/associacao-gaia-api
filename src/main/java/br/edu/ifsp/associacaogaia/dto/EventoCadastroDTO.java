package br.edu.ifsp.associacaogaia.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EventoCadastroDTO {

    @NotBlank(message = "A imagem é obrigatória.")
    @Size(max = 255, message = "A URL da imagem deve ter no máximo 255 caracteres.")
    private String imagemUrl;

    @NotBlank(message = "O título é obrigatório.")
    @Size(max = 150, message = "O título deve ter no máximo 150 caracteres.")
    private String titulo;

    // Opcional
    private String descricao;

    @NotBlank(message = "O local é obrigatório.")
    @Size(max = 255, message = "O local deve ter no máximo 255 caracteres.")
    private String local;

    @NotNull(message = "A data é obrigatória.")
    private LocalDate data;

    @NotNull(message = "A hora é obrigatória.")
    private LocalTime hora;

    public EventoCadastroDTO() {
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