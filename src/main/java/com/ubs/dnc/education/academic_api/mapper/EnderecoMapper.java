package com.ubs.dnc.education.academic_api.mapper;

import com.ubs.dnc.education.academic_api.dto.request.CreateEnderecoRequest;
import com.ubs.dnc.education.academic_api.dto.response.EnderecoDTO;
import com.ubs.dnc.education.academic_api.model.Endereco;
import org.springframework.stereotype.Component;

@Component
public class EnderecoMapper {
    public EnderecoDTO toDTO(Endereco obj) {
        EnderecoDTO dto = new EnderecoDTO();
        dto.setCep(obj.getCep());
        dto.setBairro(obj.getBairro());
        dto.setCidade(obj.getCidade());
        dto.setEstado(obj.getEstado());

        return dto;
    }

    public Endereco toOBJ(CreateEnderecoRequest request) {
        Endereco endereco = new Endereco();
        endereco.setCep(request.getCep());
        return endereco;
    }
}
