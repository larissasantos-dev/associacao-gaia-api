package br.edu.ifsp.associacaogaia.service;

import br.edu.ifsp.associacaogaia.dto.TrabalhoCadastroDTO;
import br.edu.ifsp.associacaogaia.exception.ArtesaoNaoEncontradoException;
import br.edu.ifsp.associacaogaia.exception.LimiteTrabalhosException;
import br.edu.ifsp.associacaogaia.exception.TrabalhoNaoEncontradoException;
import br.edu.ifsp.associacaogaia.model.Artesao;
import br.edu.ifsp.associacaogaia.model.Trabalho;
import br.edu.ifsp.associacaogaia.repository.ArtesaoRepository;
import br.edu.ifsp.associacaogaia.repository.HistoricoPrecoRepository;
import br.edu.ifsp.associacaogaia.repository.TrabalhoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.access.AccessDeniedException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrabalhoServiceTest {

    @Mock
    private TrabalhoRepository trabalhoRepository;

    @Mock
    private ArtesaoRepository artesaoRepository;

    @Mock
    private HistoricoPrecoRepository historicoPrecoRepository;

    @Mock
    private ImagemStorageService imagemStorageService;

    private TrabalhoService trabalhoService;

    @BeforeEach
    void setUp() {
        trabalhoService = new TrabalhoService(
                trabalhoRepository,
                artesaoRepository,
                historicoPrecoRepository,
                imagemStorageService
        );
    }

    @Test
    void cadastrarTrabalho_deveCadastrarTrabalhoParaArtesao() {
        Long idUsuario = 1L;

        Artesao artesao = mock(Artesao.class);
        TrabalhoCadastroDTO dto = criarDto();

        when(artesaoRepository.findByUsuario_IdUsuario(idUsuario))
                .thenReturn(Optional.of(artesao));

        when(trabalhoRepository.save(any(Trabalho.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Trabalho resultado = trabalhoService.cadastrarTrabalho(dto, idUsuario);

        assertNotNull(resultado);
        assertSame(artesao, resultado.getArtesao());
        assertEquals("imagem.jpg", resultado.getImagemUrl());
        assertEquals("Peça artesanal", resultado.getLegenda());
        assertEquals("Descrição da peça", resultado.getDescricao());
        assertEquals("Cerâmica", resultado.getCategoria());
        assertNotNull(resultado.getDataPublicacao());

        verify(artesaoRepository).findByUsuario_IdUsuario(idUsuario);
        verify(trabalhoRepository).save(any(Trabalho.class));
    }

    @Test
    void cadastrarTrabalho_deveLancarExcecaoQuandoArtesaoNaoExistir() {
        Long idUsuario = 1L;
        TrabalhoCadastroDTO dto = criarDto();

        when(artesaoRepository.findByUsuario_IdUsuario(idUsuario))
                .thenReturn(Optional.empty());

        assertThrows(
                ArtesaoNaoEncontradoException.class,
                () -> trabalhoService.cadastrarTrabalho(dto, idUsuario)
        );

        verify(trabalhoRepository, never()).save(any());
    }

    @Test
    void listarTrabalhosDoArtesao_deveRetornarTrabalhos() {
        Long idUsuario = 1L;
        Long idArtesao = 10L;

        Artesao artesao = mock(Artesao.class);

        Trabalho trabalho1 = new Trabalho();
        Trabalho trabalho2 = new Trabalho();

        when(artesao.getIdArtesao()).thenReturn(idArtesao);

        when(artesaoRepository.findByUsuario_IdUsuario(idUsuario))
                .thenReturn(Optional.of(artesao));

        when(trabalhoRepository
                .findByArtesao_IdArtesaoOrderByDataPublicacaoDesc(idArtesao))
                .thenReturn(List.of(trabalho1, trabalho2));

        List<Trabalho> resultado =
                trabalhoService.listarTrabalhosDoArtesao(idUsuario);

        assertEquals(2, resultado.size());
        assertEquals(trabalho1, resultado.get(0));
        assertEquals(trabalho2, resultado.get(1));

        verify(trabalhoRepository)
                .findByArtesao_IdArtesaoOrderByDataPublicacaoDesc(idArtesao);
    }

    @Test
    void listarTrabalhosDoArtesao_deveLancarExcecaoQuandoArtesaoNaoExistir() {
        Long idUsuario = 1L;

        when(artesaoRepository.findByUsuario_IdUsuario(idUsuario))
                .thenReturn(Optional.empty());

        assertThrows(
                ArtesaoNaoEncontradoException.class,
                () -> trabalhoService.listarTrabalhosDoArtesao(idUsuario)
        );

        verify(trabalhoRepository, never())
                .findByArtesao_IdArtesaoOrderByDataPublicacaoDesc(anyLong());
    }

    @Test
    void atualizarTrabalho_deveAtualizarQuandoUsuarioForDono() {
        Long idUsuario = 1L;
        Long idArtesao = 10L;
        Long idTrabalho = 20L;

        Artesao artesao = mock(Artesao.class);
        Trabalho trabalho = new Trabalho();
        TrabalhoCadastroDTO dto = criarDto();

        trabalho.setArtesao(artesao);

        when(artesao.getIdArtesao()).thenReturn(idArtesao);

        when(artesaoRepository.findByUsuario_IdUsuario(idUsuario))
                .thenReturn(Optional.of(artesao));

        when(trabalhoRepository.findById(idTrabalho))
                .thenReturn(Optional.of(trabalho));

        when(trabalhoRepository.save(trabalho))
                .thenReturn(trabalho);

        Trabalho resultado =
                trabalhoService.atualizarTrabalho(idTrabalho, dto, idUsuario);

        assertSame(trabalho, resultado);
        assertEquals("imagem.jpg", resultado.getImagemUrl());
        assertEquals("Peça artesanal", resultado.getLegenda());
        assertEquals("Descrição da peça", resultado.getDescricao());
        assertEquals("Cerâmica", resultado.getCategoria());

        verify(trabalhoRepository).save(trabalho);
    }

    @Test
    void atualizarTrabalho_deveLancarExcecaoQuandoTrabalhoNaoExistir() {
        Long idUsuario = 1L;
        Long idTrabalho = 20L;
        TrabalhoCadastroDTO dto = criarDto();

        when(trabalhoRepository.findById(idTrabalho))
                .thenReturn(Optional.empty());

        assertThrows(
                TrabalhoNaoEncontradoException.class,
                () -> trabalhoService.atualizarTrabalho(idTrabalho, dto, idUsuario)
        );

        verify(trabalhoRepository, never()).save(any());
    }

    @Test
    void atualizarTrabalho_deveNegarQuandoUsuarioNaoForDono() {
        Long idUsuario = 1L;
        Long idTrabalho = 20L;

        Artesao dono = mock(Artesao.class);
        Artesao outroArtesao = mock(Artesao.class);

        Trabalho trabalho = new Trabalho();
        trabalho.setArtesao(dono);

        TrabalhoCadastroDTO dto = criarDto();

        when(dono.getIdArtesao()).thenReturn(10L);
        when(outroArtesao.getIdArtesao()).thenReturn(20L);

        when(trabalhoRepository.findById(idTrabalho))
                .thenReturn(Optional.of(trabalho));

        when(artesaoRepository.findByUsuario_IdUsuario(idUsuario))
                .thenReturn(Optional.of(outroArtesao));

        assertThrows(
                AccessDeniedException.class,
                () -> trabalhoService.atualizarTrabalho(idTrabalho, dto, idUsuario)
        );

        verify(trabalhoRepository, never()).save(any());
    }

    @Test
    void atualizarTrabalho_deveLancarExcecaoQuandoArtesaoDoUsuarioNaoExistir() {
        Long idUsuario = 1L;
        Long idTrabalho = 20L;

        Artesao dono = mock(Artesao.class);
        Trabalho trabalho = new Trabalho();
        trabalho.setArtesao(dono);

        when(trabalhoRepository.findById(idTrabalho))
                .thenReturn(Optional.of(trabalho));

        when(artesaoRepository.findByUsuario_IdUsuario(idUsuario))
                .thenReturn(Optional.empty());

        assertThrows(
                ArtesaoNaoEncontradoException.class,
                () -> trabalhoService.atualizarTrabalho(
                        idTrabalho,
                        criarDto(),
                        idUsuario
                )
        );

        verify(trabalhoRepository, never()).save(any());
    }

    @Test
    void excluirTrabalho_deveExcluirQuandoUsuarioForDono() {
        Long idUsuario = 1L;
        Long idTrabalho = 20L;
        Long idArtesao = 10L;

        Artesao artesao = mock(Artesao.class);
        Trabalho trabalho = new Trabalho();

        trabalho.setArtesao(artesao);

        when(artesao.getIdArtesao()).thenReturn(idArtesao);

        when(trabalhoRepository.findById(idTrabalho))
                .thenReturn(Optional.of(trabalho));

        when(artesaoRepository.findByUsuario_IdUsuario(idUsuario))
                .thenReturn(Optional.of(artesao));

        trabalhoService.excluirTrabalho(idTrabalho, idUsuario);

        verify(trabalhoRepository).delete(trabalho);
    }

    @Test
    void excluirTrabalho_deveLancarExcecaoQuandoTrabalhoNaoExistir() {
        Long idUsuario = 1L;
        Long idTrabalho = 20L;

        when(trabalhoRepository.findById(idTrabalho))
                .thenReturn(Optional.empty());

        assertThrows(
                TrabalhoNaoEncontradoException.class,
                () -> trabalhoService.excluirTrabalho(idTrabalho, idUsuario)
        );

        verify(trabalhoRepository, never()).delete(any());
    }

    @Test
    void excluirTrabalho_deveNegarQuandoUsuarioNaoForDono() {
        Long idUsuario = 1L;
        Long idTrabalho = 20L;

        Artesao dono = mock(Artesao.class);
        Artesao outroArtesao = mock(Artesao.class);

        Trabalho trabalho = new Trabalho();
        trabalho.setArtesao(dono);

        when(dono.getIdArtesao()).thenReturn(10L);
        when(outroArtesao.getIdArtesao()).thenReturn(20L);

        when(trabalhoRepository.findById(idTrabalho))
                .thenReturn(Optional.of(trabalho));

        when(artesaoRepository.findByUsuario_IdUsuario(idUsuario))
                .thenReturn(Optional.of(outroArtesao));

        assertThrows(
                AccessDeniedException.class,
                () -> trabalhoService.excluirTrabalho(idTrabalho, idUsuario)
        );

        verify(trabalhoRepository, never()).delete(any());
    }

    private TrabalhoCadastroDTO criarDto() {
        TrabalhoCadastroDTO dto = new TrabalhoCadastroDTO();

        dto.setImagemUrl("imagem.jpg");
        dto.setLegenda("Peça artesanal");
        dto.setDescricao("Descrição da peça");
        dto.setCategoria("Cerâmica");

        return dto;
    }

    @Test
    void cadastrarTrabalho_deveLancarExcecaoQuandoArtesaoJaAtingiuOLimite() {
        Long idUsuario = 1L;

        Artesao artesao = mock(Artesao.class);
        when(artesao.getIdArtesao()).thenReturn(10L);

        when(artesaoRepository.findByUsuario_IdUsuario(idUsuario))
                .thenReturn(Optional.of(artesao));
        when(trabalhoRepository.countByArtesao_IdArtesao(10L)).thenReturn(8L);

        assertThrows(LimiteTrabalhosException.class,
                () -> trabalhoService.cadastrarTrabalho(criarDto(), idUsuario));

        verify(trabalhoRepository, never()).save(any(Trabalho.class));
    }

    @Test
    void cadastrarTrabalho_devePermitirQuandoAindaHaVagaNoLimite() {
        Long idUsuario = 1L;

        Artesao artesao = mock(Artesao.class);
        when(artesao.getIdArtesao()).thenReturn(10L);

        when(artesaoRepository.findByUsuario_IdUsuario(idUsuario))
                .thenReturn(Optional.of(artesao));
        when(trabalhoRepository.countByArtesao_IdArtesao(10L)).thenReturn(7L);
        when(trabalhoRepository.save(any(Trabalho.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assertNotNull(trabalhoService.cadastrarTrabalho(criarDto(), idUsuario));
    }

    @Test
    void cadastrarTrabalho_deveGuardarOPrecoInformado() {
        Long idUsuario = 1L;

        Artesao artesao = mock(Artesao.class);
        TrabalhoCadastroDTO dto = criarDto();
        dto.setPreco(new BigDecimal("89.9"));

        when(artesaoRepository.findByUsuario_IdUsuario(idUsuario))
                .thenReturn(Optional.of(artesao));
        when(trabalhoRepository.save(any(Trabalho.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Trabalho resultado = trabalhoService.cadastrarTrabalho(dto, idUsuario);

        assertEquals(new BigDecimal("89.90"), resultado.getPreco());
    }

    @Test
    void atualizarPreco_deveRegistrarHistoricoQuandoOPrecoMuda() {
        Long idUsuario = 1L;

        Artesao artesao = mock(Artesao.class);
        when(artesao.getIdArtesao()).thenReturn(10L);

        Trabalho trabalho = new Trabalho();
        trabalho.setArtesao(artesao);
        trabalho.alterarPreco(new BigDecimal("50.00"));

        when(trabalhoRepository.findById(5L)).thenReturn(Optional.of(trabalho));
        when(artesaoRepository.findByUsuario_IdUsuario(idUsuario)).thenReturn(Optional.of(artesao));
        when(trabalhoRepository.save(any(Trabalho.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Trabalho resultado = trabalhoService.atualizarPreco(5L, new BigDecimal("65.00"), idUsuario);

        assertEquals(new BigDecimal("65.00"), resultado.getPreco());
        // 1ª entrada: criação do preço (null → 50); 2ª: 50 → 65
        assertEquals(2, resultado.getHistoricoPrecos().size());
        assertEquals(new BigDecimal("50.00"), resultado.getHistoricoPrecos().get(1).getPrecoAnterior());
        assertEquals(new BigDecimal("65.00"), resultado.getHistoricoPrecos().get(1).getPrecoNovo());
    }

    @Test
    void atualizarPreco_naoDeveRegistrarHistoricoQuandoOPrecoNaoMuda() {
        Long idUsuario = 1L;

        Artesao artesao = mock(Artesao.class);
        when(artesao.getIdArtesao()).thenReturn(10L);

        Trabalho trabalho = new Trabalho();
        trabalho.setArtesao(artesao);
        trabalho.alterarPreco(new BigDecimal("50.00"));

        when(trabalhoRepository.findById(5L)).thenReturn(Optional.of(trabalho));
        when(artesaoRepository.findByUsuario_IdUsuario(idUsuario)).thenReturn(Optional.of(artesao));
        when(trabalhoRepository.save(any(Trabalho.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Trabalho resultado = trabalhoService.atualizarPreco(5L, new BigDecimal("50"), idUsuario);

        assertEquals(1, resultado.getHistoricoPrecos().size());
    }

    @Test
    void atualizarPreco_deveNegarQuandoOTrabalhoEDeOutroArtesao() {
        Long idUsuario = 1L;

        Artesao dono = mock(Artesao.class);
        when(dono.getIdArtesao()).thenReturn(10L);
        Artesao logado = mock(Artesao.class);
        when(logado.getIdArtesao()).thenReturn(20L);

        Trabalho trabalho = new Trabalho();
        trabalho.setArtesao(dono);

        when(trabalhoRepository.findById(5L)).thenReturn(Optional.of(trabalho));
        when(artesaoRepository.findByUsuario_IdUsuario(idUsuario)).thenReturn(Optional.of(logado));

        assertThrows(AccessDeniedException.class,
                () -> trabalhoService.atualizarPreco(5L, new BigDecimal("10.00"), idUsuario));

        verify(trabalhoRepository, never()).save(any(Trabalho.class));
    }

    @Test
    void excluirTrabalhoComoAdministrador_deveExcluirSemVerificarDono() {
        Trabalho trabalho = new Trabalho();

        when(trabalhoRepository.findById(5L)).thenReturn(Optional.of(trabalho));

        trabalhoService.excluirTrabalhoComoAdministrador(5L);

        verify(trabalhoRepository).delete(trabalho);
        verifyNoInteractions(artesaoRepository);
    }
}
