package br.edu.ifsp.associacaogaia.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

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
}
