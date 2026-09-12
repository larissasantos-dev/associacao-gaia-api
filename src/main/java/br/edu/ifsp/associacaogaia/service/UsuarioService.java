package br.edu.ifsp.associacaogaia.service;

import br.edu.ifsp.associacaogaia.dto.LoginDTO;
import br.edu.ifsp.associacaogaia.exception.CredenciaisInvalidasException;
import br.edu.ifsp.associacaogaia.dto.UsuarioCadastroDTO;
import br.edu.ifsp.associacaogaia.exception.EmailJaCadastradoException;
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

    public List<Usuario> listarUsuarios() {
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
                dados.getTipoUsuario()
        );

        return usuarioRepository.save(usuario);
    }

    public Usuario realizarLogin(LoginDTO dados){
        Optional<Usuario> usuarioEncontrado =
                usuarioRepository.findByEmail(dados.getEmail());

        if(usuarioEncontrado.isEmpty()){
            throw new CredenciaisInvalidasException("E-mail ou senha inválidos.");
        }

        Usuario usuario = usuarioEncontrado.get();

        if(!passwordEncoder.matches(dados.getSenha(), usuario.getSenha())){
            throw new CredenciaisInvalidasException("E-mail ou senha inválidos.");
        }
        return usuario;
    }
}