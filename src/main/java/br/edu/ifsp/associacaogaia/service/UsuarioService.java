package br.edu.ifsp.associacaogaia.service;

import br.edu.ifsp.associacaogaia.dto.LoginDTO;
import br.edu.ifsp.associacaogaia.exception.CredenciaisInvalidasException;
import br.edu.ifsp.associacaogaia.dto.UsuarioAtualizacaoDTO;
import br.edu.ifsp.associacaogaia.dto.UsuarioCadastroDTO;
import br.edu.ifsp.associacaogaia.exception.EmailJaCadastradoException;
import br.edu.ifsp.associacaogaia.exception.UsuarioInativoException;
import br.edu.ifsp.associacaogaia.exception.UsuarioNaoEncontradoException;
import br.edu.ifsp.associacaogaia.model.TipoUsuario;
import br.edu.ifsp.associacaogaia.model.Usuario;
import br.edu.ifsp.associacaogaia.repository.UsuarioRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listarUsuarios(TipoUsuario tipo){
        if(tipo != null){
            return usuarioRepository.findByTipoUsuario(tipo);
        }
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarUsuario(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Usuario cadastrarUsuario(UsuarioCadastroDTO dados) {
        if (usuarioRepository.findByEmail(dados.getEmail()).isPresent()) {
            throw new EmailJaCadastradoException("E-mail já cadastrado.");
        }

        Usuario usuario = new Usuario(
                dados.getNome(),
                dados.getEmail(),
                passwordEncoder.encode(dados.getSenha()),
                dados.getTelefone(),
                TipoUsuario.VISITANTE
        );

        return usuarioRepository.save(usuario);
    }

    public Usuario atualizarUsuario(Long id, UsuarioAtualizacaoDTO dados) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(UsuarioNaoEncontradoException::new);

        usuario.setNome(dados.getNome());
        usuario.setTelefone(dados.getTelefone());

        if (dados.getSenha() != null && !dados.getSenha().isBlank()) {
            usuario.setSenha(passwordEncoder.encode(dados.getSenha()));
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario realizarLogin(LoginDTO dados) {
        Optional<Usuario> usuarioEncontrado =
                usuarioRepository.findByEmail(dados.getEmail());

        if (usuarioEncontrado.isEmpty()) {
            throw new CredenciaisInvalidasException("E-mail ou senha inválidos.");
        }

        Usuario usuario = usuarioEncontrado.get();

        if (!passwordEncoder.matches(dados.getSenha(), usuario.getSenha())) {
            throw new CredenciaisInvalidasException("E-mail ou senha inválidos.");
        }

        if (!usuario.isAtivo()) {
            throw new UsuarioInativoException("Usuário desativado. Entre em contato com o administrador.");
        }
        return usuario;
    }

    public Usuario alterarStatusUsuario(Long id, boolean ativo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado."));

        usuario.setAtivo(ativo);
        return usuarioRepository.save(usuario);
    }
}