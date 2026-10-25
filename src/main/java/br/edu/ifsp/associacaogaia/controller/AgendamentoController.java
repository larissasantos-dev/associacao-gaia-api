
package br.edu.ifsp.associacaogaia.controller;

import br.edu.ifsp.associacaogaia.dto.AgendamentoCadastroDTO;
import br.edu.ifsp.associacaogaia.dto.AgendamentoResponseDTO;
import br.edu.ifsp.associacaogaia.model.Agendamento;
import br.edu.ifsp.associacaogaia.model.Usuario;
import br.edu.ifsp.associacaogaia.repository.UsuarioRepository;
import br.edu.ifsp.associacaogaia.service.AgendamentoService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;
    private final UsuarioRepository usuarioRepository;

    public AgendamentoController(
            AgendamentoService agendamentoService,
            UsuarioRepository usuarioRepository) {
        this.agendamentoService = agendamentoService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public ResponseEntity<AgendamentoResponseDTO> agendar(
            @Valid @RequestBody AgendamentoCadastroDTO dto,
            Authentication authentication) {

        Long idUsuario = obterIdUsuario(authentication);

        Agendamento agendamento = agendamentoService.agendar(
                idUsuario,
                dto.getIdEvento()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new AgendamentoResponseDTO(agendamento));
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoResponseDTO>> listar(
            Authentication authentication) {

        Long idUsuario = obterIdUsuario(authentication);

        List<AgendamentoResponseDTO> agendamentos =
                agendamentoService.listarMeusAgendamentos(idUsuario)
                        .stream()
                        .map(AgendamentoResponseDTO::new)
                        .toList();

        return ResponseEntity.ok(agendamentos);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<AgendamentoResponseDTO>> listarAtivos(
            Authentication authentication) {

        Long idUsuario = obterIdUsuario(authentication);

        List<AgendamentoResponseDTO> agendamentos =
                agendamentoService.listarMeusAgendamentosAtivos(idUsuario)
                        .stream()
                        .map(AgendamentoResponseDTO::new)
                        .toList();

        return ResponseEntity.ok(agendamentos);
    }

    @PatchMapping("/{idAgendamento}/cancelar")
    public ResponseEntity<AgendamentoResponseDTO> cancelar(
            @PathVariable Long idAgendamento,
            Authentication authentication) {

        Long idUsuario = obterIdUsuario(authentication);

        Agendamento agendamento = agendamentoService.cancelar(
                idAgendamento,
                idUsuario
        );

        return ResponseEntity.ok(
                new AgendamentoResponseDTO(agendamento)
        );
    }

    private Long obterIdUsuario(Authentication authentication) {
        if (authentication == null
                || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "É necessário estar autenticado."
            );
        }

        Usuario usuario = usuarioRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "Usuário autenticado não encontrado."
                ));

        return usuario.getIdUsuario();
    }
}
