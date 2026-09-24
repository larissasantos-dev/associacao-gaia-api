package br.edu.ifsp.associacaogaia.security;

import br.edu.ifsp.associacaogaia.model.TipoUsuario;
import br.edu.ifsp.associacaogaia.model.Usuario;
import br.edu.ifsp.associacaogaia.repository.UsuarioRepository;
import br.edu.ifsp.associacaogaia.service.TokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Sobe o servidor de verdade (porta aleatória). Necessário porque o MockMvc
 * não executa o redirecionamento interno para /error, onde o 403 virava 401.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SegurancaHttpRealTest {

    @Value("${local.server.port}")
    private int porta;

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private TokenService tokenService;

    private Usuario visitante;
    private final HttpClient http = HttpClient.newHttpClient();

    @BeforeEach
    void setUp() {
        visitante = usuarioRepository.save(new Usuario(
                "Visitante HTTP", "visitante-" + UUID.randomUUID() + "@teste.com",
                passwordEncoder.encode("123456"), "11988888888", TipoUsuario.VISITANTE));
    }

    @AfterEach
    void tearDown() {
        usuarioRepository.delete(visitante);
    }

    private HttpResponse<String> get(String caminho, String token) throws Exception {
        HttpRequest.Builder req = HttpRequest.newBuilder(URI.create("http://localhost:" + porta + caminho)).GET();
        if (token != null) req.header("Authorization", "Bearer " + token);
        return http.send(req.build(), HttpResponse.BodyHandlers.ofString());
    }

    @Test
    void visitanteEmRotaDeAdmin_deveRetornar403() throws Exception {
        HttpResponse<String> resposta = get("/api/usuarios", tokenService.gerarToken(visitante));

        assertEquals(403, resposta.statusCode());
        assertTrue(resposta.body().contains("\"status\":403"));
    }

    @Test
    void semToken_deveRetornar401() throws Exception {
        HttpResponse<String> resposta = get("/api/usuarios", null);

        assertEquals(401, resposta.statusCode());
    }

    @Test
    void corpoInvalidoNoCadastro_deveRetornar400_eNaoRetornar401() throws Exception {
        HttpRequest req = HttpRequest.newBuilder(URI.create("http://localhost:" + porta + "/api/usuarios"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString("{\"nome\":\"\",\"email\":\"invalido\"}"))
                .build();

        HttpResponse<String> resposta = http.send(req, HttpResponse.BodyHandlers.ofString());

        assertEquals(400, resposta.statusCode());
    }
}