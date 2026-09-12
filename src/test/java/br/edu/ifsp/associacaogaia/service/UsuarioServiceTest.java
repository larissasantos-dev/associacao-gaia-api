package br.edu.ifsp.associacaogaia.service;

import br.edu.ifsp.associacaogaia.dto.LoginDTO;
import br.edu.ifsp.associacaogaia.exception.CredenciaisInvalidasException;
import br.edu.ifsp.associacaogaia.model.TipoUsuario;
import br.edu.ifsp.associacaogaia.model.Usuario;
import br.edu.ifsp.associacaogaia.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveRealizarLoginComSenhaCorreta() {

        Usuario usuario = new Usuario(
                "Larissa",
                "larissa@gmail.com",
                "senhaCriptografada",
                "11999999999",
                TipoUsuario.ARTESAO
        );

        LoginDTO dados = new LoginDTO();
        dados.setEmail("larissa@gmail.com");
        dados.setSenha("123456");

        when(usuarioRepository.findByEmail("larissa@gmail.com"))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches(
                "123456",
                "senhaCriptografada"
        )).thenReturn(true);

        Usuario resultado = usuarioService.realizarLogin(dados);

        assertEquals(usuario, resultado);
    }

    @Test
    void deveRecusarLoginComSenhaIncorreta() {

        Usuario usuario = new Usuario(
                "Larissa",
                "larissa@gmail.com",
                "senhaCriptografada",
                "11999999999",
                TipoUsuario.ARTESAO
        );

        LoginDTO dados = new LoginDTO();
        dados.setEmail("larissa@gmail.com");
        dados.setSenha("senhaErrada");

        when(usuarioRepository.findByEmail("larissa@gmail.com"))
                .thenReturn(Optional.of(usuario));

        when(passwordEncoder.matches(
                "senhaErrada",
                "senhaCriptografada"
        )).thenReturn(false);

        assertThrows(
                CredenciaisInvalidasException.class,
                () -> usuarioService.realizarLogin(dados)
        );
    }

    @Test
    void deveRecusarLoginQuandoEmailNaoExistir() {

        LoginDTO dados = new LoginDTO();
        dados.setEmail("naoexiste@gmail.com");
        dados.setSenha("123456");

        when(usuarioRepository.findByEmail("naoexiste@gmail.com"))
                .thenReturn(Optional.empty());

        assertThrows(
                CredenciaisInvalidasException.class,
                () -> usuarioService.realizarLogin(dados)
        );
    }
}