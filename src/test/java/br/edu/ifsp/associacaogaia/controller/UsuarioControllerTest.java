package br.edu.ifsp.associacaogaia.controller;

import br.edu.ifsp.associacaogaia.model.TipoUsuario;
import br.edu.ifsp.associacaogaia.model.Usuario;
import br.edu.ifsp.associacaogaia.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)

public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @Test
    void deveRetornar200AoBuscarUsuarioExistente() throws Exception {

        Usuario usuario = new Usuario(
                "Larissa",
                "larissa@gmail.com",
                "123456",
                "11999999999",
                TipoUsuario.ARTESAO
        );

        when(usuarioService.buscarUsuario(1))
                .thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(jsonPath("$.nome").value("Larissa"))
                .andExpect(jsonPath("$.email").value("larissa@gmail.com"))
                .andExpect(jsonPath("$.tipoUsuario").value("ARTESAO"))
                .andExpect(jsonPath("$.senha").doesNotExist())
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar404AoBuscarUsuarioInexistente() throws Exception {

        when(usuarioService.buscarUsuario(1))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar200AoBuscarUsuarioPorEmail() throws Exception {

        Usuario usuario = new Usuario(
                "Larissa",
                "larissa@gmail.com",
                "123456",
                "11999999999",
                TipoUsuario.ARTESAO
        );

        when(usuarioService.buscarUsuarioPorEmail("larissa@gmail.com"))
                .thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/usuarios/email/larissa@gmail.com"))
                .andExpect(jsonPath("$.nome").value("Larissa"))
                .andExpect(jsonPath("$.email").value("larissa@gmail.com"))
                .andExpect(jsonPath("$.tipoUsuario").value("ARTESAO"))
                .andExpect(jsonPath("$.senha").doesNotExist())
                .andExpect(status().isOk());
    }

    @Test
    void deveRetornar404AoBuscarEmailInexistente() throws Exception {

        when(usuarioService.buscarUsuarioPorEmail("naoexiste@gmail.com"))
                .thenReturn(Optional.empty());

        mockMvc.perform(
                get("/api/usuarios/email/naoexiste@gmail.com")
        ).andExpect(status().isNotFound());
    }
}