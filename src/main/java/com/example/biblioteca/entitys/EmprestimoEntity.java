package com.example.biblioteca.entitys;

import java.time.LocalDateTime;

import com.example.biblioteca.DTO.EmprestimoDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "emprestimos")
public class EmprestimoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne
    @JoinColumn(name = "livro_id", nullable = false)
    private LivroEntity livro;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime dataEmprestimo;

    @Column(nullable = false)
    private LocalDateTime dataDevolucao;

    @Column(nullable = false)
    private float multa;

    public EmprestimoEntity() {
    }

    public EmprestimoEntity(EmprestimoDTO emprestimoDTO, UsuarioEntity usuario, LivroEntity livro) {
        this.usuario = usuario;
        this.livro = livro;
        this.status = "ANDAMENTO";
        this.dataEmprestimo = LocalDateTime.now();
        this.dataDevolucao = emprestimoDTO.dataDevolucao();
        this.multa = 0;
    }

    // Getters e setters para os campos

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public LivroEntity getLivro() {
        return livro;
    }

    public void setLivro(LivroEntity livro) {
        this.livro = livro;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDateTime dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDateTime getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDateTime dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public float getMulta() {
        return multa;
    }

    public void setMulta(float multa) {
        this.multa = multa;
    }
}
