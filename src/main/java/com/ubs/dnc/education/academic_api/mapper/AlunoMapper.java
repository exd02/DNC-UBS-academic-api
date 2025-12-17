package com.ubs.dnc.education.academic_api.mapper;

import com.ubs.dnc.education.academic_api.dto.request.CreateAlunoRequest;
import com.ubs.dnc.education.academic_api.dto.response.AlunoDTO;
import com.ubs.dnc.education.academic_api.model.Aluno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AlunoMapper {

    @Autowired
    private EnderecoMapper enderecoMapper;

    public AlunoDTO toDTO(Aluno obj) {
        AlunoDTO dto = new AlunoDTO();
        dto.setId(obj.getId());
        dto.setNome(obj.getNome());
        dto.setCpf(obj.getCpf());
        dto.setEmail(obj.getEmail());
        dto.setTelefone(obj.getTelefone());
        dto.setEnderecoDTO(enderecoMapper.toDTO(obj.getEndereco()));
        dto.setMatricula(obj.getMatricula());
        return dto;
    }

    public List<AlunoDTO> toDTOList(List<Aluno> objs) {
        List<AlunoDTO> list = new ArrayList<>();

        for (Aluno obj : objs) {
            AlunoDTO dto = toDTO(obj);
        }

        return list;
    }

    public Aluno toOBJ(CreateAlunoRequest request) {
        return Aluno.builder()
                .nome(request.getNome())
                .cpf(request.getCpf())
                .email(request.getEmail())
                .telefone(request.getTelefone())
                .matricula(request.getMatricula())
                .endereco(enderecoMapper.toOBJ(request.getEnderecoRequest()))
                .build();
    }
}
