package com.ubs.dnc.education.academic_api.mapper;

import com.ubs.dnc.education.academic_api.dto.request.CreateAlunoRequest;
import com.ubs.dnc.education.academic_api.dto.request.CreateProfessorRequest;
import com.ubs.dnc.education.academic_api.dto.response.AlunoDTO;
import com.ubs.dnc.education.academic_api.dto.response.ProfessorDTO;
import com.ubs.dnc.education.academic_api.model.Aluno;
import com.ubs.dnc.education.academic_api.model.Professor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProfessorMapper {

    @Autowired
    private EnderecoMapper enderecoMapper;

    public ProfessorDTO toDTO(Professor obj) {
        ProfessorDTO dto = new ProfessorDTO();
        dto.setId(obj.getId());
        dto.setNome(obj.getNome());
        dto.setCpf(obj.getCpf());
        dto.setEmail(obj.getEmail());
        dto.setTelefone(obj.getTelefone());
        dto.setEnderecoDTO(enderecoMapper.toDTO(obj.getEndereco()));
        dto.setSalario(obj.getSalario());
        return dto;
    }

    public List<ProfessorDTO> toDTOList(List<Professor> objs) {
        List<ProfessorDTO> list = new ArrayList<>();

        for (Professor obj : objs) {
            ProfessorDTO dto = toDTO(obj);
            list.add(dto);
        }

        return list;
    }

    public Professor toOBJ(CreateProfessorRequest request) {
        return Professor.builder()
                .nome(request.getNome())
                .cpf(request.getCpf())
                .email(request.getEmail())
                .telefone(request.getTelefone())
                .salario(request.getSalario())
                .endereco(enderecoMapper.toOBJ(request.getEnderecoRequest()))
                .build();
    }
}
