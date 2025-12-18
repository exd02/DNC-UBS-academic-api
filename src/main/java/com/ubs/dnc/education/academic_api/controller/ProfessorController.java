package com.ubs.dnc.education.academic_api.controller;

import com.ubs.dnc.education.academic_api.dto.request.CreateProfessorRequest;
import com.ubs.dnc.education.academic_api.dto.request.UpdateProfessorRequest;
import com.ubs.dnc.education.academic_api.dto.response.ProfessorDTO;
import com.ubs.dnc.education.academic_api.service.ProfessorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/professor")
@Tag(name = "Professores", description = "Gerenciamento de Professores")
@Slf4j
public class ProfessorController {

    @Autowired
    private ProfessorService service;

    @GetMapping
    @Operation(summary = "Listar todos os professores", description = "Retorna uma coleção de todos os professores")
    public ResponseEntity<List<ProfessorDTO>> listarTodos() {
        log.info("GET /api/v1/professor - Listando todos os professores");
        List<ProfessorDTO> professores = service.listarTodos();
        log.info("Retornados {} professores", professores.size());
        return ResponseEntity.ok(professores);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar professor pelo seu ID", description = "Retorna um professor específico")
    public ResponseEntity<ProfessorDTO> buscarPorId(@PathVariable Long id) {
        log.info("GET /api/v1/professor/{} - Buscando professor", id);
        ProfessorDTO professor = service.buscarPorId(id);
        return ResponseEntity.ok(professor);
    }

    @PostMapping
    @Operation(summary = "Cadastrar um novo professor", description = "Cria um novo professor no sistema")
    public ResponseEntity<ProfessorDTO> cadastrar(@Valid @RequestBody CreateProfessorRequest request) {
        log.info("POST /api/v1/professor - Cadastrando professor: cpf={}", request.getCpf());
        ProfessorDTO cadastrado = service.cadastrar(request);
        log.info("Professor cadastrado com sucesso: id={}", cadastrado.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(cadastrado);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar professor parcialmente", description = "Atualiza apenas os campos enviados")
    public ResponseEntity<ProfessorDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProfessorRequest request) {
        log.info("PATCH /api/v1/professor/{} - Atualizando professor", id);
        ProfessorDTO atualizado = service.atualizar(id, request);
        log.info("Professor atualizado com sucesso: id={}", id);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover professor", description = "Remove um professor do sistema")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        log.info("DELETE /api/v1/professor/{} - Removendo professor", id);
        service.remover(id);
        log.info("Professor removido com sucesso: id={}", id);
        return ResponseEntity.noContent().build();
    }
}
