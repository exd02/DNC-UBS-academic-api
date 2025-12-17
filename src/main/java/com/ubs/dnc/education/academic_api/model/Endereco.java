package com.ubs.dnc.education.academic_api.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Endereco {
    @NotBlank
    private String cep;

    private String bairro;

    @NotBlank
    private String cidade;

    @NotBlank
    private String estado;
}