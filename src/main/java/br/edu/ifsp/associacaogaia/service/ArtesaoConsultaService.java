package br.edu.ifsp.associacaogaia.service;

import br.edu.ifsp.associacaogaia.dto.ArtesaoAdminResponseDTO;
import br.edu.ifsp.associacaogaia.dto.ArtesaoPublicoDTO;
import br.edu.ifsp.associacaogaia.dto.TrabalhoResponseDTO;
import br.edu.ifsp.associacaogaia.exception.ArtesaoNaoEncontradoException;
import br.edu.ifsp.associacaogaia.model.Artesao;
import br.edu.ifsp.associacaogaia.repository.ArtesaoRepository;
import br.edu.ifsp.associacaogaia.repository.TrabalhoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Consultas de artesãos e de seus trabalhos.
 *
 * - Parte pública: só mostra artesãos com a conta ativa e nunca expõe dados
 *   da conta (e-mail, telefone).
 * - Parte administrativa: lista todos os artesãos, com e-mail, status e
 *   quantidade de trabalhos (tela "Artesãos" do painel).
 */
@Service
public class ArtesaoConsultaService {

    private final ArtesaoRepository artesaoRepository;
    private final TrabalhoRepository trabalhoRepository;

    public ArtesaoConsultaService(ArtesaoRepository artesaoRepository,
                                  TrabalhoRepository trabalhoRepository) {
        this.artesaoRepository = artesaoRepository;
        this.trabalhoRepository = trabalhoRepository;
    }

    /** Artesãos ativos, em ordem alfabética (catálogo público). */
    public List<ArtesaoPublicoDTO> listarPublicos() {
        return artesaoRepository.findByUsuario_AtivoTrueOrderByNomeExibicaoAsc()
                .stream()
                .map(artesao -> new ArtesaoPublicoDTO(
                        artesao,
                        trabalhoRepository.countByArtesao_IdArtesao(artesao.getIdArtesao())))
                .toList();
    }

    /**
     * Perfil público de um artesão.
     *
     * @throws ArtesaoNaoEncontradoException se não existir ou se a conta estiver inativa
     */
    public ArtesaoPublicoDTO buscarPublico(Long idArtesao) {
        Artesao artesao = buscarAtivo(idArtesao);

        return new ArtesaoPublicoDTO(
                artesao,
                trabalhoRepository.countByArtesao_IdArtesao(artesao.getIdArtesao()));
    }

    /**
     * Trabalhos (cards) de um artesão, do mais recente para o mais antigo.
     *
     * @throws ArtesaoNaoEncontradoException se não existir ou se a conta estiver inativa
     */
    public List<TrabalhoResponseDTO> listarTrabalhosPublicos(Long idArtesao) {
        Artesao artesao = buscarAtivo(idArtesao);

        return trabalhoRepository
                .findByArtesao_IdArtesaoOrderByDataPublicacaoDesc(artesao.getIdArtesao())
                .stream()
                .map(TrabalhoResponseDTO::new)
                .toList();
    }

    /** Todos os artesãos (ativos e inativos) para o painel administrativo. */
    public List<ArtesaoAdminResponseDTO> listarParaAdministrador() {
        return artesaoRepository.findAllByOrderByNomeExibicaoAsc()
                .stream()
                .map(artesao -> new ArtesaoAdminResponseDTO(
                        artesao,
                        trabalhoRepository.countByArtesao_IdArtesao(artesao.getIdArtesao())))
                .toList();
    }

    /**
     * Trabalhos de qualquer artesão (inclusive inativo), para o administrador moderar.
     *
     * @throws ArtesaoNaoEncontradoException se não existir artesão com esse ID
     */
    public List<TrabalhoResponseDTO> listarTrabalhosParaAdministrador(Long idArtesao) {
        Artesao artesao = artesaoRepository.findById(idArtesao)
                .orElseThrow(ArtesaoNaoEncontradoException::new);

        return trabalhoRepository
                .findByArtesao_IdArtesaoOrderByDataPublicacaoDesc(artesao.getIdArtesao())
                .stream()
                .map(TrabalhoResponseDTO::new)
                .toList();
    }

    private Artesao buscarAtivo(Long idArtesao) {
        Artesao artesao = artesaoRepository.findById(idArtesao)
                .orElseThrow(ArtesaoNaoEncontradoException::new);

        if (!artesao.getUsuario().isAtivo()) {
            throw new ArtesaoNaoEncontradoException();
        }
        return artesao;
    }
}
