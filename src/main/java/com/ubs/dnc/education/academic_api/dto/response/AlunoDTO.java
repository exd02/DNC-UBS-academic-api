package com.ubs.dnc.education.academic_api.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AlunoDTO extends PessoaDTO {
    private String matricula;
}
