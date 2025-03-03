package com.example.biblioteca.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.biblioteca.entitys.UsuarioEntity;
import com.example.biblioteca.repository.UsuarioRepository;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

  @Autowired
  private UsuarioRepository usuarioRepostiory;

  @GetMapping()
  public List<UsuarioEntity> listaTodosUsuarios(@RequestParam(required = false) String nome) {
    if (nome != null) {
      return usuarioRepostiory.findByNome(nome);
    }
    return usuarioRepostiory.findAll();
  }

  @PostMapping
  public ResponseEntity<String> addUsuario(@RequestBody UsuarioEntity usuario) {
    usuarioRepostiory.save(usuario);
    return ResponseEntity.ok().build();
  }

}