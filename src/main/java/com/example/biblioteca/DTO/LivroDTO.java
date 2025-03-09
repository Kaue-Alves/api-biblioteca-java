package com.example.biblioteca.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LivroDTO(@NotBlank String autor, @NotBlank String titulo, @NotBlank String editora, @NotNull int ano) {
}
