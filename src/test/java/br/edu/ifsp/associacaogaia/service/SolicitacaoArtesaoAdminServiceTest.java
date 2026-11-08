
package br.edu.ifsp.associacaogaia.service;

import br.edu.ifsp.associacaogaia.exception.SolicitacaoJaRespondidaException;
import br.edu.ifsp.associacaogaia.exception.SolicitacaoNaoEncontradaException;
import br.edu.ifsp.associacaogaia.exception.UsuarioNaoEhVisitanteException;
import br.edu.ifsp.associacaogaia.model.Artesao;
import br.edu.ifsp.associacaogaia.model.SolicitacaoArtesao;
import br.edu.ifsp.associacaogaia.model.StatusSolicitacao;
import br.edu.ifsp.associacaogaia.model.TipoUsuario;
import br.edu.ifsp.associacaogaia.model.Usuario;
import br.edu.ifsp.associacaogaia.repository.ArtesaoRepository;
import br.edu.ifsp.associacaogaia.repository.SolicitacaoArtesaoRepository;
import br.edu.ifsp.associacaogaia.repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolicitacaoArtesaoAdminServiceTest {

    @Mock
    private SolicitacaoArtesaoRepository solicitacaoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ArtesaoRepository artesaoRepository;

    private SolicitacaoArtesaoAdminService service;

    @BeforeEach
    void setUp() {
        service = new SolicitacaoArtesaoAdminService(
                solicitacaoRepository,
                usuarioRepository,
                artesaoRepository
        );
    }

    // ---------- LISTAGEM ----------

    @Test
    void listar_comStatus_deveRetornarSolicitacoesFiltradas() {
        SolicitacaoArtesao solicitacao = criarSolicitacao();

        when(solicitacaoRepository.findByStatusOrderByDataSolicitacaoAsc(
                StatusSolicitacao.PENDENTE
        )).thenReturn(List.of(solicitacao));

        List<SolicitacaoArtesao> resultado =
                service.listar(StatusSolicitacao.PENDENTE);

        assertEquals(1, resultado.size());
        assertSame(solicitacao, resultado.get(0));

        verify(solicitacaoRepository)
                .findByStatusOrderByDataSolicitacaoAsc(StatusSolicitacao.PENDENTE);

        verify(solicitacaoRepository, never())
                .findAllByOrderByDataSolicitacaoDesc();
    }

    @Test
    void listar_semStatus_deveRetornarTodasAsSolicitacoes() {
        SolicitacaoArtesao solicitacao = criarSolicitacao();

        when(solicitacaoRepository.findAllByOrderByDataSolicitacaoDesc())
                .thenReturn(List.of(solicitacao));

        List<SolicitacaoArtesao> resultado = service.listar(null);

        assertEquals(1, resultado.size());
        assertSame(solicitacao, resultado.get(0));

        verify(solicitacaoRepository)
                .findAllByOrderByDataSolicitacaoDesc();

        verify(solicitacaoRepository, never())
                .findByStatusOrderByDataSolicitacaoAsc(any());
    }

    // ---------- APROVAÇÃO ----------

    @Test
    void aprovar_comSolicitacaoPendente_deveCriarPerfilEAlterarUsuario() {
        Long id = 1L;
        Usuario usuario = criarUsuario();
        SolicitacaoArtesao solicitacao = criarSolicitacao(usuario);

        when(solicitacaoRepository.findById(id))
                .thenReturn(Optional.of(solicitacao));

        when(artesaoRepository.findByUsuario_IdUsuario(any()))
                .thenReturn(Optional.empty());

        when(artesaoRepository.save(any(Artesao.class)))
                .thenAnswer(chamada -> chamada.getArgument(0));

        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(chamada -> chamada.getArgument(0));

        when(solicitacaoRepository.save(solicitacao))
                .thenReturn(solicitacao);

        SolicitacaoArtesao resultado = service.aprovar(id);

        assertSame(solicitacao, resultado);
        assertEquals(StatusSolicitacao.APROVADA, solicitacao.getStatus());
        assertEquals(TipoUsuario.ARTESAO, usuario.getTipoUsuario());
        assertNotNull(solicitacao.getDataResposta());

        verify(artesaoRepository).save(any(Artesao.class));
        verify(usuarioRepository).save(usuario);
        verify(solicitacaoRepository).save(solicitacao);
    }

    @Test
    void aprovar_comIdInexistente_deveLancarExcecao() {
        when(solicitacaoRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                SolicitacaoNaoEncontradaException.class,
                () -> service.aprovar(999L)
        );

        verify(artesaoRepository, never()).save(any());
        verify(usuarioRepository, never()).save(any());
        verify(solicitacaoRepository, never()).save(any());
    }

    @Test
    void aprovar_comSolicitacaoJaRespondida_deveLancarExcecao() {
        SolicitacaoArtesao solicitacao = criarSolicitacao();
        solicitacao.aprovar();

        when(solicitacaoRepository.findById(1L))
                .thenReturn(Optional.of(solicitacao));

        assertThrows(
                SolicitacaoJaRespondidaException.class,
                () -> service.aprovar(1L)
        );

        verify(artesaoRepository, never()).save(any());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void aprovar_comUsuarioQueNaoEhVisitante_deveLancarExcecao() {
        Usuario usuario = new Usuario(
                "Artesao",
                "artesao@teste.com",
                "senha",
                "11999999999",
                TipoUsuario.ARTESAO
        );

        SolicitacaoArtesao solicitacao = criarSolicitacao(usuario);

        when(solicitacaoRepository.findById(1L))
                .thenReturn(Optional.of(solicitacao));

        assertThrows(
                UsuarioNaoEhVisitanteException.class,
                () -> service.aprovar(1L)
        );

        verify(artesaoRepository, never()).save(any());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void aprovar_comUsuarioQueJaPossuiPerfil_deveLancarExcecao() {
        Usuario usuario = criarUsuario();
        SolicitacaoArtesao solicitacao = criarSolicitacao(usuario);

        when(solicitacaoRepository.findById(1L))
                .thenReturn(Optional.of(solicitacao));

        when(artesaoRepository.findByUsuario_IdUsuario(any()))
                .thenReturn(Optional.of(mock(Artesao.class)));

        assertThrows(
                UsuarioNaoEhVisitanteException.class,
                () -> service.aprovar(1L)
        );

        verify(artesaoRepository, never()).save(any());
        verify(usuarioRepository, never()).save(any());
    }

    // ---------- REJEIÇÃO ----------

    @Test
    void rejeitar_comSolicitacaoPendente_deveRegistrarMotivoEAlterarStatus() {
        SolicitacaoArtesao solicitacao = criarSolicitacao();
        String motivo = "Informações insuficientes.";

        when(solicitacaoRepository.findById(1L))
                .thenReturn(Optional.of(solicitacao));

        when(solicitacaoRepository.save(solicitacao))
                .thenReturn(solicitacao);

        SolicitacaoArtesao resultado = service.rejeitar(1L, motivo);

        assertSame(solicitacao, resultado);
        assertEquals(StatusSolicitacao.REJEITADA, resultado.getStatus());
        assertEquals(motivo, resultado.getMotivoRejeicao());
        assertNotNull(resultado.getDataResposta());

        verify(solicitacaoRepository).save(solicitacao);
        verify(usuarioRepository, never()).save(any());
        verify(artesaoRepository, never()).save(any());
    }

    @Test
    void rejeitar_deveRemoverEspacosDoMotivo() {
        SolicitacaoArtesao solicitacao = criarSolicitacao();

        when(solicitacaoRepository.findById(1L))
                .thenReturn(Optional.of(solicitacao));

        when(solicitacaoRepository.save(solicitacao))
                .thenReturn(solicitacao);

        service.rejeitar(1L, "  Dados insuficientes.  ");

        assertEquals(
                "Dados insuficientes.",
                solicitacao.getMotivoRejeicao()
        );
    }

    @Test
    void rejeitar_comIdInexistente_deveLancarExcecao() {
        when(solicitacaoRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                SolicitacaoNaoEncontradaException.class,
                () -> service.rejeitar(999L, "Motivo de rejeição")
        );

        verify(solicitacaoRepository, never()).save(any());
    }

    @Test
    void rejeitar_comSolicitacaoJaRespondida_deveLancarExcecao() {
        SolicitacaoArtesao solicitacao = criarSolicitacao();
        solicitacao.rejeitar("Já rejeitada.");

        when(solicitacaoRepository.findById(1L))
                .thenReturn(Optional.of(solicitacao));

        assertThrows(
                SolicitacaoJaRespondidaException.class,
                () -> service.rejeitar(1L, "Novo motivo")
        );

        verify(solicitacaoRepository, never()).save(any());
    }

    // ---------- HELPERS ----------

    private Usuario criarUsuario() {
        return new Usuario(
                "Larissa",
                "larissa@teste.com",
                "senha",
                "11999999999",
                TipoUsuario.VISITANTE
        );
    }

    private SolicitacaoArtesao criarSolicitacao() {
        return criarSolicitacao(criarUsuario());
    }

    private SolicitacaoArtesao criarSolicitacao(Usuario usuario) {
        return new SolicitacaoArtesao(
                usuario,
                "Quero participar da associação",
                "Artesanato da Lari",
                "Artesã",
                "São Paulo"
        );
    }
}
