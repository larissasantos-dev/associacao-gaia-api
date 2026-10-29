package br.edu.ifsp.associacaogaia.service;

import br.edu.ifsp.associacaogaia.dto.SolicitacaoArtesaoRequestDTO;
import br.edu.ifsp.associacaogaia.exception.PrazoReenvioNaoCumpridoException;
import br.edu.ifsp.associacaogaia.exception.SolicitacaoJaPendenteException;
import br.edu.ifsp.associacaogaia.exception.TipoUsuarioNaoPodeSolicitarException;
import br.edu.ifsp.associacaogaia.exception.UsuarioNaoEncontradoException;
import br.edu.ifsp.associacaogaia.model.SolicitacaoArtesao;
import br.edu.ifsp.associacaogaia.model.StatusSolicitacao;
import br.edu.ifsp.associacaogaia.model.TipoUsuario;
import br.edu.ifsp.associacaogaia.model.Usuario;
import br.edu.ifsp.associacaogaia.repository.SolicitacaoArtesaoRepository;
import br.edu.ifsp.associacaogaia.repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SolicitacaoArtesaoServiceTest {

    private static final long PRAZO_DIAS = 7;

    @Mock
    private SolicitacaoArtesaoRepository solicitacaoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    private SolicitacaoArtesaoService service;

    @BeforeEach
    void setUp() {
        service = new SolicitacaoArtesaoService(solicitacaoRepository, usuarioRepository, PRAZO_DIAS);
    }

    // ---------- CRIAR SOLICITAÇÃO ----------

    @Test
    void criarSolicitacao_comVisitanteSemHistorico_deveCriarPendente() {
        Usuario visitante = criarUsuario(TipoUsuario.VISITANTE);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(visitante));
        when(solicitacaoRepository.existsByUsuario_IdUsuarioAndStatus(1L, StatusSolicitacao.PENDENTE))
                .thenReturn(false);
        when(solicitacaoRepository.findFirstByUsuario_IdUsuarioOrderByDataSolicitacaoDesc(1L))
                .thenReturn(Optional.empty());
        when(solicitacaoRepository.save(any(SolicitacaoArtesao.class)))
                .thenAnswer(chamada -> chamada.getArgument(0));

        SolicitacaoArtesao resultado = service.criarSolicitacao(1L, criarDados());

        assertEquals(StatusSolicitacao.PENDENTE, resultado.getStatus());
        assertNotNull(resultado.getDataSolicitacao());
        assertEquals("Ana Silva", resultado.getNomeExibicao());
        assertSame(visitante, resultado.getUsuario());
    }

    @Test
    void criarSolicitacao_comUsuarioInexistente_deveLancarExcecao() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(UsuarioNaoEncontradoException.class,
                () -> service.criarSolicitacao(99L, criarDados()));
    }

    @Test
    void criarSolicitacao_comUsuarioArtesao_deveLancarExcecao() {
        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(criarUsuario(TipoUsuario.ARTESAO)));

        assertThrows(TipoUsuarioNaoPodeSolicitarException.class,
                () -> service.criarSolicitacao(1L, criarDados()));
    }

    @Test
    void criarSolicitacao_comUsuarioAdministrador_deveLancarExcecao() {
        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(criarUsuario(TipoUsuario.ADMINISTRADOR)));

        assertThrows(TipoUsuarioNaoPodeSolicitarException.class,
                () -> service.criarSolicitacao(1L, criarDados()));
    }

    @Test
    void criarSolicitacao_comSolicitacaoPendente_deveLancarExcecaoESemSalvar() {
        when(usuarioRepository.findById(1L))
                .thenReturn(Optional.of(criarUsuario(TipoUsuario.VISITANTE)));
        when(solicitacaoRepository.existsByUsuario_IdUsuarioAndStatus(1L, StatusSolicitacao.PENDENTE))
                .thenReturn(true);

        assertThrows(SolicitacaoJaPendenteException.class,
                () -> service.criarSolicitacao(1L, criarDados()));

        verify(solicitacaoRepository, never()).save(any());
    }

    @Test
    void criarSolicitacao_rejeitadaDentroDoPrazo_deveLancarExcecaoESemSalvar() {
        Usuario visitante = criarUsuario(TipoUsuario.VISITANTE);
        SolicitacaoArtesao rejeitada = criarRejeitada(visitante, LocalDateTime.now().minusDays(5));

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(visitante));
        when(solicitacaoRepository.existsByUsuario_IdUsuarioAndStatus(1L, StatusSolicitacao.PENDENTE))
                .thenReturn(false);
        when(solicitacaoRepository.findFirstByUsuario_IdUsuarioOrderByDataSolicitacaoDesc(1L))
                .thenReturn(Optional.of(rejeitada));

        assertThrows(PrazoReenvioNaoCumpridoException.class,
                () -> service.criarSolicitacao(1L, criarDados()));

        verify(solicitacaoRepository, never()).save(any());
    }

    @Test
    void criarSolicitacao_rejeitadaForaDoPrazo_deveCriarNovaPendente() {
        Usuario visitante = criarUsuario(TipoUsuario.VISITANTE);
        SolicitacaoArtesao rejeitada = criarRejeitada(visitante, LocalDateTime.now().minusDays(PRAZO_DIAS + 1));

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(visitante));
        when(solicitacaoRepository.existsByUsuario_IdUsuarioAndStatus(1L, StatusSolicitacao.PENDENTE))
                .thenReturn(false);
        when(solicitacaoRepository.findFirstByUsuario_IdUsuarioOrderByDataSolicitacaoDesc(1L))
                .thenReturn(Optional.of(rejeitada));
        when(solicitacaoRepository.save(any(SolicitacaoArtesao.class)))
                .thenAnswer(chamada -> chamada.getArgument(0));

        SolicitacaoArtesao resultado = service.criarSolicitacao(1L, criarDados());

        assertEquals(StatusSolicitacao.PENDENTE, resultado.getStatus());
    }

    @Test
    void dataLiberacaoReenvio_solicitacaoPendente_deveRetornarNull() {
        SolicitacaoArtesao pendente = new SolicitacaoArtesao(
                criarUsuario(TipoUsuario.VISITANTE), "j", "Ana", "Ceramista", "Tatui");

        assertNull(service.dataLiberacaoReenvio(pendente));
    }

    @Test
    void dataLiberacaoReenvio_solicitacaoRejeitada_deveSomarOPrazoNaDataDaResposta() {
        LocalDateTime resposta = LocalDateTime.of(2026, 10, 1, 10, 0);
        SolicitacaoArtesao rejeitada = criarRejeitada(criarUsuario(TipoUsuario.VISITANTE), resposta);

        assertEquals(resposta.plusDays(PRAZO_DIAS), service.dataLiberacaoReenvio(rejeitada));
    }

    private Usuario criarUsuario(TipoUsuario tipo) {
        return new Usuario("Teste", "teste@teste.com", "senha", "11999999999", tipo);
    }

    private SolicitacaoArtesaoRequestDTO criarDados() {
        SolicitacaoArtesaoRequestDTO dados = new SolicitacaoArtesaoRequestDTO();
        dados.setJustificativa("Quero expor meus trabalhos");
        dados.setNomeExibicao("Ana Silva");
        dados.setTituloCurto("Ceramista");
        dados.setCidade("Tatui");
        return dados;
    }

    /**
     * A entidade ainda não tem aprovar()/rejeitar() (vêm na tarefa de
     * aprovação), então o estado "rejeitada" é montado por reflexão.
     * Quando esses métodos existirem, dá pra usá-los aqui no lugar.
     */
    private SolicitacaoArtesao criarRejeitada(Usuario usuario, LocalDateTime dataResposta) {
        SolicitacaoArtesao s = new SolicitacaoArtesao(usuario, "j", "Ana", "Ceramista", "Tatui");
        ReflectionTestUtils.setField(s, "status", StatusSolicitacao.REJEITADA);
        ReflectionTestUtils.setField(s, "dataResposta", dataResposta);
        return s;
    }
}