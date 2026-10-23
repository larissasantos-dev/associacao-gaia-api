package br.edu.ifsp.associacaogaia.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agendamento")
    private Long idAgendamento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento evento;

    @Column(name = "data_agendamento", nullable = false, updatable = false)
    private LocalDateTime dataAgendamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusAgendamento status;

    protected Agendamento() {
    }

    public Agendamento(Usuario usuario, Evento evento) {
        this.usuario = usuario;
        this.evento = evento;
        this.dataAgendamento = LocalDateTime.now();
        this.status = StatusAgendamento.AGENDADO;
    }

    public Long getIdAgendamento() {
        return idAgendamento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Evento getEvento() {
        return evento;
    }

    public LocalDateTime getDataAgendamento() {
        return dataAgendamento;
    }

    public StatusAgendamento getStatus() {
        return status;
    }

    public void cancelar() {
        this.status = StatusAgendamento.CANCELADO;
    }
}