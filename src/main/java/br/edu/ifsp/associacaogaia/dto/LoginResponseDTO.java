package br.edu.ifsp.associacaogaia.dto;

public class LoginResponseDTO {
    private String token;
    private UsuarioResponseDTO usuario;

    public LoginResponseDTO(String token, UsuarioResponseDTO usuario){
        this.token = token;
        this.usuario = usuario;
    }

    public String getToken(){
        return token;
    }

    public UsuarioResponseDTO getUsuario(){
        return usuario;
    }
}