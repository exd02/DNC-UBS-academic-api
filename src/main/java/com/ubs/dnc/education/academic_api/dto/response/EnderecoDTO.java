package com.ubs.dnc.education.academic_api.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnderecoDTO {
    private String cep;
    private String bairro;
    private String cidade;
    private String estado;
}