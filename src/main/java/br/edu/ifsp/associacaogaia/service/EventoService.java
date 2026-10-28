package br.edu.ifsp.associacaogaia.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import br.edu.ifsp.associacaogaia.dto.EventoAtualizacaoDTO;
import br.edu.ifsp.associacaogaia.dto.EventoCadastroDTO;
import br.edu.ifsp.associacaogaia.dto.EventoResponseDTO;
import br.edu.ifsp.associacaogaia.exception.EventoNaoEncontradoException;
import br.edu.ifsp.associacaogaia.model.Evento;
import br.edu.ifsp.associacaogaia.repository.EventoRepository;

@Service
public class EventoService {
    private final EventoRepository eventoRepository;

    public EventoService(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    public EventoResponseDTO cadastrar(EventoCadastroDTO eventoCadastroDTO){

        Evento evento = new Evento();
        evento.setImagemUrl(eventoCadastroDTO.getImagemUrl());
        evento.setTitulo(eventoCadastroDTO.getTitulo());
        evento.setDescricao(eventoCadastroDTO.getDescricao());
        evento.setLocal(eventoCadastroDTO.getLocal());
        evento.setData(eventoCadastroDTO.getData());
        evento.setHora(eventoCadastroDTO.getHora());
        evento.setCategoria(eventoCadastroDTO.getCategoria());

        Evento eventoSalvo = eventoRepository.save(evento);
        return new EventoResponseDTO(eventoSalvo);
    }

    public List<EventoResponseDTO> listarTodos() {
        List<Evento> eventos = eventoRepository.findAll();

        return eventos.stream().map(EventoResponseDTO::new).collect(Collectors.toList());
    }

    public EventoResponseDTO buscarPorId(Long id) {
    Evento evento = eventoRepository.findById(id).orElseThrow(EventoNaoEncontradoException::new);

    return new EventoResponseDTO(evento);
    }

    
    public EventoResponseDTO atualizar(Long id, EventoAtualizacaoDTO dto) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(EventoNaoEncontradoException::new);

        if (dto.getImagemUrl() != null) {
            evento.setImagemUrl(dto.getImagemUrl());
        }

        if (dto.getTitulo() != null) {
            evento.setTitulo(dto.getTitulo());
        }

        if (dto.getDescricao() != null) {
            evento.setDescricao(dto.getDescricao());
        }

        if (dto.getLocal() != null) {
            evento.setLocal(dto.getLocal());
        }

        if (dto.getData() != null) {
            evento.setData(dto.getData());
        }

        if (dto.getHora() != null) {
            evento.setHora(dto.getHora());
        }
        if (dto.getCategoria() != null) {
            evento.setCategoria(dto.getCategoria());
        }

        Evento eventoSalvo = eventoRepository.save(evento);
        return new EventoResponseDTO(eventoSalvo);
    }

    public void excluir(Long id) {
        if (!eventoRepository.existsById(id)) {
            throw new EventoNaoEncontradoException();
        }

        eventoRepository.deleteById(id);
    }

}
// Service pra eventos