package br.edu.ifsp.associacaogaia.controller;

import br.edu.ifsp.associacaogaia.dto.UsuarioResponseDTO;
import br.edu.ifsp.associacaogaia.model.Usuario;
import br.edu.ifsp.associacaogaia.service.UsuarioService;
import br.edu.ifsp.associacaogaia.dto.UsuarioCadastroDTO;
import br.edu.ifsp.associacaogaia.dto.LoginDTO;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioResponseDTO> listarUsuario(){
        return usuarioService.listarUsuarios()
                .stream()
                .map(UsuarioResponseDTO::new)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuario(@PathVariable Long id){
        return usuarioService.buscarUsuario(id)
                .map(UsuarioResponseDTO::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("email/{email}")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorEmail(@PathVariable String email){
        return usuarioService.buscarUsuarioPorEmail(email)
                .map(UsuarioResponseDTO::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public UsuarioResponseDTO cadastrarUsuario(@Valid @RequestBody UsuarioCadastroDTO dados){
        Usuario usuario = usuarioService.cadastrarUsuario(dados);
        return new UsuarioResponseDTO(usuario);
    }

    @PostMapping("/login")
    public UsuarioResponseDTO realizarLogin(@Valid @RequestBody LoginDTO dados){
        Usuario usuario = usuarioService.realizarLogin(dados);
        return new UsuarioResponseDTO(usuario);
    }
}