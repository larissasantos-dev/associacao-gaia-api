package br.edu.ifsp.associacaogaia.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RejeicaoSolicitacaoDTO {
    private String motivo;

    public RejeicaoSolicitacaoDTO(){
    }

    @NotBlank
    @Size(max = 500)
    public String getMotivo(){
        return motivo;
    }

    public void setMotivo(String motivo){
        this.motivo = motivo;
    }
}