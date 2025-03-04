package com.example.biblioteca.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.biblioteca.repository.LivroRepository;
import com.example.biblioteca.entitys.LivroEntity;

@RestController
@RequestMapping("/livros")
public class LivroController {

  @Autowired
  private LivroRepository livroRepository;

  public LivroController(LivroRepository livroRepository) {
    this.livroRepository = livroRepository;
  }

  @GetMapping()
  public List<LivroEntity> listaTodosLivros(@RequestParam(required = false) String titulo) {
    if (titulo != null) {
      return livroRepository.findByTitulo(titulo);
    }
    return livroRepository.findAll();
  }

  @PostMapping
  public ResponseEntity<String> addLivro(@RequestBody LivroEntity livro) {
    livroRepository.save(livro);
    return ResponseEntity.ok().build();
  }

  @DeleteMapping
  public ResponseEntity<String> deleteUsuario(@RequestParam long id) {
    livroRepository.deleteById(id);
    return ResponseEntity.ok().build();
  }
}
