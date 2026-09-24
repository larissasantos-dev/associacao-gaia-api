package br.edu.ifsp.associacaogaia.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AuthenticationEntryPointCustomTest {

    private final AuthenticationEntryPointCustom entryPoint = new AuthenticationEntryPointCustom();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void commence_deveResponder401ComCorpoJson() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        entryPoint.commence(request, response,
                new InsufficientAuthenticationException("sem token"));

        assertEquals(401, response.getStatus());
        assertTrue(response.getContentType().startsWith("application/json"));

        // UTF-8 explícito: o padrão do mock é ISO-8859-1 e estragaria os acentos
        JsonNode corpo = objectMapper.readTree(response.getContentAsString(StandardCharsets.UTF_8));
        assertEquals(401, corpo.get("status").asInt());
        assertEquals("Não autenticado", corpo.get("erro").asText());
        assertEquals("Token ausente, inválido ou expirado.", corpo.get("mensagem").asText());
    }
}