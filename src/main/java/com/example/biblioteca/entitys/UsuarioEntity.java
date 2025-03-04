package com.example.biblioteca.entitys;

import com.example.biblioteca.DTO.UsuarioDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;

@Entity
@Table(name = "usuarios")
public class UsuarioEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private String nome;

  @Email
  @Column(nullable = false, unique = true)
  private String email;

  //cpf deve ser único
  @Column(unique = true)
  private String cpf;

  public UsuarioEntity() {
  }

  
  public UsuarioEntity(long id, String nome, String email, String cpf) {
    this.id = id;
    this.nome = nome;
    this.email = email;
    this.cpf = cpf;
  }

  public UsuarioEntity(UsuarioDTO usuarioDTO) {
    this.nome = usuarioDTO.nome();
    this.email = usuarioDTO.email();
    this.cpf = usuarioDTO.cpf();
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getNome() {
    return nome;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCpf() {
    return cpf;
  }

  public void setCpf(String cpf) {
    this.cpf = cpf;
  }
}