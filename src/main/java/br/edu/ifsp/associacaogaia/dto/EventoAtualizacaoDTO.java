
package br.edu.ifsp.associacaogaia.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EventoAtualizacaoDTO {

    @Pattern(regexp = ".*\\S.*", message = "A URL da imagem não pode estar vazia ou conter apenas espaços.")
    @Size(max = 255, message = "A URL da imagem deve ter no máximo 255 caracteres.")
    private String imagemUrl;

    @Pattern(regexp = ".*\\S.*", message = "O título não pode estar vazio ou conter apenas espaços.")
    @Size(max = 150, message = "O título deve ter no máximo 150 caracteres.")
    private String titulo;

    private String descricao;

    @Pattern(regexp = ".*\\S.*", message = "O local não pode estar vazio ou conter apenas espaços.")
    @Size(max = 255, message = "O local deve ter no máximo 255 caracteres.")
    private String local;

    private LocalDate data;

    private LocalTime hora;

    @Pattern(
        regexp = "FEIRAS|OFICINAS|EXPOSICOES|PALESTRAS|CURSOS",
        message = "A categoria informada não é válida."
    )
    private String categoria;

    public EventoAtualizacaoDTO() {
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
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
}
