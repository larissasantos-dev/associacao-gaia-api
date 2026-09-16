package br.edu.ifsp.associacaogaia.controller;

import br.edu.ifsp.associacaogaia.dto.ArtesaoAtualizacaoDTO;
import br.edu.ifsp.associacaogaia.dto.ArtesaoResponseDTO;
import br.edu.ifsp.associacaogaia.model.Artesao;
import br.edu.ifsp.associacaogaia.security.UsuarioDetails;
import br.edu.ifsp.associacaogaia.service.ArtesaoService;

import jakarta.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para o perfil público do artesão autenticado
 * (nome de exibição, biografia, avatar, redes sociais etc).
 *
 * Endpoints expostos:
 *   GET /api/artesaos/me — retorna o perfil do artesão autenticado
 *   PUT /api/artesaos/me — atualiza o perfil do artesão autenticado
 *
 * Segurança: exige token JWT válido (Authorization: Bearer). O idUsuario é
 * extraído do principal autenticado — o front-end não envia nenhum ID.
 * 404 caso o usuário autenticado não possua perfil de artesão vinculado.
 */
@RestController
@RequestMapping("/api/artesaos")
public class ArtesaoController {

    private final ArtesaoService artesaoService;

    public ArtesaoController(ArtesaoService artesaoService) {
        this.artesaoService = artesaoService;
    }

    @GetMapping("/me")
    public ArtesaoResponseDTO buscarMeuPerfil(@AuthenticationPrincipal UsuarioDetails usuarioDetails) {
        Long idUsuario = usuarioDetails.getUsuario().getIdUsuario();
        Artesao artesao = artesaoService.buscarPerfilDoUsuarioAutenticado(idUsuario);
        return new ArtesaoResponseDTO(artesao);
    }

    @PutMapping("/me")
    public ArtesaoResponseDTO atualizarMeuPerfil(
            @Valid @RequestBody ArtesaoAtualizacaoDTO dto,
            @AuthenticationPrincipal UsuarioDetails usuarioDetails) {

        Long idUsuario = usuarioDetails.getUsuario().getIdUsuario();
        Artesao artesao = artesaoService.atualizarPerfil(idUsuario, dto);
        return new ArtesaoResponseDTO(artesao);
    }
}
