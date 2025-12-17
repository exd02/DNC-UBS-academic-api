package com.ubs.dnc.education.academic_api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAlunoRequest extends CreatePessoaRequest {
    @NotBlank(message = "Matricula é obrigatória")
    private String matricula;
}
