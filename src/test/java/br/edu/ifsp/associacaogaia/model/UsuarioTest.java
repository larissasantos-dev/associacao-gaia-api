package br.edu.ifsp.associacaogaia.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UsuarioTest{

    @Test
    void deveCriarUsuarioVisitante(){

        Usuario usuario = new Usuario(
                "gabriel",
                "gabriel.teste@gmail.com",
                "111111",
                "11111111111",
                TipoUsuario.VISITANTE
        );

        assertEquals(TipoUsuario.VISITANTE, usuario.getTipoUsuario());
    }

    @Test
    void deveCriarUsuarioArtesao(){

        Usuario usuario = new Usuario(
                "Lana",
                "lana.teste@gmail.com",
                "999999",
                "999999999999",
                TipoUsuario.ARTESAO
        );
        assertEquals(TipoUsuario.ARTESAO, usuario.getTipoUsuario());
    }

    @Test
    void deveCriarUsuarioAdministrador(){

        Usuario usuario = new Usuario(
                "Larissa",
                "larissa.teste@gmail.com",
                "777777",
                "77777777777",
                TipoUsuario.ADMINISTRADOR
        );
        assertEquals(TipoUsuario.ADMINISTRADOR, usuario.getTipoUsuario());
    }

    @Test
    void deveCadastrarUsuarioPreenchendoDataAutomaticamente(){
        
            Usuario usuario = new Usuario(
                    "João",
                    "joao.teste@gmail.com",
                    "1234567",
                    "11998888888",
                    TipoUsuario.ARTESAO
            );

            assertNotNull(usuario.getDataCadastro());
    }
}