package br.edu.ifsp.associacaogaia.service;

import br.edu.ifsp.associacaogaia.dto.UsuarioCadastroDTO;
import br.edu.ifsp.associacaogaia.model.Usuario;
import br.edu.ifsp.associacaogaia.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarUsuarios(){
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarUsuario(Integer id){
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarUsuarioPorEmail(String email){
        return usuarioRepository.findByEmail(email);
    }

    public Usuario cadastrarUsuario(UsuarioCadastroDTO dados){

        Usuario usuario = new Usuario(
                dados.getNome(),
                dados.getEmail(),
                dados.getSenha(),
                dados.getTelefone(),
                dados.getTipoUsuario()
        );

    return usuarioRepository.save(usuario);
    }


}