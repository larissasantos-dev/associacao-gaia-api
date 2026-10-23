
package br.edu.ifsp.associacaogaia.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AgendamentoCadastroDTO {

    @NotNull(message = "O ID do evento é obrigatório.")
    @Positive(message = "O ID do evento deve ser positivo.")
    private Long idEvento;

    public Long getIdEvento() {
        return idEvento;
    }

    public void setIdEvento(Long idEvento) {
        this.idEvento = idEvento;
    }
}
