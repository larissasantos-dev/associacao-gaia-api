
package br.edu.ifsp.associacaogaia.service;

import br.edu.ifsp.associacaogaia.model.Agendamento;
import br.edu.ifsp.associacaogaia.model.Evento;
import br.edu.ifsp.associacaogaia.model.StatusAgendamento;
import br.edu.ifsp.associacaogaia.model.TipoUsuario;
import br.edu.ifsp.associacaogaia.model.Usuario;
import br.edu.ifsp.associacaogaia.repository.AgendamentoRepository;
import br.edu.ifsp.associacaogaia.repository.EventoRepository;
import br.edu.ifsp.associacaogaia.repository.UsuarioRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AgendamentoService {

    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final EventoRepository eventoRepository;

    public AgendamentoService(
            AgendamentoRepository agendamentoRepository,
            UsuarioRepository usuarioRepository,
            EventoRepository eventoRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.eventoRepository = eventoRepository;
    }

    @Transactional
    public Agendamento agendar(Long idUsuario, Long idEvento) {
        Usuario usuario = buscarVisitanteAtivo(idUsuario);

        Evento evento = eventoRepository.findById(idEvento)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Evento não encontrado."));

        if (evento.getData() == null || evento.getHora() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O evento precisa ter data e horário definidos.");
        }

        LocalDateTime dataHoraEvento =
                evento.getData().atTime(evento.getHora());

        if (!dataHoraEvento.isAfter(LocalDateTime.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Não é possível agendar um evento que já passou.");
        }

        boolean jaAgendado =
                agendamentoRepository
                        .existsByUsuario_IdUsuarioAndEvento_IdEventoAndStatus(
                                idUsuario,
                                idEvento,
                                StatusAgendamento.AGENDADO);

        if (jaAgendado) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Você já possui um agendamento ativo para este evento.");
        }

        Agendamento agendamento = new Agendamento(usuario, evento);

        return agendamentoRepository.save(agendamento);
    }

    @Transactional(readOnly = true)
    public List<Agendamento> listarMeusAgendamentos(Long idUsuario) {
        buscarVisitanteAtivo(idUsuario);

        return agendamentoRepository
                .findByUsuario_IdUsuario(idUsuario);
    }

    @Transactional(readOnly = true)
    public List<Agendamento> listarMeusAgendamentosAtivos(Long idUsuario) {
        buscarVisitanteAtivo(idUsuario);

        return agendamentoRepository
                .findByUsuario_IdUsuarioAndStatus(
                        idUsuario,
                        StatusAgendamento.AGENDADO);
    }

    @Transactional
    public Agendamento cancelar(Long idAgendamento, Long idUsuario) {
        buscarVisitanteAtivo(idUsuario);

        Agendamento agendamento =
                agendamentoRepository
                        .findByIdAgendamentoAndUsuario_IdUsuario(
                                idAgendamento,
                                idUsuario)
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Agendamento não encontrado."));

        if (agendamento.getStatus() == StatusAgendamento.CANCELADO) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Este agendamento já foi cancelado.");
        }

        agendamento.cancelar();

        return agendamentoRepository.save(agendamento);
    }

    private Usuario buscarVisitanteAtivo(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Usuário não encontrado."));

        if (!usuario.isAtivo()) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Sua conta está inativa.");
        }

        if (usuario.getTipoUsuario() != TipoUsuario.VISITANTE) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Somente visitantes podem realizar agendamentos.");
        }

        return usuario;
    }
}
