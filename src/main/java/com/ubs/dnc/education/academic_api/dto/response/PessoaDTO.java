package com.ubs.dnc.education.academic_api.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PessoaDTO {
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;

    @JsonProperty("endereco")
    private EnderecoDTO enderecoDTO;
}
