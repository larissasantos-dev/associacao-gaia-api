package br.edu.ifsp.associacaogaia.controller;

import br.edu.ifsp.associacaogaia.dto.LoginDTO;
import br.edu.ifsp.associacaogaia.dto.UsuarioCadastroDTO;
import br.edu.ifsp.associacaogaia.model.TipoUsuario;
import br.edu.ifsp.associacaogaia.model.Usuario;
import br.edu.ifsp.associacaogaia.repository.UsuarioRepository;
import br.edu.ifsp.associacaogaia.service.TokenService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // dá rollback no banco depois de cada teste
class UsuarioControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    private Usuario admin;
    private Usuario visitante;
    private String tokenAdmin;
    private String tokenVisitante;

    @BeforeEach
    void setUp() {
        admin = usuarioRepository.save(new Usuario(
                "Admin Teste", "admin@teste.com",
                passwordEncoder.encode("123456"),
                "11999999999", TipoUsuario.ADMINISTRADOR
        ));

        visitante = usuarioRepository.save(new Usuario(
                "Visitante Teste", "visitante@teste.com",
                passwordEncoder.encode("123456"),
                "11988888888", TipoUsuario.VISITANTE
        ));

        tokenAdmin = tokenService.gerarToken(admin);
        tokenVisitante = tokenService.gerarToken(visitante);
    }

    // ---------- CADASTRO (público) ----------

    @Test
    void cadastro_semToken_deveFuncionar() throws Exception {
        UsuarioCadastroDTO dados = new UsuarioCadastroDTO();
        dados.setNome("Novo Usuario");
        dados.setEmail("novo@teste.com");
        dados.setSenha("123456");
        dados.setTelefone("11977777777");

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dados)))
                .andExpect(status().isCreated());
    }

    // ---------- LOGIN (público) ----------

    @Test
    void login_semToken_deveFuncionar() throws Exception {
        LoginDTO dados = new LoginDTO();
        dados.setEmail("admin@teste.com");
        dados.setSenha("123456");

        mockMvc.perform(post("/api/usuarios/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dados)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    // ---------- LISTAGEM (só admin) ----------

    @Test
    void listarUsuarios_comTokenAdmin_deveRetornar200() throws Exception {
        mockMvc.perform(get("/api/usuarios")
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk());
    }

    @Test
    void listarUsuarios_comTokenVisitante_deveRetornar403() throws Exception {
        mockMvc.perform(get("/api/usuarios")
                        .header("Authorization", "Bearer " + tokenVisitante))
                .andExpect(status().isForbidden());
    }

    @Test
    void listarUsuarios_semToken_deveRetornar401ComCorpoJson() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.erro").value("Não autenticado"));
    }

    @Test
    void listarUsuarios_comTokenInvalido_deveRetornar401() throws Exception {
        mockMvc.perform(get("/api/usuarios")
                        .header("Authorization", "Bearer token.invalido.aqui"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void listarUsuarios_comHeaderSemPrefixoBearer_deveRetornar401() throws Exception {
        mockMvc.perform(get("/api/usuarios")
                        .header("Authorization", tokenAdmin))
                .andExpect(status().isUnauthorized());
    }

    // ---------- BUSCAR POR ID (dono ou admin) ----------

    @Test
    void buscarUsuarioPorId_comTokenDoProprioUsuario_deveRetornar200() throws Exception {
        mockMvc.perform(get("/api/usuarios/" + visitante.getIdUsuario())
                        .header("Authorization", "Bearer " + tokenVisitante))
                .andExpect(status().isOk());
    }

    @Test
    void buscarUsuarioPorId_comTokenDeOutroUsuarioComum_deveRetornar403() throws Exception {
        Usuario outroVisitante = usuarioRepository.save(new Usuario(
                "Outro Visitante", "outro@teste.com",
                passwordEncoder.encode("123456"),
                "11966666666", TipoUsuario.VISITANTE
        ));

        mockMvc.perform(get("/api/usuarios/" + outroVisitante.getIdUsuario())
                        .header("Authorization", "Bearer " + tokenVisitante))
                .andExpect(status().isForbidden());
    }

    @Test
    void buscarUsuarioPorId_comTokenAdmin_deveRetornar200() throws Exception {
        mockMvc.perform(get("/api/usuarios/" + visitante.getIdUsuario())
                        .header("Authorization", "Bearer " + tokenAdmin))
                .andExpect(status().isOk());
    }
}