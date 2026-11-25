package br.edu.ifsp.associacaogaia.controller;

import br.edu.ifsp.associacaogaia.dto.NewsletterInscricaoResponseDTO;
import br.edu.ifsp.associacaogaia.service.NewsletterService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Newsletter — área do ADMINISTRADOR (aba "Newsletter" do painel).
 *
 *   GET    /api/admin/newsletter/inscricoes        — lista todas as inscrições
 *   DELETE /api/admin/newsletter/inscricoes/{id}   — remove uma inscrição
 */
@RestController
@RequestMapping("/api/admin/newsletter/inscricoes")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class NewsletterAdminController {

    private final NewsletterService newsletterService;

    public NewsletterAdminController(NewsletterService newsletterService) {
        this.newsletterService = newsletterService;
    }

    @GetMapping
    public List<NewsletterInscricaoResponseDTO> listar() {
        return newsletterService.listar()
                .stream()
                .map(NewsletterInscricaoResponseDTO::new)
                .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        newsletterService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
