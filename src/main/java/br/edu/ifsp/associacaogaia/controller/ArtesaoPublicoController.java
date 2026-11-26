package br.edu.ifsp.associacaogaia.controller;

import br.edu.ifsp.associacaogaia.dto.ArtesaoPublicoDTO;
import br.edu.ifsp.associacaogaia.dto.TrabalhoResponseDTO;
import br.edu.ifsp.associacaogaia.service.ArtesaoConsultaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Catálogo PÚBLICO de artesãos (qualquer visitante, sem login).
 *
 *   GET /api/artesaos/publico                      — artesãos ativos
 *   GET /api/artesaos/publico/{id}                 — perfil de um artesão
 *   GET /api/artesaos/publico/{id}/trabalhos       — cards (trabalhos) do artesão, com preço
 *
 * Fica em "/publico" para não conflitar com GET /api/artesaos/me (que exige login).
 */
@RestController
@RequestMapping("/api/artesaos/publico")
public class ArtesaoPublicoController {

    private final ArtesaoConsultaService consultaService;

    public ArtesaoPublicoController(ArtesaoConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @GetMapping
    public List<ArtesaoPublicoDTO> listar() {
        return consultaService.listarPublicos();
    }

    @GetMapping("/{id}")
    public ArtesaoPublicoDTO buscar(@PathVariable Long id) {
        return consultaService.buscarPublico(id);
    }

    @GetMapping("/{id}/trabalhos")
    public List<TrabalhoResponseDTO> listarTrabalhos(@PathVariable Long id) {
        return consultaService.listarTrabalhosPublicos(id);
    }
}
