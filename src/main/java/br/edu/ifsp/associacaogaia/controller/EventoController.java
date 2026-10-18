package br.edu.ifsp.associacaogaia.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import br.edu.ifsp.associacaogaia.dto.EventoAtualizacaoDTO;
import br.edu.ifsp.associacaogaia.dto.EventoCadastroDTO;
import br.edu.ifsp.associacaogaia.dto.EventoResponseDTO;
import br.edu.ifsp.associacaogaia.service.EventoService;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    @PostMapping
    public ResponseEntity<EventoResponseDTO> cadastrar(
            @Valid @RequestBody EventoCadastroDTO dto) {

        EventoResponseDTO eventoSalvo = eventoService.cadastrar(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(eventoSalvo);
    }

    @GetMapping
    public ResponseEntity<List<EventoResponseDTO>> listarTodos() {
        List<EventoResponseDTO> eventos = eventoService.listarTodos();

        return ResponseEntity.ok(eventos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventoResponseDTO> buscarPorId(
            @PathVariable Long id) {

        EventoResponseDTO evento = eventoService.buscarPorId(id);

        return ResponseEntity.ok(evento);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EventoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody EventoAtualizacaoDTO dto) {

        EventoResponseDTO eventoAtualizado = eventoService.atualizar(id, dto);

        return ResponseEntity.ok(eventoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        eventoService.excluir(id);

        return ResponseEntity.noContent().build();
    }
}