package br.edu.ifsp.associacaogaia.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

// Anotação para dizer ao JPA que essa classe representa uma entidade que existe no BD
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @OneToOne(mappedBy = "usuario")
    private Artesao artesao;

    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "senha", nullable = false, length = 255)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_usuario", nullable = false, length = 20)
    private TipoUsuario tipoUsuario;

    @Column(name = "telefone", length = 20)
    private String telefone;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;

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

    public Artesao getArtesao(){
        return artesao;
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