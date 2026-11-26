package br.edu.ifsp.associacaogaia.service;

import br.edu.ifsp.associacaogaia.exception.InscricaoNaoEncontradaException;
import br.edu.ifsp.associacaogaia.model.NewsletterInscricao;
import br.edu.ifsp.associacaogaia.repository.NewsletterInscricaoRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NewsletterServiceTest {

    @Mock
    private NewsletterInscricaoRepository repository;

    private NewsletterService service;

    @BeforeEach
    void setUp() {
        service = new NewsletterService(repository);
    }

    @Test
    void inscrever_deveSalvarEmailNormalizado() {
        when(repository.findByEmail("maria@teste.com")).thenReturn(Optional.empty());
        when(repository.save(any(NewsletterInscricao.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        NewsletterInscricao resultado = service.inscrever("  Maria@Teste.com ");

        assertEquals("maria@teste.com", resultado.getEmail());
        assertTrue(resultado.isAtivo());
    }

    @Test
    void inscrever_naoDeveDuplicarEmailJaAtivo() {
        NewsletterInscricao existente = new NewsletterInscricao("maria@teste.com");
        when(repository.findByEmail("maria@teste.com")).thenReturn(Optional.of(existente));

        NewsletterInscricao resultado = service.inscrever("maria@teste.com");

        assertSame(existente, resultado);
        verify(repository, never()).save(any(NewsletterInscricao.class));
    }

    @Test
    void inscrever_deveReativarInscricaoCancelada() {
        NewsletterInscricao cancelada = new NewsletterInscricao("maria@teste.com");
        cancelada.cancelar();

        when(repository.findByEmail("maria@teste.com")).thenReturn(Optional.of(cancelada));
        when(repository.save(any(NewsletterInscricao.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        NewsletterInscricao resultado = service.inscrever("maria@teste.com");

        assertTrue(resultado.isAtivo());
        assertNull(resultado.getDataCancelamento());
    }

    @Test
    void cancelar_deveDesativarInscricaoAtiva() {
        NewsletterInscricao ativa = new NewsletterInscricao("maria@teste.com");
        when(repository.findByEmail("maria@teste.com")).thenReturn(Optional.of(ativa));

        service.cancelar("maria@teste.com");

        assertFalse(ativa.isAtivo());
        assertNotNull(ativa.getDataCancelamento());
        verify(repository).save(ativa);
    }

    @Test
    void cancelar_naoDeveFazerNadaQuandoEmailNaoExiste() {
        when(repository.findByEmail("x@teste.com")).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> service.cancelar("x@teste.com"));

        verify(repository, never()).save(any(NewsletterInscricao.class));
    }

    @Test
    void remover_deveLancarExcecaoQuandoNaoExiste() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(InscricaoNaoEncontradaException.class, () -> service.remover(99L));
    }

    @Test
    void remover_deveExcluirInscricaoExistente() {
        NewsletterInscricao inscricao = new NewsletterInscricao("maria@teste.com");
        when(repository.findById(1L)).thenReturn(Optional.of(inscricao));

        service.remover(1L);

        verify(repository).delete(inscricao);
    }
}
