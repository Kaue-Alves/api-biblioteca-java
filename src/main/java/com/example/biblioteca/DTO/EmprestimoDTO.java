package com.example.biblioteca.DTO;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

public record EmprestimoDTO(@NotNull long usuario, @NotNull long livro, @NotNull LocalDateTime dataDevolucao) {
}
