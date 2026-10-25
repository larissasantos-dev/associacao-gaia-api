package br.edu.ifsp.associacaogaia.repository;

import br.edu.ifsp.associacaogaia.model.Agendamento;
import br.edu.ifsp.associacaogaia.model.StatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AgendamentoRepository
        extends JpaRepository<Agendamento, Long> {

    List<Agendamento> findByUsuario_IdUsuario(Long idUsuario);

    List<Agendamento> findByUsuario_IdUsuarioAndStatus(
            Long idUsuario,
            StatusAgendamento status
    );

    Optional<Agendamento> findByIdAgendamentoAndUsuario_IdUsuario(
            Long idAgendamento,
            Long idUsuario
    );

    boolean existsByUsuario_IdUsuarioAndEvento_IdEventoAndStatus(
            Long idUsuario,
            Long idEvento,
            StatusAgendamento status
    );
}