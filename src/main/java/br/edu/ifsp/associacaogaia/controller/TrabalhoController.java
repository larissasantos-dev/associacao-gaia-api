package br.edu.ifsp.associacaogaia.controller;

import br.edu.ifsp.associacaogaia.dto.TrabalhoCadastroDTO;
import br.edu.ifsp.associacaogaia.dto.TrabalhoResponseDTO;
import br.edu.ifsp.associacaogaia.model.Trabalho;
import br.edu.ifsp.associacaogaia.security.UsuarioDetails;
import br.edu.ifsp.associacaogaia.service.TrabalhoService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para operações de Trabalho (portfólio do artesão).
 *
 * Endpoints expostos:
 *   POST   /api/trabalhos       — cadastra um novo trabalho vinculado ao artesão autenticado
 *   GET    /api/trabalhos/meus  — lista os trabalhos do artesão autenticado
 *   PUT    /api/trabalhos/{id}  — atualiza um trabalho existente do artesão autenticado
 *   DELETE /api/trabalhos/{id}  — exclui um trabalho existente do artesão autenticado
 *
 * Segurança:
 *   Todos os endpoints exigem token JWT válido no header "Authorization: Bearer <token>".
 *   O idUsuario é extraído do principal autenticado pelo Spring Security —
 *   o front-end NÃO precisa enviar nenhum identificador do artesão.
 *   Em PUT/DELETE, o service verifica que o trabalho pertence ao artesão
 *   autenticado antes de alterar/excluir (→ HTTP 403 caso contrário).
 */
@RestController
@RequestMapping("/api/trabalhos")
public class TrabalhoController {

    private final TrabalhoService trabalhoService;

    public TrabalhoController(TrabalhoService trabalhoService) {
        this.trabalhoService = trabalhoService;
    }

    /**
     * Cadastra um novo trabalho no portfólio do artesão autenticado.
     *
     * Corpo esperado (JSON):
     * {
     *   "imagemUrl": "https://...",   // obrigatório
     *   "legenda":   "Minha peça",    // obrigatório, máx 150 chars
     *   "descricao": "Feita à mão...",// opcional
     *   "categoria": "Bordado"         // opcional
     * }
     *
     * Respostas:
     *   201 Created  — trabalho salvo com sucesso
     *   400 Bad Request — validação falhou (@NotBlank, @Size)
     *   401 Unauthorized — token ausente ou inválido (Spring Security)
     *   404 Not Found — usuário autenticado não possui perfil de artesão
     *
     * @param dto            dados do formulário, validados pelo Bean Validation
     * @param usuarioDetails principal autenticado injetado pelo Spring Security
     * @return ResponseEntity com o trabalho criado e status 201
     */
    @PostMapping
    public ResponseEntity<TrabalhoResponseDTO> cadastrarTrabalho(
            @Valid @RequestBody TrabalhoCadastroDTO dto,
            @AuthenticationPrincipal UsuarioDetails usuarioDetails) {

        // Extrai o ID do usuário autenticado a partir do token JWT
        // (nunca confiamos em um id enviado pelo cliente)
        Long idUsuario = usuarioDetails.getUsuario().getIdUsuario();

        // Delega toda a lógica de negócio para o service
        Trabalho trabalhoSalvo = trabalhoService.cadastrarTrabalho(dto, idUsuario);

        // Retorna 201 Created com o trabalho persistido no corpo da resposta.
        // Convertido para DTO (não retorna a entidade direto — ver TrabalhoResponseDTO).
        return ResponseEntity.status(HttpStatus.CREATED).body(new TrabalhoResponseDTO(trabalhoSalvo));
    }

    /**
     * Lista os trabalhos do portfólio do artesão autenticado.
     *
     * Respostas:
     *   200 OK — lista de trabalhos (pode ser vazia)
     *   401 Unauthorized — token ausente ou inválido
     *   404 Not Found — usuário autenticado não possui perfil de artesão
     */
    @GetMapping("/meus")
    public List<TrabalhoResponseDTO> listarMeusTrabalhos(@AuthenticationPrincipal UsuarioDetails usuarioDetails) {
        Long idUsuario = usuarioDetails.getUsuario().getIdUsuario();

        return trabalhoService.listarTrabalhosDoArtesao(idUsuario)
                .stream()
                .map(TrabalhoResponseDTO::new)
                .toList();
    }

    /**
     * Atualiza um trabalho existente do portfólio do artesão autenticado.
     * Aceita o mesmo corpo JSON do cadastro (imagemUrl, legenda, descricao, categoria).
     *
     * Respostas:
     *   200 OK — trabalho atualizado com sucesso
     *   400 Bad Request — validação falhou (@NotBlank, @Size)
     *   401 Unauthorized — token ausente ou inválido
     *   403 Forbidden — o trabalho pertence a outro artesão
     *   404 Not Found — não existe trabalho com esse ID
     *
     * @param id             ID do trabalho a ser atualizado (vem da URL)
     * @param dto            novos dados do formulário, validados pelo Bean Validation
     * @param usuarioDetails principal autenticado injetado pelo Spring Security
     */
    @PutMapping("/{id}")
    public ResponseEntity<TrabalhoResponseDTO> atualizarTrabalho(
            @PathVariable Long id,
            @Valid @RequestBody TrabalhoCadastroDTO dto,
            @AuthenticationPrincipal UsuarioDetails usuarioDetails) {

        Long idUsuario = usuarioDetails.getUsuario().getIdUsuario();
        Trabalho trabalhoAtualizado = trabalhoService.atualizarTrabalho(id, dto, idUsuario);
        return ResponseEntity.ok(new TrabalhoResponseDTO(trabalhoAtualizado));
    }

    /**
     * Exclui um trabalho existente do portfólio do artesão autenticado.
     *
     * Respostas:
     *   204 No Content — trabalho excluído com sucesso
     *   401 Unauthorized — token ausente ou inválido
     *   403 Forbidden — o trabalho pertence a outro artesão
     *   404 Not Found — não existe trabalho com esse ID
     *
     * @param id             ID do trabalho a ser excluído (vem da URL)
     * @param usuarioDetails principal autenticado injetado pelo Spring Security
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirTrabalho(
            @PathVariable Long id,
            @AuthenticationPrincipal UsuarioDetails usuarioDetails) {

        Long idUsuario = usuarioDetails.getUsuario().getIdUsuario();
        trabalhoService.excluirTrabalho(id, idUsuario);
        return ResponseEntity.noContent().build();
    }
}
