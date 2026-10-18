package br.edu.ifsp.associacaogaia.controller;

import br.edu.ifsp.associacaogaia.dto.ArtesaoAtualizacaoDTO;
import br.edu.ifsp.associacaogaia.dto.ArtesaoResponseDTO;
import br.edu.ifsp.associacaogaia.model.Artesao;
import br.edu.ifsp.associacaogaia.model.Usuario;
import br.edu.ifsp.associacaogaia.security.UsuarioDetails;
import br.edu.ifsp.associacaogaia.service.ArtesaoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtesaoControllerTest {

    @Mock
    private ArtesaoService artesaoService;

    @Mock
    private UsuarioDetails usuarioDetails;

    @Mock
    private Usuario usuario;

    @Mock
    private Artesao artesao;

    private ArtesaoController artesaoController;

    @BeforeEach
    void setUp() {
        artesaoController = new ArtesaoController(artesaoService);

        when(usuarioDetails.getUsuario()).thenReturn(usuario);
        when(usuario.getIdUsuario()).thenReturn(1L);
    }

    @Test
    void buscarMeuPerfil_deveBuscarPerfilDoUsuarioAutenticado() {
        when(artesaoService.buscarPerfilDoUsuarioAutenticado(1L))
                .thenReturn(artesao);

        ArtesaoResponseDTO resultado =
                artesaoController.buscarMeuPerfil(usuarioDetails);

        assertNotNull(resultado);

        verify(artesaoService)
                .buscarPerfilDoUsuarioAutenticado(1L);
    }

    @Test
    void atualizarMeuPerfil_deveAtualizarPerfilDoUsuarioAutenticado() {
        ArtesaoAtualizacaoDTO dto = new ArtesaoAtualizacaoDTO();
        dto.setNomeExibicao("Artesanato");

        when(artesaoService.atualizarPerfil(1L, dto))
                .thenReturn(artesao);

        ArtesaoResponseDTO resultado =
                artesaoController.atualizarMeuPerfil(dto, usuarioDetails);

        assertNotNull(resultado);

        verify(artesaoService)
                .atualizarPerfil(1L, dto);
    }
}