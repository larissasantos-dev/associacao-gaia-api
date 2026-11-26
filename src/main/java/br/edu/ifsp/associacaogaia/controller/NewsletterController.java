package br.edu.ifsp.associacaogaia.controller;

import br.edu.ifsp.associacaogaia.dto.NewsletterRequestDTO;
import br.edu.ifsp.associacaogaia.dto.NewsletterRespostaDTO;
import br.edu.ifsp.associacaogaia.service.NewsletterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Newsletter — endpoints PÚBLICOS (formulário do rodapé do site).
 *
 *   POST /api/newsletter/inscricoes            — {"email": "..."}  inscreve
 *   POST /api/newsletter/inscricoes/cancelar   — {"email": "..."}  cancela
 *
 * A resposta é sempre a mesma, exista o e-mail ou não (não revela quem já está inscrito).
 */
@RestController
@RequestMapping("/api/newsletter/inscricoes")
public class NewsletterController {

    private final NewsletterService newsletterService;

    public NewsletterController(NewsletterService newsletterService) {
        this.newsletterService = newsletterService;
    }

    @PostMapping
    public ResponseEntity<NewsletterRespostaDTO> inscrever(@Valid @RequestBody NewsletterRequestDTO dto) {
        newsletterService.inscrever(dto.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new NewsletterRespostaDTO("Inscrição realizada! Você receberá as novidades da Associação Gaia."));
    }

    @PostMapping("/cancelar")
    public ResponseEntity<NewsletterRespostaDTO> cancelar(@Valid @RequestBody NewsletterRequestDTO dto) {
        newsletterService.cancelar(dto.getEmail());

        return ResponseEntity.ok(new NewsletterRespostaDTO("Se este e-mail estava inscrito, a inscrição foi cancelada."));
    }
}
