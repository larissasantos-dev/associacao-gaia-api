package br.edu.ifsp.associacaogaia.controller;

import br.edu.ifsp.associacaogaia.dto.LoginResponseDTO;
import br.edu.ifsp.associacaogaia.dto.UsuarioResponseDTO;
import br.edu.ifsp.associacaogaia.model.TipoUsuario;
import br.edu.ifsp.associacaogaia.model.Usuario;
import br.edu.ifsp.associacaogaia.service.TokenService;
import br.edu.ifsp.associacaogaia.service.UsuarioService;
import br.edu.ifsp.associacaogaia.dto.UsuarioCadastroDTO;
import br.edu.ifsp.associacaogaia.dto.LoginDTO;
import br.edu.ifsp.associacaogaia.dto.AtualizarStatusDTO;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final TokenService tokenService;

    public UsuarioController(UsuarioService usuarioService, TokenService tokenService){
        this.usuarioService = usuarioService;
        this.tokenService = tokenService;
    }

    @GetMapping
    public List<UsuarioResponseDTO> listarUsuario(
            @RequestParam(required = false)TipoUsuario tipo){
        return usuarioService.listarUsuarios(tipo)
                .stream()
                .map(UsuarioResponseDTO::new)
                .toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or #id == authentication.principal.usuario.idUsuario")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuario(@PathVariable Long id){
        return usuarioService.buscarUsuario(id)
                .map(UsuarioResponseDTO::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("email/{email}")
    @PreAuthorize("hasRole('ADMINISTRADOR') or #email == authentication.principal.usuario.email")
    public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorEmail(@PathVariable String email){
        return usuarioService.buscarUsuarioPorEmail(email)
                .map(UsuarioResponseDTO::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponseDTO cadastrarUsuario(@Valid @RequestBody UsuarioCadastroDTO dados){
        Usuario usuario = usuarioService.cadastrarUsuario(dados);
        return new UsuarioResponseDTO(usuario);
    }

    @PostMapping("/login")
    public LoginResponseDTO realizarLogin(@Valid @RequestBody LoginDTO dados){
        Usuario usuario = usuarioService.realizarLogin(dados);
        String token = tokenService.gerarToken(usuario);
        return new LoginResponseDTO(token, new UsuarioResponseDTO(usuario));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public UsuarioResponseDTO alterarStatusUsuario(
            @PathVariable Long id,
            @Valid @RequestBody AtualizarStatusDTO dados){
        Usuario usuario = usuarioService.alterarStatusUsuario(id, dados.getAtivo());
        return new UsuarioResponseDTO(usuario);
    }
}