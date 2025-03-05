package com.example.biblioteca.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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

  // @GetMapping()
  // public List<UsuarioEntity> listaTodosUsuarios(@RequestParam(required = false)
  // String nome) {
  // if (nome != null) {
  // return usuarioRepository.findByNome(nome);
  // }
  // return usuarioRepository.findAll();
  // }

  @GetMapping()
  public ResponseEntity<List<UsuarioEntity>> buscarUsuarios() {

    List<UsuarioEntity> usuarios = this.usuarioRepository.findAll();

    return ResponseEntity.ok(usuarios);
  }

  @GetMapping("/{id}")

  public ResponseEntity<UsuarioEntity> buscarUsuarioPorId(@PathVariable long id) {

    UsuarioEntity usuario = this.usuarioRepository.findById(id).orElse(null);

    if (usuario == null) {
      return ResponseEntity.notFound().build();

    }

    return ResponseEntity.ok(usuario);

  }

  @PostMapping
  public ResponseEntity<UsuarioEntity> criarUsuario(@RequestBody @Valid UsuarioDTO dados) {
    UsuarioEntity usuarioCriado = new UsuarioEntity(dados);

    this.usuarioRepository.save(usuarioCriado);
    return ResponseEntity.ok(usuarioCriado);
  }

  @PutMapping("/{id}")

  public ResponseEntity<UsuarioEntity> atualizarUsuario(@PathVariable long id, @RequestBody UsuarioDTO dados){

      UsuarioEntity usuario = this.usuarioRepository.findById(id).orElse(null);

      if (usuario == null) {
          return ResponseEntity.notFound().build();

      }

      usuario.setNome(dados.nome());
      usuario.setEmail(dados.email());
      usuario.setCpf(dados.cpf());

      this.usuarioRepository.save(usuario);

      return ResponseEntity.ok(usuario);
      
  }

  // deleção de usuario

  @DeleteMapping("/{id}")

  public ResponseEntity<String> deletarUsuario(@PathVariable long id){

      UsuarioEntity usuario = this.usuarioRepository.findById(id).orElse(null);

      if (usuario == null) {
          return ResponseEntity.notFound().build();

      }

      this.usuarioRepository.delete(usuario);
      return ResponseEntity.ok("Usuário" + " " + usuario.getNome() + " " + "deletado com sucesso! ");

  } 

  // @DeleteMapping
  // public ResponseEntity<String> deletarUsuario(@RequestParam long id) {
  //   usuarioRepository.deleteById(id);
  //   return ResponseEntity.ok().build();
  // }
}