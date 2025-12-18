package com.ubs.dnc.education.academic_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class CreateAlunoRequest extends CreatePessoaRequest {
    @NotBlank(message = "Matricula é obrigatória")
    private String matricula;
}
