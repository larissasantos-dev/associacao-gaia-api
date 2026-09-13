package br.edu.ifsp.associacaogaia.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UsuarioCadastroDTO {
    private String nome;
    private String email;
    private String senha;
    private String telefone;

    public UsuarioCadastroDTO(){
    }

    @NotBlank
    public String getNome(){
        return nome;
    }

    @NotBlank
    @Email
    public String getEmail(){
        return email;
    }

    @NotBlank
    public String getSenha(){
        return senha;
    }

    @NotBlank
    public String getTelefone(){
        return telefone;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setSenha(String senha){
        this.senha = senha;
    }

    public void setTelefone(String telefone){
        this.telefone = telefone;
    }
}