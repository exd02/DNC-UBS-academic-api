package com.ubs.dnc.education.academic_api.service;

import com.ubs.dnc.education.academic_api.dto.request.CreateProfessorRequest;
import com.ubs.dnc.education.academic_api.dto.request.UpdateProfessorRequest;
import com.ubs.dnc.education.academic_api.dto.response.ProfessorDTO;
import com.ubs.dnc.education.academic_api.exception.AlunoNotFoundException;
import com.ubs.dnc.education.academic_api.mapper.ProfessorMapper;
import com.ubs.dnc.education.academic_api.model.Endereco;
import com.ubs.dnc.education.academic_api.model.Professor;
import com.ubs.dnc.education.academic_api.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository repository;

    @Autowired
    private ViaCepService viaCepService;

    @Autowired
    private ProfessorMapper professorMapper;

    // Create
    public ProfessorDTO cadastrar(CreateProfessorRequest request) {
        Professor obj = professorMapper.toOBJ(request);

        Endereco enderecoCompleto = viaCepService.buscarEndereco(obj.getEndereco().getCep());
        obj.setEndereco(enderecoCompleto);

        Professor salvo = repository.save(obj);
        return professorMapper.toDTO(salvo);
    }

    // Read
    public List<ProfessorDTO> listarTodos() {
        List<Professor> list = repository.findAll();
        return professorMapper.toDTOList(list);
    }

    public ProfessorDTO buscarPorId(Long id) {
        Professor prof = repository.findById(id).orElseThrow(() -> new AlunoNotFoundException("Professor com ID " + id + " não encontrado"));
        return professorMapper.toDTO(prof);
    }

    // Update
    public ProfessorDTO atualizar(Long id, UpdateProfessorRequest profAtualizado) {
        Professor profExistente = repository.findById(id).orElseThrow(() -> new AlunoNotFoundException("Professor com ID " + id + " não encontrado"));

        profExistente.setNome(profAtualizado.getNome());

        profExistente.setCpf(profAtualizado.getCpf());
        profExistente.setEmail(profAtualizado.getEmail());
        profExistente.setTelefone(profAtualizado.getTelefone());

        // se o CEP mudar, buscar o novo endereço
        if (!Objects.equals(profExistente.getEndereco().getCep(), profAtualizado.getEnderecoRequest().getCep())) {
            Endereco enderecoCompleto = viaCepService.buscarEndereco(profAtualizado.getEnderecoRequest().getCep());
            profExistente.setEndereco(enderecoCompleto);
        }

        return professorMapper.toDTO(profExistente);
    }

    // Delete
    public void remover(long id) {
        Professor prof = repository.findById(id).orElseThrow(() -> new AlunoNotFoundException("Professor com ID " + id + " não encontrado"));
        repository.delete(prof);
    }
}
