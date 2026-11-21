
package br.edu.ifsp.associacaogaia.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import br.edu.ifsp.associacaogaia.dto.EventoAtualizacaoDTO;
import br.edu.ifsp.associacaogaia.dto.EventoCadastroDTO;
import br.edu.ifsp.associacaogaia.dto.EventoResponseDTO;
import br.edu.ifsp.associacaogaia.exception.EventoNaoEncontradoException;
import br.edu.ifsp.associacaogaia.model.Evento;
import br.edu.ifsp.associacaogaia.repository.EventoRepository;
import br.edu.ifsp.associacaogaia.repository.AgendamentoRepository;

@Service
public class EventoService {

    private static final Logger log = LoggerFactory.getLogger(EventoService.class);

    private final EventoRepository eventoRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final ImagemStorageService imagemStorageService;

    public EventoService(
            EventoRepository eventoRepository,
            AgendamentoRepository agendamentoRepository,
            ImagemStorageService imagemStorageService) {

        this.eventoRepository = eventoRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.imagemStorageService = imagemStorageService;
    }

    public EventoResponseDTO cadastrar(EventoCadastroDTO dto) {

        ImagemStorageService.ResultadoUpload imagem =
                imagemStorageService.upload(dto.getImagem());

        try {
            Evento evento = new Evento();

            evento.setImagemUrl(imagem.url());
            evento.setImagemPublicId(imagem.publicId());
            evento.setTitulo(dto.getTitulo());
            evento.setDescricao(dto.getDescricao());
            evento.setLocal(dto.getLocal());
            evento.setData(dto.getData());
            evento.setHora(dto.getHora());
            evento.setCategoria(dto.getCategoria());

            Evento eventoSalvo = eventoRepository.saveAndFlush(evento);

            return new EventoResponseDTO(eventoSalvo);

        } catch (RuntimeException e) {
            excluirImagemComSeguranca(imagem.publicId());
            throw e;
        }
    }

    public List<EventoResponseDTO> listarTodos() {
        return eventoRepository.findAll()
                .stream()
                .map(EventoResponseDTO::new)
                .toList();
    }

    public EventoResponseDTO buscarPorId(Long id) {
        Evento evento = eventoRepository.findById(id)
                .orElseThrow(EventoNaoEncontradoException::new);

        return new EventoResponseDTO(evento);
    }

    public EventoResponseDTO atualizar(Long id, EventoAtualizacaoDTO dto) {

        Evento evento = eventoRepository.findById(id)
                .orElseThrow(EventoNaoEncontradoException::new);

        String imagemAntigaPublicId = evento.getImagemPublicId();

        ImagemStorageService.ResultadoUpload novaImagem = null;

        if (dto.getImagem() != null && !dto.getImagem().isEmpty()) {
            novaImagem = imagemStorageService.upload(dto.getImagem());
        }

        try {
            if (novaImagem != null) {
                evento.setImagemUrl(novaImagem.url());
                evento.setImagemPublicId(novaImagem.publicId());
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

            if (dto.getCancelado() != null) {
                evento.setCancelado(dto.getCancelado());
            }

            Evento eventoSalvo = eventoRepository.saveAndFlush(evento);

            // Só exclui a imagem antiga depois de salvar a nova referência.
            if (novaImagem != null && imagemAntigaPublicId != null
                    && !imagemAntigaPublicId.isBlank()) {
                excluirImagemComSeguranca(imagemAntigaPublicId);
            }

            return new EventoResponseDTO(eventoSalvo);

        } catch (RuntimeException e) {
            // Se o banco falhar, remove a imagem nova para evitar arquivo órfão.
            if (novaImagem != null) {
                excluirImagemComSeguranca(novaImagem.publicId());
            }

            throw e;
        }
    }

    public void excluir(Long id) {

        Evento evento = eventoRepository.findById(id)
                .orElseThrow(EventoNaoEncontradoException::new);

        if (agendamentoRepository.existsByEvento_idEvento(id)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Este evento possui agendamentos. Cancele-o em vez de excluir."
            );
        }

        String imagemPublicId = evento.getImagemPublicId();

        eventoRepository.delete(evento);

        if (imagemPublicId != null && !imagemPublicId.isBlank()) {
            excluirImagemComSeguranca(imagemPublicId);
        }
    }

    private void excluirImagemComSeguranca(String publicId) {
        try {
            imagemStorageService.excluir(publicId);
        } catch (RuntimeException e) {
            log.warn(
                    "Não foi possível excluir a imagem {} do Cloudinary.",
                    publicId,
                    e
            );
        }
    }
}
