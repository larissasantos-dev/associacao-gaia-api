package br.edu.ifsp.associacaogaia.service;

import br.edu.ifsp.associacaogaia.dto.ArtesaoAtualizacaoDTO;
import br.edu.ifsp.associacaogaia.exception.ArtesaoNaoEncontradoException;
import br.edu.ifsp.associacaogaia.model.Artesao;
import br.edu.ifsp.associacaogaia.repository.ArtesaoRepository;
import org.springframework.stereotype.Service;

/**
 * Service responsável pela leitura e atualização do perfil público do artesão
 * (nome de exibição, biografia, avatar, redes sociais etc — model Artesao).
 */
@Service
public class ArtesaoService {

    private final ArtesaoRepository artesaoRepository;

    public ArtesaoService(ArtesaoRepository artesaoRepository) {
        this.artesaoRepository = artesaoRepository;
    }

    public Artesao buscarPerfilDoUsuarioAutenticado(Long idUsuario) {
        return artesaoRepository
                .findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(ArtesaoNaoEncontradoException::new);
    }

    public Artesao atualizarPerfil(Long idUsuario, ArtesaoAtualizacaoDTO dto) {
        Artesao artesao = artesaoRepository
                .findByUsuario_IdUsuario(idUsuario)
                .orElseThrow(ArtesaoNaoEncontradoException::new);

        artesao.setNomeExibicao(dto.getNomeExibicao());
        artesao.setTituloCurto(dto.getTituloCurto());
        artesao.setBiografia(dto.getBiografia());
        artesao.setLead(dto.getLead());
        artesao.setCidade(dto.getCidade());
        artesao.setMembroDesde(dto.getMembroDesde());
        artesao.setLocaisAtendimento(dto.getLocaisAtendimento());
        artesao.setAvatarUrl(dto.getAvatarUrl());
        artesao.setInstagram(dto.getInstagram());
        artesao.setWhatsappUrl(dto.getWhatsappUrl());
        artesao.setFacebookUrl(dto.getFacebookUrl());

        return artesaoRepository.save(artesao);
    }
}
