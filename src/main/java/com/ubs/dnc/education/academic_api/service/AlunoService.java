package com.ubs.dnc.education.academic_api.service;

import com.ubs.dnc.education.academic_api.dto.request.CreateAlunoRequest;
import com.ubs.dnc.education.academic_api.dto.request.UpdateAlunoRequest;
import com.ubs.dnc.education.academic_api.dto.response.AlunoDTO;
import com.ubs.dnc.education.academic_api.exception.AlunoNotFoundException;
import com.ubs.dnc.education.academic_api.mapper.AlunoMapper;
import com.ubs.dnc.education.academic_api.model.Aluno;
import com.ubs.dnc.education.academic_api.model.Endereco;
import com.ubs.dnc.education.academic_api.repository.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository repository;

    @Autowired
    private ViaCepService viaCepService;

    @Autowired
    private AlunoMapper alunoMapper;

    // Create
    public AlunoDTO cadastrar(CreateAlunoRequest request) {
        Aluno obj = alunoMapper.toOBJ(request);

        Endereco enderecoCompleto = viaCepService.buscarEndereco(obj.getEndereco().getCep());
        obj.setEndereco(enderecoCompleto);

        Aluno salvo = repository.save(obj);
        return alunoMapper.toDTO(salvo);
    }

    // Read
    public List<AlunoDTO> listarTodos() {
        List<Aluno> list = repository.findAll();
        return alunoMapper.toDTOList(list);
    }

    public AlunoDTO buscarPorId(Long id) {
        Aluno aluno = repository.findById(id).orElseThrow(() -> new AlunoNotFoundException("Aluno com ID " + id + " não encontrado"));
        return alunoMapper.toDTO(aluno);
    }

    // Update
    public AlunoDTO atualizar(Long id, UpdateAlunoRequest alunoAtualizado) {
        Aluno alunoExistente = repository.findById(id).orElseThrow(() -> new AlunoNotFoundException("Aluno com ID " + id + " não encontrado"));

        alunoExistente.setNome(alunoAtualizado.getNome());

        alunoExistente.setCpf(alunoAtualizado.getCpf());
        alunoExistente.setEmail(alunoAtualizado.getEmail());
        alunoExistente.setTelefone(alunoAtualizado.getTelefone());

        // se o CEP mudar, buscar o novo endereço
        if (!Objects.equals(alunoExistente.getEndereco().getCep(), alunoAtualizado.getEnderecoRequest().getCep())) {
            Endereco enderecoCompleto = viaCepService.buscarEndereco(alunoAtualizado.getEnderecoRequest().getCep());
            alunoExistente.setEndereco(enderecoCompleto);
        }

        return alunoMapper.toDTO(alunoExistente);
    }

    // Delete
    public void remover(long id) {
        Aluno aluno = repository.findById(id).orElseThrow(() -> new AlunoNotFoundException("Aluno com ID " + id + " não encontrado"));
        repository.delete(aluno);
    }
}
