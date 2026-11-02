package br.edu.ifsp.associacaogaia.service;

import br.edu.ifsp.associacaogaia.model.Evento;
import br.edu.ifsp.associacaogaia.model.TipoUsuario;
import br.edu.ifsp.associacaogaia.model.Usuario;
import br.edu.ifsp.associacaogaia.repository.AgendamentoRepository;
import br.edu.ifsp.associacaogaia.repository.EventoRepository;
import br.edu.ifsp.associacaogaia.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AgendamentoServiceTest {

    @InjectMocks
    private AgendamentoService agendamentoService;

    @Mock
    private AgendamentoRepository agendamentoRepository;

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Deve lançar ResponseStatusException (409) ao tentar agendar um evento cancelado")
    void deveLancarExceptionAoAgendarEventoCancelado(){
        Long eventoId = 1L;
        Long usuarioId = 2L;

        Usuario usuarioAtivo = new Usuario(
                "Larissa Teste",
                "larissa@teste.com",
                "senha123",
                "11999999999",
                TipoUsuario.VISITANTE
        );

        org.springframework.test.util.ReflectionTestUtils.setField(usuarioAtivo, "idUsuario", usuarioId);

        Mockito.when(usuarioRepository.findById(usuarioId))
                .thenReturn(Optional.of(usuarioAtivo));


        Evento eventoCancelado = new Evento();
        org.springframework.test.util.ReflectionTestUtils.setField(eventoCancelado, "idEvento", eventoId);
        eventoCancelado.setCancelado(true);

        Mockito.when(eventoRepository.findById(eventoId))
                .thenReturn(Optional.of(eventoCancelado));


        ResponseStatusException exception = Assertions.assertThrows(
                ResponseStatusException.class,
                () -> agendamentoService.agendar(usuarioId, eventoId)
        );


        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());

        String motivo = exception.getReason();
        Assertions.assertNotNull(motivo, "O motivo da exceção não deveria ser nulo");
        Assertions.assertTrue(motivo.contains("Este evento foi cancelado"));
    }
}
