package br.edu.ifsp.associacaogaia.dto;

import br.edu.ifsp.associacaogaia.model.TipoUsuario;
import br.edu.ifsp.associacaogaia.model.Usuario;

import java.time.LocalDateTime;

public class UsuarioResponseDTO {
    private Long idUsuario;
    private String nome;
    private String email;
    private String telefone;
    private LocalDateTime dataCadastro;
    private TipoUsuario tipoUsuario;
    private boolean ativo;

    public UsuarioResponseDTO(Usuario usuario){
        this.idUsuario = usuario.getIdUsuario();
        this.nome = usuario.getNome();
        this.email = usuario.getEmail();
        this.telefone = usuario.getTelefone();
        this.dataCadastro = usuario.getDataCadastro();
        this.tipoUsuario = usuario.getTipoUsuario();
        this.ativo = usuario.isAtivo();
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public boolean isAtivo() {
        return ativo;
    }
}