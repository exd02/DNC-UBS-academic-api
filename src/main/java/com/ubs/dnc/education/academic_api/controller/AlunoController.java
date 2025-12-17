package com.ubs.dnc.education.academic_api.controller;

import com.ubs.dnc.education.academic_api.dto.request.CreateAlunoRequest;
import com.ubs.dnc.education.academic_api.dto.request.UpdateAlunoRequest;
import com.ubs.dnc.education.academic_api.dto.response.AlunoDTO;
import com.ubs.dnc.education.academic_api.service.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/aluno")
@Tag(name = "Alunos", description = "Gerenciamento de Alunos")
public class AlunoController {
    @Autowired
    private AlunoService service;

    @GetMapping
    @Operation(summary = "Listar todos os alunos", description = "Retorna uma coleção de todos os alunos")
    public ResponseEntity<List<AlunoDTO>> listarTodos() {
        List<AlunoDTO> alunos = service.listarTodos();
        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar aluno pelo seu ID", description = "Retorna um aluno específico")
    public ResponseEntity<AlunoDTO> buscarPorId(@PathVariable Long id) {
        AlunoDTO aluno = service.buscarPorId(id);
        return ResponseEntity.ok(aluno);
    }

    @PostMapping
    @Operation(summary = "Cadastrar um novo aluno", description = "Cria um novo aluno no sistema")
    public ResponseEntity<AlunoDTO> cadastrar(@Valid @RequestBody CreateAlunoRequest request) {
        AlunoDTO cadastrado = service.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(cadastrado);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar aluno parcialmente", description = "Atualiza apenas os campos enviados")
    public ResponseEntity<AlunoDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UpdateAlunoRequest request) {
        AlunoDTO atualizado = service.atualizar(id, request);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover aluno", description = "Remove um aluno do sistema")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
