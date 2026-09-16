package br.edu.ifsp.associacaogaia.dto;

import jakarta.validation.constraints.NotBlank;

public class UsuarioAtualizacaoDTO {
    private String nome;
    private String telefone;
    private String senha;

    public UsuarioAtualizacaoDTO(){
    }

    @NotBlank
    public String getNome(){
        return nome;
    }

    @NotBlank
    public String getTelefone(){
        return telefone;
    }

    public String getSenha(){
        return senha;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setTelefone(String telefone){
        this.telefone = telefone;
    }

    public void setSenha(String senha){
        this.senha = senha;
    }
}
