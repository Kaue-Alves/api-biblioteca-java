package com.example.biblioteca.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.biblioteca.repository.LivroRepository;

import jakarta.validation.Valid;

import com.example.biblioteca.DTO.LivroDTO;
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
  public ResponseEntity<List<LivroEntity>> buscarLivros() {

    List<LivroEntity> livros = this.livroRepository.findAll();

    return ResponseEntity.ok(livros);
  }

  @GetMapping("/{id}")

  public ResponseEntity<LivroEntity> buscarLivroPorId(@PathVariable long id) {

    LivroEntity livro = this.livroRepository.findById(id).orElse(null);

    if (livro == null) {
      return ResponseEntity.notFound().build();

    }

    return ResponseEntity.ok(livro);

  }

  @PostMapping
  public ResponseEntity<LivroEntity> adicionarLivro(@RequestBody @Valid LivroDTO dados) {
    LivroEntity livroCriado = new LivroEntity(dados);
    this.livroRepository.save(livroCriado);
    return ResponseEntity.ok(livroCriado);
  }

  @PutMapping("/{id}")

  public ResponseEntity<LivroEntity> atualizarLivro(@PathVariable long id, @RequestBody LivroDTO dados) {

    LivroEntity livro = this.livroRepository.findById(id).orElse(null);

    if (livro == null) {
      return ResponseEntity.notFound().build();

    }

    livro.setAutor(dados.autor());
    livro.setTitulo(dados.titulo());
    livro.setEditora(dados.editora());
    livro.setAno(dados.ano());

    this.livroRepository.save(livro);

    return ResponseEntity.ok(livro);

  }

  @PatchMapping("/{id}/emprestar")
public ResponseEntity<LivroEntity> alterarEmprestado(@PathVariable long id) {
    LivroEntity livro = this.livroRepository.findById(id).orElse(null);

    if (livro == null) {
        return ResponseEntity.notFound().build();
    }

    livro.setEmprestado(!livro.isEmprestado());
    this.livroRepository.save(livro);

    return ResponseEntity.ok(livro);
}


  // deleção de usuario

  @DeleteMapping("/{id}")

  public ResponseEntity<String> deletarLivro(@PathVariable long id) {

    LivroEntity livro = this.livroRepository.findById(id).orElse(null);

    if (livro == null) {
      return ResponseEntity.notFound().build();

    }

    this.livroRepository.delete(livro);
    return ResponseEntity.ok("Livro" + " " + livro.getTitulo() + " " + "deletado com sucesso! ");

  }
}
