
package br.edu.ifsp.associacaogaia.controller;

import br.edu.ifsp.associacaogaia.dto.RejeicaoSolicitacaoDTO;
import br.edu.ifsp.associacaogaia.dto.SolicitacaoArtesaoAdminResponseDTO;
import br.edu.ifsp.associacaogaia.model.SolicitacaoArtesao;
import br.edu.ifsp.associacaogaia.model.StatusSolicitacao;
import br.edu.ifsp.associacaogaia.service.SolicitacaoArtesaoAdminService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/solicitacoes-artesao")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class SolicitacaoArtesaoAdminController {

    private final SolicitacaoArtesaoAdminService solicitacaoAdminService;

    public SolicitacaoArtesaoAdminController(
            SolicitacaoArtesaoAdminService solicitacaoAdminService) {
        this.solicitacaoAdminService = solicitacaoAdminService;
    }

    @GetMapping
    public ResponseEntity<List<SolicitacaoArtesaoAdminResponseDTO>> listar(
            @RequestParam(required = false) StatusSolicitacao status) {

        List<SolicitacaoArtesaoAdminResponseDTO> solicitacoes =
                solicitacaoAdminService.listar(status)
                        .stream()
                        .map(SolicitacaoArtesaoAdminResponseDTO::new)
                        .toList();

        return ResponseEntity.ok(solicitacoes);
    }

    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<SolicitacaoArtesaoAdminResponseDTO> aprovar(
            @PathVariable Long id) {

        SolicitacaoArtesao solicitacao =
                solicitacaoAdminService.aprovar(id);

        return ResponseEntity.ok(
                new SolicitacaoArtesaoAdminResponseDTO(solicitacao)
        );
    }

    @PatchMapping("/{id}/rejeitar")
    public ResponseEntity<SolicitacaoArtesaoAdminResponseDTO> rejeitar(
            @PathVariable Long id,
            @Valid @RequestBody RejeicaoSolicitacaoDTO dados) {

        SolicitacaoArtesao solicitacao =
                solicitacaoAdminService.rejeitar(id, dados.getMotivo());

        return ResponseEntity.ok(
                new SolicitacaoArtesaoAdminResponseDTO(solicitacao)
        );
    }
}
