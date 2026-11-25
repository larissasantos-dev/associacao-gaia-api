package br.edu.ifsp.associacaogaia.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TrabalhoTest {

    @Test
    void alterarPreco_deveRegistrarPrimeiroPrecoNoHistorico() {
        Trabalho trabalho = new Trabalho();

        boolean mudou = trabalho.alterarPreco(new BigDecimal("120"));

        assertTrue(mudou);
        assertEquals(new BigDecimal("120.00"), trabalho.getPreco());
        assertNotNull(trabalho.getPrecoAtualizadoEm());
        assertEquals(1, trabalho.getHistoricoPrecos().size());
        assertNull(trabalho.getHistoricoPrecos().get(0).getPrecoAnterior());
    }

    @Test
    void alterarPreco_deveIgnorarValorIgual() {
        Trabalho trabalho = new Trabalho();
        trabalho.alterarPreco(new BigDecimal("120.00"));

        boolean mudou = trabalho.alterarPreco(new BigDecimal("120"));

        assertFalse(mudou);
        assertEquals(1, trabalho.getHistoricoPrecos().size());
    }

    @Test
    void alterarPreco_deveAceitarPrecoNuloComoSobConsulta() {
        Trabalho trabalho = new Trabalho();
        trabalho.alterarPreco(new BigDecimal("10.00"));

        boolean mudou = trabalho.alterarPreco(null);

        assertTrue(mudou);
        assertNull(trabalho.getPreco());
        assertEquals(2, trabalho.getHistoricoPrecos().size());
    }

    @Test
    void alterarPreco_naoDeveRegistrarQuandoContinuaNulo() {
        Trabalho trabalho = new Trabalho();

        assertFalse(trabalho.alterarPreco(null));
        assertTrue(trabalho.getHistoricoPrecos().isEmpty());
    }
}
