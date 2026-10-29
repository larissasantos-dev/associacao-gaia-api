package br.edu.ifsp.associacaogaia.controller;

import br.edu.ifsp.associacaogaia.dto.SolicitacaoArtesaoRequestDTO;
import br.edu.ifsp.associacaogaia.dto.SolicitacaoArtesaoResponseDTO;
import br.edu.ifsp.associacaogaia.model.SolicitacaoArtesao;
import br.edu.ifsp.associacaogaia.security.UsuarioDetails;
import br.edu.ifsp.associacaogaia.service.SolicitacaoArtesaoService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * Endpoints da solicitação para virar artesão. O usuário é sempre o do
 * token (nunca vem no corpo da requisição).
 *
 * POST /api/solicitacoes-artesao        201 criada | 400 dados inválidos
 *                                       403 não é visitante | 409 já pendente
 *                                       ou prazo de reenvio não cumprido
 * GET  /api/solicitacoes-artesao/minha  200 última solicitação | 204 nunca solicitou
 */

@RestController
@RequestMapping("/api/solicitacoes-artesao")
public class SolicitacaoArtesaoController {

    private final SolicitacaoArtesaoService solicitacaoService;

    public SolicitacaoArtesaoController(SolicitacaoArtesaoService solicitacaoService) {
        this.solicitacaoService = solicitacaoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SolicitacaoArtesaoResponseDTO criarSolicitacao(
            @Valid @RequestBody SolicitacaoArtesaoRequestDTO dados,
            @AuthenticationPrincipal UsuarioDetails usuarioDetails) {
        Long idUsuario = usuarioDetails.getUsuario().getIdUsuario();
        SolicitacaoArtesao solicitacao = solicitacaoService.criarSolicitacao(idUsuario, dados);
        return new SolicitacaoArtesaoResponseDTO(solicitacao, null);
    }

    @GetMapping("/minha")
    public ResponseEntity<SolicitacaoArtesaoResponseDTO> buscarMinhaSolicitacao(
            @AuthenticationPrincipal UsuarioDetails usuarioDetails) {
        Long idUsuario = usuarioDetails.getUsuario().getIdUsuario();
        return solicitacaoService.buscarUltimaDoUsuario(idUsuario)
                .map(s -> new SolicitacaoArtesaoResponseDTO(s, solicitacaoService.dataLiberacaoReenvio(s)))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }
}