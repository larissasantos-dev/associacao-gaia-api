package br.edu.ifsp.associacaogaia.dto;

import jakarta.validation.constraints.NotNull;

public class AtualizarStatusDTO {
    @NotNull
    private Boolean ativo;

    public AtualizarStatusDTO(){
    }

    public Boolean getAtivo(){
        return ativo;
    }

    public void setAtivo(Boolean ativo){
        this.ativo = ativo;
    }
}