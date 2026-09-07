package br.edu.ifsp.associacaogaia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDateTime;

// Anotação para dizer ao JPA que essa classe representa uma entidade que existe no BD
@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private LocalDateTime dataCadastro;

    @Enumerated(EnumType.STRING)
    private TipoUsuario tipoUsuario;

    protected Usuario(){
    }
    public Usuario(String nome, String email, String senha, String telefone, TipoUsuario tipoUsuario){
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.telefone = telefone;
        this.tipoUsuario = tipoUsuario;
        this.dataCadastro = LocalDateTime.now();
    }

    public Integer getIdUsuario(){
        return idUsuario;
    }

    public String getNome(){
        return nome;
    }

    public String getEmail(){
        return email;
    }

    public String getSenha(){
        return senha;
    }

    public String getTelefone(){
        return telefone;
    }

    public LocalDateTime getDataCadastro(){
        return dataCadastro;
    }

    public TipoUsuario getTipoUsuario(){
        return tipoUsuario;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setTelefone(String telefone){
        this.telefone = telefone;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario){
        this.tipoUsuario = tipoUsuario;
    }


}