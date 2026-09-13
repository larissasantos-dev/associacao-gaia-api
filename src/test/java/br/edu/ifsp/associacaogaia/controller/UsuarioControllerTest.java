package br.edu.ifsp.associacaogaia.controller;

import br.edu.ifsp.associacaogaia.dto.LoginDTO;
import br.edu.ifsp.associacaogaia.dto.UsuarioCadastroDTO;
import br.edu.ifsp.associacaogaia.exception.CredenciaisInvalidasException;
import br.edu.ifsp.associacaogaia.exception.EmailJaCadastradoException;
import br.edu.ifsp.associacaogaia.model.TipoUsuario;
import br.edu.ifsp.associacaogaia.model.Usuario;
import br.edu.ifsp.associacaogaia.security.SecurityFilter;
import br.edu.ifsp.associacaogaia.service.TokenService;
import br.edu.ifsp.associacaogaia.service.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.Mockito.when;
import java.util.Optional;

import org.junit.jupiter.api.Test;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private SecurityFilter securityFilter;

    @Test
    void deveRetornar200AoBuscarUsuarioExistente() throws Exception {

        Usuario usuario = new Usuario(
                "Larissa",
                "larissa@gmail.com",
                "123456",
                "11999999999",
                TipoUsuario.ARTESAO
        );

        when(usuarioService.buscarUsuario(1L))
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

        when(usuarioService.buscarUsuario(1L))
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

    @Test
    void deveCadastrarUsuarioComSucesso() throws Exception{

        Usuario usuario = new Usuario(
                "Larissa",
                "larissa@gmail.com",
                "123456",
                "11999999999",
                TipoUsuario.ARTESAO
        );

        when(usuarioService.cadastrarUsuario(org.mockito.ArgumentMatchers.any(UsuarioCadastroDTO.class)))
                .thenReturn(usuario);

        mockMvc.perform(
                post("/api/usuarios")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                "nome": "Larissa",
                                "email": "larissa@gmail.com",
                                "senha": "123456",
                                "telefone": "11999999999",
                                "tipoUsuario": "ARTESAO"
                                }
                                """)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Larissa"))
                .andExpect(jsonPath("$.email").value("larissa@gmail.com"))
                .andExpect(jsonPath("$.telefone").value("11999999999"))
                .andExpect(jsonPath("$.tipoUsuario").value("ARTESAO"))
                .andExpect(jsonPath("$.senha").doesNotExist());
    }

    @Test
    void deveRetornar400QuandoNomeNaoForInformado() throws Exception {
        mockMvc.perform(
                post("/api/usuarios")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                "email": "larissa@gmail.com",
                                "senha": "123456",
                                "telefone": "11999999999",
                                "tipoUsuario": "ARTESAO"
                                }
                                """)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void DeveRetornar400QuandoEmailForInvalido() throws Exception{
        mockMvc.perform(
                post("/api/usuarios")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                "nome": "Larissa",
                                "email": "email-invalido",
                                "senha": "123456",
                                "telefone": "11999999999",
                                "tipoUsuario": "ARTESAO"
                                }
                                """)
        ).andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar409QuandoEmailJaEstiverCadastrado() throws Exception{
        when(usuarioService.cadastrarUsuario(
                org.mockito.ArgumentMatchers.any(UsuarioCadastroDTO.class)
        )).thenThrow(new EmailJaCadastradoException("E-mail já cadastrado."));

        mockMvc.perform(
                post("/api/usuarios")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                "nome": "Larissa",
                                "email": "larissa@gmail.com",
                                "senha": "123456",
                                "telefone": "11999999999",
                                "tipoUsuario": "ARTESAO"
                                }
                                """)
        )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$").value("E-mail já cadastrado."));
    }

    @Test
    void deveRealizarLoginComSucesso() throws Exception {
        Usuario usuario = new Usuario(
                "Larissa",
                "larissa@gmail.com",
                "senhaCriptografada",
                "11999999999",
                TipoUsuario.ARTESAO
        );

        when(usuarioService.realizarLogin(
                org.mockito.ArgumentMatchers.any(LoginDTO.class)
        )).thenReturn(usuario);

        when(tokenService.gerarToken(usuario))
                .thenReturn("token-jwt-teste");

        mockMvc.perform(
                        post("/api/usuarios/login")
                                .contentType(APPLICATION_JSON)
                                .content("""
                                {
                                    "email": "larissa@gmail.com",
                                    "senha": "123456"
                                }
                                """)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-jwt-teste"))
                .andExpect(jsonPath("$.usuario.nome").value("Larissa"))
                .andExpect(jsonPath("$.usuario.email").value("larissa@gmail.com"))
                .andExpect(jsonPath("$.usuario.tipoUsuario").value("ARTESAO"))
                .andExpect(jsonPath("$.usuario.senha").doesNotExist());
    }

    @Test
    void deveRetornar401QuandoCredenciaisForemInvalidas() throws Exception {

        when(usuarioService.realizarLogin(
                org.mockito.ArgumentMatchers.any(LoginDTO.class)
        )).thenThrow(
                new CredenciaisInvalidasException("E-mail ou senha inválidos.")
        );

        mockMvc.perform(
                        post("/api/usuarios/login")
                                .contentType(APPLICATION_JSON)
                                .content("""
                            {
                            "email": "larissa@gmail.com",
                            "senha": "senhaErrada"
                            }
                            """)
                )
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$").value("E-mail ou senha inválidos."));
    }

    @Test
    void deveRetornar400QuandoEmailDoLoginForInvalido() throws Exception {

        mockMvc.perform(
                        post("/api/usuarios/login")
                                .contentType(APPLICATION_JSON)
                                .content("""
                            {
                            "email": "email-invalido",
                            "senha": "123456"
                            }
                            """)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400QuandoSenhaDoLoginNaoForInformada() throws Exception {

        mockMvc.perform(
                        post("/api/usuarios/login")
                                .contentType(APPLICATION_JSON)
                                .content("""
                            {
                            "email": "larissa@gmail.com"
                            }
                            """)
                )
                .andExpect(status().isBadRequest());
    }
}