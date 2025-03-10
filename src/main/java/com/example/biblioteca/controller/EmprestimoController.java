package com.example.biblioteca.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.biblioteca.entitys.EmprestimoEntity;
import com.example.biblioteca.entitys.LivroEntity;
import com.example.biblioteca.repository.EmprestimoRepository;
import com.example.biblioteca.repository.LivroRepository;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private LivroRepository livroRepository;

    @GetMapping
    public ResponseEntity<List<EmprestimoEntity>> buscarEmprestimos() {
        List<EmprestimoEntity> emprestimos = this.emprestimoRepository.findAll();
        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<EmprestimoEntity>> buscarEmprestimosPorUsuarioId(@PathVariable long id) {
        List<EmprestimoEntity> emprestimos = this.emprestimoRepository.findByUsuarioId(id);

        if (emprestimos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping("/livro/{id}")
    public ResponseEntity<List<EmprestimoEntity>> buscarEmprestimosPorLivroId(@PathVariable long id) {
        List<EmprestimoEntity> emprestimos = this.emprestimoRepository.findByLivroId(id);

        if (emprestimos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<EmprestimoEntity>> buscarEmprestimosPorStatus(@PathVariable String status) {
        List<EmprestimoEntity> emprestimos = this.emprestimoRepository.findByStatus(status);

        if (emprestimos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(emprestimos);
    }

    @PostMapping
    public ResponseEntity<EmprestimoEntity> criarEmprestimo(@RequestBody EmprestimoEntity emprestimo) {
        LivroEntity livro = emprestimo.getLivro();
        if (livro.isEmprestado()) {
            return ResponseEntity.status(400).body(null);
        }

        emprestimo.setStatus("ANDAMENTO");
        emprestimo.setDataEmprestimo(LocalDateTime.now());
        emprestimo.setMulta(0.0f);

        livro.setEmprestado(true);
        this.livroRepository.save(livro);

        EmprestimoEntity novoEmprestimo = this.emprestimoRepository.save(emprestimo);
        return ResponseEntity.ok(novoEmprestimo);
    }

    @PostMapping("/devolver/{id}")
    public ResponseEntity<EmprestimoEntity> devolverLivro(@PathVariable long id) {
        EmprestimoEntity emprestimo = this.emprestimoRepository.findById(id).orElse(null);

        if (emprestimo == null) {
            return ResponseEntity.notFound().build();
        }

        emprestimo.setStatus("DEVOLVIDO");
        emprestimo.setDataDevolucao(LocalDateTime.now());

        LivroEntity livro = emprestimo.getLivro();
        livro.setEmprestado(false);
        this.livroRepository.save(livro);

        this.emprestimoRepository.save(emprestimo);

        return ResponseEntity.ok(emprestimo);
    }

    
    @Scheduled(fixedRate = 120000)
    public void aplicarMultas() {
        List<EmprestimoEntity> emprestimos = this.emprestimoRepository.findByStatus("ANDAMENTO");
        LocalDateTime agora = LocalDateTime.now();

        for (EmprestimoEntity emprestimo : emprestimos) {
            long minutosAtraso = ChronoUnit.MINUTES.between(emprestimo.getDataEmprestimo().plusMinutes(2), agora);
            if (minutosAtraso > 0) {
                emprestimo.setMulta(minutosAtraso * 1.0f);
                emprestimo.setStatus("ATRASADO");
                this.emprestimoRepository.save(emprestimo);
            }
        }
    }
}