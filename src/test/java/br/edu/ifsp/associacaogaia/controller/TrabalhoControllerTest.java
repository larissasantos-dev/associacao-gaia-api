package br.edu.ifsp.associacaogaia.controller;

import br.edu.ifsp.associacaogaia.dto.TrabalhoCadastroDTO;
import br.edu.ifsp.associacaogaia.dto.TrabalhoResponseDTO;
import br.edu.ifsp.associacaogaia.model.Trabalho;
import br.edu.ifsp.associacaogaia.model.Usuario;
import br.edu.ifsp.associacaogaia.security.UsuarioDetails;
import br.edu.ifsp.associacaogaia.service.TrabalhoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrabalhoControllerTest {

    @Mock
    private TrabalhoService trabalhoService;

    @Mock
    private UsuarioDetails usuarioDetails;

    @Mock
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        when(usuarioDetails.getUsuario()).thenReturn(usuario);
        when(usuario.getIdUsuario()).thenReturn(1L);
    }

    @Test
    void cadastrarTrabalho_deveRetornarStatusCreated() {
        TrabalhoController controller =
                new TrabalhoController(trabalhoService);

        TrabalhoCadastroDTO dto = criarDto();
        Trabalho trabalho = new Trabalho();

        when(trabalhoService.cadastrarTrabalho(dto, 1L))
                .thenReturn(trabalho);

        ResponseEntity<TrabalhoResponseDTO> resposta =
                controller.cadastrarTrabalho(dto, usuarioDetails);

        assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
        assertNotNull(resposta.getBody());

        verify(trabalhoService)
                .cadastrarTrabalho(dto, 1L);
    }

    @Test
    void listarMeusTrabalhos_deveRetornarTrabalhosDoUsuario() {
        TrabalhoController controller =
                new TrabalhoController(trabalhoService);

        Trabalho trabalho1 = new Trabalho();
        Trabalho trabalho2 = new Trabalho();

        when(trabalhoService.listarTrabalhosDoArtesao(1L))
                .thenReturn(List.of(trabalho1, trabalho2));

        List<TrabalhoResponseDTO> resultado =
                controller.listarMeusTrabalhos(usuarioDetails);

        assertEquals(2, resultado.size());

        verify(trabalhoService)
                .listarTrabalhosDoArtesao(1L);
    }

    @Test
    void atualizarTrabalho_deveRetornarStatusOk() {
        TrabalhoController controller =
                new TrabalhoController(trabalhoService);

        TrabalhoCadastroDTO dto = criarDto();
        Trabalho trabalho = new Trabalho();

        when(trabalhoService.atualizarTrabalho(10L, dto, 1L))
                .thenReturn(trabalho);

        ResponseEntity<TrabalhoResponseDTO> resposta =
                controller.atualizarTrabalho(
                        10L,
                        dto,
                        usuarioDetails
                );

        assertEquals(HttpStatus.OK, resposta.getStatusCode());
        assertNotNull(resposta.getBody());

        verify(trabalhoService)
                .atualizarTrabalho(10L, dto, 1L);
    }

    @Test
    void excluirTrabalho_deveRetornarStatusNoContent() {
        TrabalhoController controller =
                new TrabalhoController(trabalhoService);

        doNothing()
                .when(trabalhoService)
                .excluirTrabalho(10L, 1L);

        ResponseEntity<Void> resposta =
                controller.excluirTrabalho(10L, usuarioDetails);

        assertEquals(HttpStatus.NO_CONTENT, resposta.getStatusCode());
        assertNull(resposta.getBody());

        verify(trabalhoService)
                .excluirTrabalho(10L, 1L);
    }

    private TrabalhoCadastroDTO criarDto() {
        TrabalhoCadastroDTO dto = new TrabalhoCadastroDTO();

        dto.setImagemUrl("imagem.jpg");
        dto.setLegenda("Peça artesanal");
        dto.setDescricao("Descrição");
        dto.setCategoria("Cerâmica");

        return dto;
    }
}