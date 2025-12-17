package com.ubs.dnc.education.academic_api.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@SuperBuilder
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotBlank
    protected String nome;

    @NotBlank
    // @CPF (substituir depois)
    @Column(unique = true, nullable = false, length = 11)
    protected String cpf;

    @NotBlank
    @Email
    @Column(unique = true)
    protected String email;

    @NotBlank
    @Column(nullable = false)
    protected String telefone;

    @Valid
    @Embedded
    protected Endereco endereco;
}
