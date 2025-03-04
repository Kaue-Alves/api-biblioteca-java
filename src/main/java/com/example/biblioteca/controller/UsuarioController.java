package com.example.biblioteca.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.biblioteca.DTO.UsuarioDTO;
import com.example.biblioteca.entitys.UsuarioEntity;
import com.example.biblioteca.repository.UsuarioRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

  @Autowired
  private UsuarioRepository usuarioRepository;

  @GetMapping()
  public List<UsuarioEntity> listaTodosUsuarios(@RequestParam(required = false) String nome) {
    if (nome != null) {
      return usuarioRepository.findByNome(nome);
    }
    return usuarioRepository.findAll();
  }

  @PostMapping
  public ResponseEntity<UsuarioEntity> criarUsuario(@RequestBody @Valid UsuarioDTO dados) {
    UsuarioEntity usuarioCriado = new UsuarioEntity(dados);

    this.usuarioRepository.save(usuarioCriado);
    return ResponseEntity.ok(usuarioCriado);
  }

  @DeleteMapping
  public ResponseEntity<String> deletarUsuario(@RequestParam long id) {
    usuarioRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }
}