package com.ubs.dnc.education.academic_api.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePessoaRequest {
    private String nome;

    @Pattern(regexp = "\\d{11}", message = "CPF deve seguir o formato de 11 digitos numéricos")
    private String cpf;

    @Email(message = "Email inválido")
    private String email;

    private String telefone;

    @Valid
    private UpdateEnderecoRequest enderecoRequest;
}
