package com.example.biblioteca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.biblioteca.entitys.LivroEntity;

public interface LivroRepository extends JpaRepository<LivroEntity, Long>{
    List<LivroEntity> findByTitulo(String titulo);
    List<LivroEntity> findByEmprestado(boolean emprestado);
}
