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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class SolicitacaoArtesaoService {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private final SolicitacaoArtesaoRepository solicitacaoRepository;
    private final UsuarioRepository usuarioRepository;
    private final long prazoReenvioDias;

    public SolicitacaoArtesaoService(
            SolicitacaoArtesaoRepository solicitacaoRepository,
            UsuarioRepository usuarioRepository,
            @Value("${gaia.solicitacao.prazo-reenvio-dias:30}") long prazoReenvioDias){
        this.solicitacaoRepository = solicitacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.prazoReenvioDias = prazoReenvioDias;
    }

    /**
     * Cria uma solicitação PENDENTE para o usuário informado.
     *
     * @throws UsuarioNaoEncontradoException       se o usuário não existe
     * @throws TipoUsuarioNaoPodeSolicitarException se não é VISITANTE
     * @throws SolicitacaoJaPendenteException       se já tem uma PENDENTE
     * @throws PrazoReenvioNaoCumpridoException     se a última foi rejeitada há
     *                                              menos tempo que o prazo
     */

    @Transactional
    public SolicitacaoArtesao criarSolicitacao(Long idUsuario, SolicitacaoArtesaoRequestDTO dados) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado."));

        if (usuario.getTipoUsuario() != TipoUsuario.VISITANTE) {
            throw new TipoUsuarioNaoPodeSolicitarException(
                    "Apenas visitantes podem solicitar para se tornar artesão.");
        }

        if (solicitacaoRepository.existsByUsuario_IdUsuarioAndStatus(idUsuario, StatusSolicitacao.PENDENTE)) {
            throw new SolicitacaoJaPendenteException(
                    "Você já possui uma solicitação em análise.");
        }

        Optional<SolicitacaoArtesao> ultima =
                solicitacaoRepository.findFirstByUsuario_IdUsuarioOrderByDataSolicitacaoDesc(idUsuario);

        if (ultima.isPresent()) {
            LocalDateTime liberaEm = dataLiberacaoReenvio(ultima.get());
            if (liberaEm != null && LocalDateTime.now().isBefore(liberaEm)) {
                throw new PrazoReenvioNaoCumpridoException(
                        "Sua última solicitação foi rejeitada. Você poderá solicitar novamente a partir de "
                                + liberaEm.format(FORMATO_DATA) + ".");
            }
        }

        SolicitacaoArtesao solicitacao = new SolicitacaoArtesao(
                usuario,
                dados.getJustificativa(),
                dados.getNomeExibicao(),
                dados.getTituloCurto(),
                dados.getCidade()
        );
        solicitacao.setBiografia(dados.getBiografia());
        solicitacao.setInstagram(dados.getInstagram());
        solicitacao.setWhatsappUrl(dados.getWhatsappUrl());

        return solicitacaoRepository.save(solicitacao);
    }

    public Optional<SolicitacaoArtesao> buscarUltimaDoUsuario(Long idUsuario) {
        return solicitacaoRepository.findFirstByUsuario_IdUsuarioOrderByDataSolicitacaoDesc(idUsuario);
    }

    public LocalDateTime dataLiberacaoReenvio(SolicitacaoArtesao solicitacao) {
        if (solicitacao.getStatus() != StatusSolicitacao.REJEITADA || solicitacao.getDataResposta() == null) {
            return null;
        }
        return solicitacao.getDataResposta().plusDays(prazoReenvioDias);
    }
}
