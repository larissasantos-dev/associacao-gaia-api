package br.edu.ifsp.associacaogaia.service;

import br.edu.ifsp.associacaogaia.exception.SolicitacaoJaRespondidaException;
import br.edu.ifsp.associacaogaia.exception.SolicitacaoNaoEncontradaException;
import br.edu.ifsp.associacaogaia.exception.UsuarioNaoEhVisitanteException;
import br.edu.ifsp.associacaogaia.model.SolicitacaoArtesao;
import br.edu.ifsp.associacaogaia.model.StatusSolicitacao;
import br.edu.ifsp.associacaogaia.model.TipoUsuario;
import br.edu.ifsp.associacaogaia.model.Usuario;
import br.edu.ifsp.associacaogaia.repository.ArtesaoRepository;
import br.edu.ifsp.associacaogaia.repository.SolicitacaoArtesaoRepository;
import br.edu.ifsp.associacaogaia.repository.UsuarioRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Regras do lado do administrador: listar, aprovar e rejeitar solicitações
 * para virar artesão. A autorização (só ADMINISTRADOR) é feita no controller.
 */
@Service
public class SolicitacaoArtesaoAdminService {

    private final SolicitacaoArtesaoRepository solicitacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ArtesaoRepository artesaoRepository;

    public SolicitacaoArtesaoAdminService(
            SolicitacaoArtesaoRepository solicitacaoRepository,
            UsuarioRepository usuarioRepository,
            ArtesaoRepository artesaoRepository) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.artesaoRepository = artesaoRepository;
    }

    public List<SolicitacaoArtesao> listar(StatusSolicitacao status) {
        if (status != null) {
            return solicitacaoRepository.findByStatusOrderByDataSolicitacaoAsc(status);
        }
        return solicitacaoRepository.findAllByOrderByDataSolicitacaoDesc();
    }

    /**
     * Aprova a solicitação: cria o Artesao a partir dos dados dela, muda o
     * usuário para ARTESAO e marca a solicitação como APROVADA. Tudo na mesma
     * transação: se qualquer passo falhar, nada é gravado.
     *
     * @throws SolicitacaoNaoEncontradaException se o id não existe
     * @throws SolicitacaoJaRespondidaException  se não está mais PENDENTE
     * @throws UsuarioNaoEhVisitanteException    se o usuário deixou de ser
     *                                           VISITANTE ou já tem perfil de artesão
     */
    @Transactional
    public SolicitacaoArtesao aprovar(Long idSolicitacao) {
        SolicitacaoArtesao solicitacao = buscarPendente(idSolicitacao);
        Usuario usuario = solicitacao.getUsuario();

        if (usuario.getTipoUsuario() != TipoUsuario.VISITANTE) {
            throw new UsuarioNaoEhVisitanteException(
                    "O usuário desta solicitação não é mais um visitante.");
        }

        if (artesaoRepository.findByUsuario_IdUsuario(usuario.getIdUsuario()).isPresent()) {
            throw new UsuarioNaoEhVisitanteException(
                    "Este usuário já possui um perfil de artesão.");
        }

        artesaoRepository.save(solicitacao.criarPerfilArtesao());

        usuario.setTipoUsuario(TipoUsuario.ARTESAO);
        usuarioRepository.save(usuario);

        solicitacao.aprovar();
        return solicitacaoRepository.save(solicitacao);
    }

    /**
     * Rejeita a solicitação, guardando o motivo. A data da resposta é o que
     * começa a contar o prazo para o usuário solicitar de novo.
     *
     * @throws SolicitacaoNaoEncontradaException se o id não existe
     * @throws SolicitacaoJaRespondidaException  se não está mais PENDENTE
     */
    @Transactional
    public SolicitacaoArtesao rejeitar(Long idSolicitacao, String motivo) {
        SolicitacaoArtesao solicitacao = buscarPendente(idSolicitacao);
        solicitacao.rejeitar(motivo.trim());
        return solicitacaoRepository.save(solicitacao);
    }

    private SolicitacaoArtesao buscarPendente(Long idSolicitacao) {
        SolicitacaoArtesao solicitacao = solicitacaoRepository.findById(idSolicitacao)
                .orElseThrow(() -> new SolicitacaoNaoEncontradaException("Solicitação não encontrada."));

        if (solicitacao.getStatus() != StatusSolicitacao.PENDENTE) {
            throw new SolicitacaoJaRespondidaException("Esta solicitação já foi respondida.");
        }

        return solicitacao;
    }
}