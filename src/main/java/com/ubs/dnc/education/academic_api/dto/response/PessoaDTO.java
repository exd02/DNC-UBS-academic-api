package com.ubs.dnc.education.academic_api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class PessoaDTO {
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;

    @JsonProperty("endereco")
    private EnderecoDTO enderecoDTO;
}
