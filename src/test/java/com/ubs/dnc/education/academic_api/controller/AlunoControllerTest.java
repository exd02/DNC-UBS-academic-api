package com.ubs.dnc.education.academic_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubs.dnc.education.academic_api.dto.request.CreateAlunoRequest;
import com.ubs.dnc.education.academic_api.dto.request.CreateEnderecoRequest;
import com.ubs.dnc.education.academic_api.dto.request.UpdateAlunoRequest;
import com.ubs.dnc.education.academic_api.dto.response.AlunoDTO;
import com.ubs.dnc.education.academic_api.dto.response.EnderecoDTO;
import com.ubs.dnc.education.academic_api.exception.AlunoNotFoundException;
import com.ubs.dnc.education.academic_api.service.AlunoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlunoController.class)
@DisplayName("Testes do AlunoController")
class AlunoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AlunoService service;

    private AlunoDTO objDTO;
    private CreateAlunoRequest createRequest;
    private UpdateAlunoRequest updateRequest;

    @BeforeEach
    void setup() {
        EnderecoDTO enderecoDTO = EnderecoDTO.builder()
                .cep("36180000")
                .bairro("")
                .cidade("Rio Pomba")
                .estado("MG")
                .build();

        objDTO = AlunoDTO.builder()
                .id(1L)
                .nome("João Silva")
                .cpf("12345678901")
                .email("joao@email.com")
                .telefone("11999999999")
                .enderecoDTO(enderecoDTO)
                .matricula("20250001")
                .build();

        CreateEnderecoRequest enderecoRequest = new CreateEnderecoRequest("36180000");

        createRequest = CreateAlunoRequest.builder()
                .nome("João Silva")
                .cpf("12345678901")
                .email("joao@email.com")
                .telefone("11999999999")
                .enderecoRequest(enderecoRequest)
                .matricula("2022001")
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/aluno - Deve cadastrar novo aluno e pegar corretamente os dados a partir da API externa")
    void deveCadastrarNovoAluno() throws Exception {
        when(service.cadastrar(any(CreateAlunoRequest.class))).thenReturn(objDTO);

        mockMvc.perform(post("/api/v1/aluno")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.endereco.estado").value("MG"))
                .andExpect(jsonPath("$.matricula").value("20250001"));

        verify(service, times(1)).cadastrar(any(CreateAlunoRequest.class));
    }

    @Test
    @DisplayName("GET /api/v1/aluno/{id} - Deve retornar 404 quando aluno não existe")
    void deveRetornar404QuandoAlunoNaoExiste() throws Exception {
        when(service.buscarPorId(999L))
                .thenThrow(new AlunoNotFoundException("Aluno com ID 999 não encontrado"));

        mockMvc.perform(get("/api/v1/aluno/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(service, times(1)).buscarPorId(999L);
    }

    @Test
    @DisplayName("GET /api/v1/aluno/{id} - Deve buscar aluno por ID")
    void deveBuscarAlunoPorId() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(objDTO);

        mockMvc.perform(get("/api/v1/aluno/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.cpf").value("12345678901"))
                .andExpect(jsonPath("$.email").value("joao@email.com"))
                .andExpect(jsonPath("$.telefone").value("11999999999"))
                .andExpect(jsonPath("$.matricula").value("20250001"))
                .andExpect(jsonPath("$.endereco.cep").value("36180000"))
                .andExpect(jsonPath("$.endereco.cidade").value("Rio Pomba"))
                .andExpect(jsonPath("$.endereco.estado").value("MG"));

        verify(service, times(1)).buscarPorId(1L);
    }
}
