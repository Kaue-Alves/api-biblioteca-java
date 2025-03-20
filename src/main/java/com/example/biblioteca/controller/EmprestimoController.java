package com.example.biblioteca.controller;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.biblioteca.DTO.EmprestimoDTO;
import com.example.biblioteca.DTO.EmailDTO;
import com.example.biblioteca.entitys.EmprestimoEntity;
import com.example.biblioteca.entitys.LivroEntity;
import com.example.biblioteca.entitys.UsuarioEntity;
import com.example.biblioteca.repository.EmprestimoRepository;
import com.example.biblioteca.repository.LivroRepository;
import com.example.biblioteca.repository.UsuarioRepository;
import com.example.biblioteca.services.EmailService;

@RestController
@RequestMapping("/emprestimos")
public class EmprestimoController {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EmailService emailService;

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
    public ResponseEntity<EmprestimoEntity> criarEmprestimo(@RequestBody EmprestimoDTO emprestimoDTO) {
        // Verificar se o livro já está emprestado
        LivroEntity livro = livroRepository.findById(emprestimoDTO.livro()).orElse(null);
        if (livro == null || livro.isEmprestado()) {
            return ResponseEntity.status(400).body(null); // Retorna 400 Bad Request se o livro já estiver emprestado ou
                                                          // não existir
        }

        // Verificar se o usuário existe
        UsuarioEntity usuario = usuarioRepository.findById(emprestimoDTO.usuario()).orElse(null);
        if (usuario == null) {
            return ResponseEntity.status(400).body(null); // Retorna 400 Bad Request se o usuário não existir
        }

        // Criar o novo empréstimo
        EmprestimoEntity emprestimo = new EmprestimoEntity(emprestimoDTO, usuario, livro);

        // Atualizando o estado do livro para emprestado
        livro.setEmprestado(true);
        this.livroRepository.save(livro);

        EmprestimoEntity novoEmprestimo = this.emprestimoRepository.save(emprestimo);

        // Enviar e-mail de confirmação de empréstimo
        String emailBody = String.format("O livro '%s' foi emprestado com sucesso.", livro.getTitulo());
        EmailDTO email = new EmailDTO(usuario.getEmail(), "Empréstimo de Livro", emailBody);
        emailService.sendEmail(email);

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

        // Atualizando o estado do livro para não emprestado
        LivroEntity livro = emprestimo.getLivro();
        livro.setEmprestado(false);
        this.livroRepository.save(livro);

        this.emprestimoRepository.save(emprestimo);

        // Enviar e-mail de confirmação de devolução
        String emailBody = String.format("O livro '%s' foi devolvido com sucesso.", livro.getTitulo());
        EmailDTO email = new EmailDTO(emprestimo.getUsuario().getEmail(), "Devolução de Livro", emailBody);
        emailService.sendEmail(email);

        return ResponseEntity.ok(emprestimo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEmprestimo(@PathVariable long id) {
        EmprestimoEntity emprestimo = this.emprestimoRepository.findById(id).orElse(null);

        if (emprestimo == null) {
            return ResponseEntity.notFound().build();
        }

        // Atualizando o estado do livro para não emprestado
        LivroEntity livro = emprestimo.getLivro();
        livro.setEmprestado(false);
        this.livroRepository.save(livro);

        this.emprestimoRepository.delete(emprestimo);

        return ResponseEntity.noContent().build();
    }

    // Método agendado para verificar e aplicar multas
    @Scheduled(fixedRate = 120000) // Executa a cada 2 minutos
    public void aplicarMultas() {
        List<EmprestimoEntity> emprestimos = this.emprestimoRepository.findByStatus("ANDAMENTO");
        LocalDateTime agora = LocalDateTime.now();

        for (EmprestimoEntity emprestimo : emprestimos) {
            long minutosAtraso = ChronoUnit.MINUTES.between(emprestimo.getDataEmprestimo().plusMinutes(2), agora);
            if (minutosAtraso > 0) {
                emprestimo.setMulta(minutosAtraso * 1.0f);
                emprestimo.setStatus("ATRASADO");
                this.emprestimoRepository.save(emprestimo);

                // Enviar e-mail de notificação de atraso
                String emailBody = String.format("O livro '%s' está atrasado. Multa acumulada: R$ %.2f.",
                        emprestimo.getLivro().getTitulo(), emprestimo.getMulta());
                EmailDTO email = new EmailDTO(emprestimo.getUsuario().getEmail(), "Atraso na Devolução de Livro",
                        emailBody);
                emailService.sendEmail(email);
            }
        }
    }
}