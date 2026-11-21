package br.edu.ifsp.associacaogaia.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EventoAtualizacaoDTO {

    private MultipartFile imagem;

    @Pattern(
            regexp = ".*\\S.*",
            message = "O título não pode estar vazio ou conter apenas espaços."
    )
    @Size(max = 150, message = "O título deve ter no máximo 150 caracteres.")
    private String titulo;

    private String descricao;

    @Pattern(
            regexp = ".*\\S.*",
            message = "O local não pode estar vazio ou conter apenas espaços."
    )
    @Size(max = 255, message = "O local deve ter no máximo 255 caracteres.")
    private String local;

    private LocalDate data;

    private LocalTime hora;

    @Pattern(
            regexp = "feira|oficina|exposicao|palestras|cursos",
            message = "Categoria inválida."
    )
    private String categoria;

    private Boolean cancelado;

    public EventoAtualizacaoDTO() {
    }

    public MultipartFile getImagem() {
        return imagem;
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

    public Boolean getCancelado() {
        return cancelado;
    }

    public void setImagem(MultipartFile imagem) {
        this.imagem = imagem;
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

    public void setCancelado(Boolean cancelado) {
        this.cancelado = cancelado;
    }
}