package br.edu.ifsp.associacaogaia.service;

import br.edu.ifsp.associacaogaia.dto.ArtesaoAtualizacaoDTO;
import br.edu.ifsp.associacaogaia.exception.ArtesaoNaoEncontradoException;
import br.edu.ifsp.associacaogaia.model.Artesao;
import br.edu.ifsp.associacaogaia.repository.ArtesaoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtesaoServiceTest {

    @Mock
    private ArtesaoRepository artesaoRepository;

    private ArtesaoService artesaoService;

    @BeforeEach
    void setUp() {
        artesaoService = new ArtesaoService(artesaoRepository);
    }

    @Test
    void buscarPerfilDoUsuarioAutenticado_deveRetornarArtesao() {
        Long idUsuario = 1L;
        Artesao artesao = mock(Artesao.class);

        when(artesaoRepository.findByUsuario_IdUsuario(idUsuario))
                .thenReturn(Optional.of(artesao));

        Artesao resultado = artesaoService.buscarPerfilDoUsuarioAutenticado(idUsuario);

        assertSame(artesao, resultado);
        verify(artesaoRepository).findByUsuario_IdUsuario(idUsuario);
    }

    @Test
    void buscarPerfilDoUsuarioAutenticado_deveLancarExcecaoQuandoNaoEncontrar() {
        Long idUsuario = 1L;

        when(artesaoRepository.findByUsuario_IdUsuario(idUsuario))
                .thenReturn(Optional.empty());

        assertThrows(
                ArtesaoNaoEncontradoException.class,
                () -> artesaoService.buscarPerfilDoUsuarioAutenticado(idUsuario)
        );

        verify(artesaoRepository).findByUsuario_IdUsuario(idUsuario);
    }

    @Test
    void atualizarPerfil_deveAtualizarDadosEDevolverArtesao() {
        Long idUsuario = 1L;

        Artesao artesao = mock(Artesao.class);
        ArtesaoAtualizacaoDTO dto = criarDto();

        when(artesaoRepository.findByUsuario_IdUsuario(idUsuario))
                .thenReturn(Optional.of(artesao));

        when(artesaoRepository.save(artesao))
                .thenReturn(artesao);

        Artesao resultado = artesaoService.atualizarPerfil(idUsuario, dto);

        assertSame(artesao, resultado);

        verify(artesao).setNomeExibicao("Artesanato da Lari");
        verify(artesao).setTituloCurto("Artesã");
        verify(artesao).setBiografia("Biografia de teste");
        verify(artesao).setLead("Peças feitas à mão");
        verify(artesao).setCidade("São Paulo");
        verify(artesao).setMembroDesde("2026");
        verify(artesao).setLocaisAtendimento("São Paulo");
        verify(artesao).setAvatarUrl("avatar.jpg");
        verify(artesao).setInstagram("@artesanato");
        verify(artesao).setWhatsappUrl("https://wa.me/5511999999999");
        verify(artesao).setFacebookUrl("facebook.com/artesanato");

        verify(artesaoRepository).save(artesao);
    }

    @Test
    void atualizarPerfil_deveLancarExcecaoQuandoArtesaoNaoExistir() {
        Long idUsuario = 1L;
        ArtesaoAtualizacaoDTO dto = criarDto();

        when(artesaoRepository.findByUsuario_IdUsuario(idUsuario))
                .thenReturn(Optional.empty());

        assertThrows(
                ArtesaoNaoEncontradoException.class,
                () -> artesaoService.atualizarPerfil(idUsuario, dto)
        );

        verify(artesaoRepository, never()).save(any());
    }

    private ArtesaoAtualizacaoDTO criarDto() {
        ArtesaoAtualizacaoDTO dto = new ArtesaoAtualizacaoDTO();

        dto.setNomeExibicao("Artesanato da Lari");
        dto.setTituloCurto("Artesã");
        dto.setBiografia("Biografia de teste");
        dto.setLead("Peças feitas à mão");
        dto.setCidade("São Paulo");
        dto.setMembroDesde("2026");
        dto.setLocaisAtendimento("São Paulo");
        dto.setAvatarUrl("avatar.jpg");
        dto.setInstagram("@artesanato");
        dto.setWhatsappUrl("https://wa.me/5511999999999");
        dto.setFacebookUrl("facebook.com/artesanato");

        return dto;
    }
}

