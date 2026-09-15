package br.edu.ifsp.associacaogaia.service;

import br.edu.ifsp.associacaogaia.dto.LoginDTO;
import br.edu.ifsp.associacaogaia.dto.UsuarioCadastroDTO;
import br.edu.ifsp.associacaogaia.exception.CredenciaisInvalidasException;
import br.edu.ifsp.associacaogaia.exception.EmailJaCadastradoException;
import br.edu.ifsp.associacaogaia.model.TipoUsuario;
import br.edu.ifsp.associacaogaia.model.Usuario;
import br.edu.ifsp.associacaogaia.repository.UsuarioRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        usuarioService = new UsuarioService(usuarioRepository, passwordEncoder);
    }

    // ---------- CADASTRO ----------

    @Test
    void cadastrarUsuario_comDadosValidos_deveSalvarComSucesso() {
        UsuarioCadastroDTO dados = criarDadosCadastro("teste@teste.com");

        when(usuarioRepository.findByEmail(dados.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dados.getSenha())).thenReturn("senha-criptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(chamada -> chamada.getArgument(0));

        Usuario usuario = usuarioService.cadastrarUsuario(dados);

        assertNotNull(usuario);
        assertEquals(dados.getNome(), usuario.getNome());
        assertEquals(dados.getEmail(), usuario.getEmail());
        assertEquals("senha-criptografada", usuario.getSenha());
    }

    @Test
    void cadastrarUsuario_comEmailJaExistente_deveLancarExcecao() {
        UsuarioCadastroDTO dados = criarDadosCadastro("existente@teste.com");

        Usuario usuarioExistente = new Usuario(
                "Usuario Existente", "existente@teste.com",
                "senha-qualquer", "11999999999", TipoUsuario.VISITANTE
        );

        when(usuarioRepository.findByEmail(dados.getEmail()))
                .thenReturn(Optional.of(usuarioExistente));

        assertThrows(EmailJaCadastradoException.class,
                () -> usuarioService.cadastrarUsuario(dados));
    }

    @Test
    void cadastrarUsuario_deveSempreCriarComoVisitante() {
        UsuarioCadastroDTO dados = criarDadosCadastro("visitante@teste.com");

        when(usuarioRepository.findByEmail(dados.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dados.getSenha())).thenReturn("senha-criptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(chamada -> chamada.getArgument(0));

        Usuario usuario = usuarioService.cadastrarUsuario(dados);

        assertEquals(TipoUsuario.VISITANTE, usuario.getTipoUsuario());
    }

    // ---------- LOGIN ----------

    @Test
    void realizarLogin_comCredenciaisCorretas_deveRetornarUsuario() {
        LoginDTO dados = new LoginDTO();
        dados.setEmail("teste@teste.com");
        dados.setSenha("123456");

        Usuario usuarioSalvo = new Usuario(
                "Teste", "teste@teste.com", "senha-criptografada",
                "11999999999", TipoUsuario.VISITANTE
        );

        when(usuarioRepository.findByEmail(dados.getEmail())).thenReturn(Optional.of(usuarioSalvo));
        when(passwordEncoder.matches(dados.getSenha(), usuarioSalvo.getSenha())).thenReturn(true);

        Usuario resultado = usuarioService.realizarLogin(dados);

        assertEquals(usuarioSalvo.getEmail(), resultado.getEmail());
    }

    @Test
    void realizarLogin_comEmailInexistente_deveLancarExcecao() {
        LoginDTO dados = new LoginDTO();
        dados.setEmail("naoexiste@teste.com");
        dados.setSenha("123456");

        when(usuarioRepository.findByEmail(dados.getEmail())).thenReturn(Optional.empty());

        assertThrows(CredenciaisInvalidasException.class,
                () -> usuarioService.realizarLogin(dados));
    }

    @Test
    void realizarLogin_comSenhaErrada_deveLancarExcecao() {
        LoginDTO dados = new LoginDTO();
        dados.setEmail("teste@teste.com");
        dados.setSenha("senhaErrada");

        Usuario usuarioSalvo = new Usuario(
                "Teste", "teste@teste.com", "senha-criptografada",
                "11999999999", TipoUsuario.VISITANTE
        );

        when(usuarioRepository.findByEmail(dados.getEmail())).thenReturn(Optional.of(usuarioSalvo));
        when(passwordEncoder.matches(dados.getSenha(), usuarioSalvo.getSenha())).thenReturn(false);

        assertThrows(CredenciaisInvalidasException.class,
                () -> usuarioService.realizarLogin(dados));
    }

    // ---------- helper ----------

    private UsuarioCadastroDTO criarDadosCadastro(String email) {
        UsuarioCadastroDTO dados = new UsuarioCadastroDTO();
        dados.setNome("Teste");
        dados.setEmail(email);
        dados.setSenha("123456");
        dados.setTelefone("11999999999");
        return dados;
    }
}