package br.edu.ifsp.associacaogaia.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class SolicitacaoArtesaoTest {

    @Test
    void construtor_deveCriarSolicitacaoPendente() {
        Usuario usuario = criarUsuario();

        SolicitacaoArtesao solicitacao = criarSolicitacao(usuario);

        assertEquals(usuario, solicitacao.getUsuario());
        assertEquals("Quero participar da associação", solicitacao.getJustificativa());
        assertEquals("Artesanato da Lari", solicitacao.getNomeExibicao());
        assertEquals("Artesã", solicitacao.getTituloCurto());
        assertEquals("São Paulo", solicitacao.getCidade());

        assertEquals(StatusSolicitacao.PENDENTE, solicitacao.getStatus());
        assertNotNull(solicitacao.getDataSolicitacao());
        assertNull(solicitacao.getDataResposta());
        assertNull(solicitacao.getMotivoRejeicao());
    }

    @Test
    void aprovar_deveAlterarStatusERegistrarDataResposta() {
        SolicitacaoArtesao solicitacao = criarSolicitacao(criarUsuario());

        solicitacao.aprovar();

        assertEquals(StatusSolicitacao.APROVADA, solicitacao.getStatus());
        assertNotNull(solicitacao.getDataResposta());
        assertNull(solicitacao.getMotivoRejeicao());
    }

    @Test
    void rejeitar_deveAlterarStatusRegistrarDataERetornarMotivo() {
        SolicitacaoArtesao solicitacao = criarSolicitacao(criarUsuario());
        String motivo = "Informações insuficientes.";

        solicitacao.rejeitar(motivo);

        assertEquals(StatusSolicitacao.REJEITADA, solicitacao.getStatus());
        assertNotNull(solicitacao.getDataResposta());
        assertEquals(motivo, solicitacao.getMotivoRejeicao());
    }

    @Test
    void criarPerfilArtesao_deveTransferirDadosDaSolicitacao() {
        Usuario usuario = criarUsuario();
        SolicitacaoArtesao solicitacao = criarSolicitacao(usuario);

        solicitacao.setBiografia("Trabalho com peças artesanais.");
        solicitacao.setInstagram("@artesanatodalari");
        solicitacao.setWhatsappUrl("https://wa.me/5511999999999");

        Artesao artesao = solicitacao.criarPerfilArtesao();

        assertNotNull(artesao);
        assertEquals(usuario, artesao.getUsuario());
        assertEquals("Artesanato da Lari", artesao.getNomeExibicao());
        assertEquals("Artesã", artesao.getTituloCurto());
        assertEquals("São Paulo", artesao.getCidade());
        assertEquals("Trabalho com peças artesanais.", artesao.getBiografia());
        assertEquals("@artesanatodalari", artesao.getInstagram());
        assertEquals("https://wa.me/5511999999999", artesao.getWhatsappUrl());
        assertEquals(String.valueOf(Year.now().getValue()), artesao.getMembroDesde());
    }

    private Usuario criarUsuario() {
        return new Usuario(
                "Larissa",
                "larissa@teste.com",
                "senha-criptografada",
                "11999999999",
                TipoUsuario.VISITANTE
        );
    }

    private SolicitacaoArtesao criarSolicitacao(Usuario usuario) {
        return new SolicitacaoArtesao(
                usuario,
                "Quero participar da associação",
                "Artesanato da Lari",
                "Artesã",
                "São Paulo"
        );
    }
}