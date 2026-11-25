package br.edu.ifsp.associacaogaia.controller;

import br.edu.ifsp.associacaogaia.dto.ArtesaoAdminResponseDTO;
import br.edu.ifsp.associacaogaia.dto.TrabalhoResponseDTO;
import br.edu.ifsp.associacaogaia.service.ArtesaoConsultaService;
import br.edu.ifsp.associacaogaia.service.TrabalhoService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Gerenciamento de artesãos pelo ADMINISTRADOR (aba "Artesãos" do painel).
 *
 *   GET    /api/admin/artesaos                    — todos os artesãos (ativos e inativos)
 *   GET    /api/admin/artesaos/{id}/trabalhos     — trabalhos de um artesão
 *   DELETE /api/admin/trabalhos/{id}              — remove um trabalho (moderação)
 *
 * Ativar/inativar a conta continua em PATCH /api/usuarios/{id}/status.
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class ArtesaoAdminController {

    private final ArtesaoConsultaService consultaService;
    private final TrabalhoService trabalhoService;

    public ArtesaoAdminController(ArtesaoConsultaService consultaService,
                                  TrabalhoService trabalhoService) {
        this.consultaService = consultaService;
        this.trabalhoService = trabalhoService;
    }

    @GetMapping("/artesaos")
    public List<ArtesaoAdminResponseDTO> listarArtesaos() {
        return consultaService.listarParaAdministrador();
    }

    @GetMapping("/artesaos/{id}/trabalhos")
    public List<TrabalhoResponseDTO> listarTrabalhos(@PathVariable Long id) {
        return consultaService.listarTrabalhosParaAdministrador(id);
    }

    @DeleteMapping("/trabalhos/{id}")
    public ResponseEntity<Void> excluirTrabalho(@PathVariable Long id) {
        trabalhoService.excluirTrabalhoComoAdministrador(id);
        return ResponseEntity.noContent().build();
    }
}
