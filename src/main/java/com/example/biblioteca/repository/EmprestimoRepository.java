package com.example.biblioteca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.biblioteca.entitys.EmprestimoEntity;

public interface EmprestimoRepository extends JpaRepository<EmprestimoEntity, Long> {
    List<EmprestimoEntity> findByUsuarioId(long usuarioId);

    List<EmprestimoEntity> findByLivroId(long livroId);

    List<EmprestimoEntity> findByStatus(String status);
}
