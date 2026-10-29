package br.edu.ifsp.associacaogaia.controller;

import br.edu.ifsp.associacaogaia.model.TipoUsuario;
import br.edu.ifsp.associacaogaia.model.Usuario;
import br.edu.ifsp.associacaogaia.repository.UsuarioRepository;
import br.edu.ifsp.associacaogaia.service.TokenService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class SolicitacaoArtesaoControllerIntegrationTest {

    private static final String URL = "/api/solicitacoes-artesao";

    private static final String JSON_VALIDO = """
            {"justificativa":"Quero expor meus trabalhos","nomeExibicao":"Ana Silva",
             "tituloCurto":"Ceramista","cidade":"Tatui"}
            """;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    private String tokenVisitante;
    private String tokenAdmin;

    @BeforeEach
    void setUp() {
        Usuario visitante = usuarioRepository.save(new Usuario(
                "Visitante Teste", "visitante@teste.com",
                passwordEncoder.encode("123456"), "11988888888", TipoUsuario.VISITANTE));

        Usuario admin = usuarioRepository.save(new Usuario(
                "Admin Teste", "admin@teste.com",
                passwordEncoder.encode("123456"), "11999999999", TipoUsuario.ADMINISTRADOR));

        tokenVisitante = tokenService.gerarToken(visitante);
        tokenAdmin = tokenService.gerarToken(admin);
    }

    @Test
    void criar_semToken_deveSerRecusado() throws Exception {
        // Depois que o ajuste do 401 for mergeado, trocar por isUnauthorized()
        mockMvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_VALIDO))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void criar_comVisitanteEDadosValidos_deveRetornar201ComStatusPendente() throws Exception {
        mockMvc.perform(post(URL)
                        .header("Authorization", "Bearer " + tokenVisitante)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDENTE"))
                .andExpect(jsonPath("$.nomeExibicao").value("Ana Silva"));
    }

    @Test
    void criar_comStatusNoCorpo_deveIgnorarEManterPendente() throws Exception {
        String jsonComStatusForjado = """
                {"justificativa":"Quero expor meus trabalhos","nomeExibicao":"Ana Silva",
                 "tituloCurto":"Ceramista","cidade":"Tatui","status":"APROVADA"}
                """;

        mockMvc.perform(post(URL)
                        .header("Authorization", "Bearer " + tokenVisitante)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonComStatusForjado))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("PENDENTE"));
    }

    @Test
    void criar_semJustificativa_deveRetornar400() throws Exception {
        String jsonSemJustificativa = """
                {"nomeExibicao":"Ana Silva","tituloCurto":"Ceramista","cidade":"Tatui"}
                """;

        mockMvc.perform(post(URL)
                        .header("Authorization", "Bearer " + tokenVisitante)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonSemJustificativa))
                .andExpect(status().isBadRequest());
    }

    @Test
    void criar_comAdministrador_deveRetornar403() throws Exception {
        mockMvc.perform(post(URL)
                        .header("Authorization", "Bearer " + tokenAdmin)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_VALIDO))
                .andExpect(status().isForbidden());
    }

    @Test
    void criar_duasVezes_segundaDeveRetornar409() throws Exception {
        mockMvc.perform(post(URL)
                        .header("Authorization", "Bearer " + tokenVisitante)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_VALIDO))
                .andExpect(status().isCreated());

        mockMvc.perform(post(URL)
                        .header("Authorization", "Bearer " + tokenVisitante)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_VALIDO))
                .andExpect(status().isConflict());
    }

    @Test
    void minha_semNenhumaSolicitacao_deveRetornar204() throws Exception {
        mockMvc.perform(get(URL + "/minha")
                        .header("Authorization", "Bearer " + tokenVisitante))
                .andExpect(status().isNoContent());
    }

    @Test
    void minha_aposSolicitar_deveRetornar200ComAPendente() throws Exception {
        mockMvc.perform(post(URL)
                        .header("Authorization", "Bearer " + tokenVisitante)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_VALIDO))
                .andExpect(status().isCreated());

        mockMvc.perform(get(URL + "/minha")
                        .header("Authorization", "Bearer " + tokenVisitante))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDENTE"));
    }
}