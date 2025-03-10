package com.example.biblioteca.entitys;

import com.example.biblioteca.DTO.LivroDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "livros")
public class LivroEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String autor;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String editora;

    @Column(nullable = false)
    private int ano;

    @Column(nullable = false)
    private boolean emprestado;

    public LivroEntity() {
        this.emprestado = false; // Definindo valor padrão no construtor
    }

    public LivroEntity(LivroDTO livroDTO) {
        this.autor = livroDTO.autor();
        this.titulo = livroDTO.titulo();
        this.editora = livroDTO.editora();
        this.ano = livroDTO.ano();
        this.emprestado = false; // Sempre falso ao criar
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public boolean isEmprestado() {
        return emprestado;
    }

    public void setEmprestado(boolean emprestado) {
        this.emprestado = emprestado;
    }

}
