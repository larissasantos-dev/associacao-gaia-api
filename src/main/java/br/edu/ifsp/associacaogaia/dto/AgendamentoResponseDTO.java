
package br.edu.ifsp.associacaogaia.dto;

import br.edu.ifsp.associacaogaia.model.Agendamento;
import br.edu.ifsp.associacaogaia.model.StatusAgendamento;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

public class AgendamentoResponseDTO {

    private Long idAgendamento;
    private Long idEvento;
    private String imagemUrl;
    private String tituloEvento;
    private LocalDate dataEvento;
    private LocalTime horaEvento;
    private String local;
    private LocalDateTime dataAgendamento;
    private StatusAgendamento status;

    public AgendamentoResponseDTO(Agendamento agendamento) {
        this.idAgendamento = agendamento.getIdAgendamento();
        this.idEvento = agendamento.getEvento().getIdEvento();
        this.imagemUrl = agendamento.getEvento().getImagemUrl();
        this.tituloEvento = agendamento.getEvento().getTitulo();
        this.dataEvento = agendamento.getEvento().getData();
        this.horaEvento = agendamento.getEvento().getHora();
        this.local = agendamento.getEvento().getLocal();
        this.dataAgendamento = agendamento.getDataAgendamento();
        this.status = agendamento.getStatus();
    }

    public Long getIdAgendamento() {
        return idAgendamento;
    }

    public Long getIdEvento() {
        return idEvento;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public String getTituloEvento() {
        return tituloEvento;
    }

    public LocalDate getDataEvento() {
        return dataEvento;
    }

    public LocalTime getHoraEvento() {
        return horaEvento;
    }

    public String getLocal() {
        return local;
    }

    public LocalDateTime getDataAgendamento() {
        return dataAgendamento;
    }

    public StatusAgendamento getStatus() {
        return status;
    }
}
