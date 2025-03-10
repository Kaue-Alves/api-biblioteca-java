package com.example.biblioteca.DTO;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record EmprestimoDTO(@NotNull long usuarioId, @NotNull long livroId, @NotNull String status,
        @NotNull LocalDateTime dataEmprestimo, @NotNull LocalDateTime dataDevolucao, float multa) {

}
