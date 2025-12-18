package com.ubs.dnc.education.academic_api.service;

import com.ubs.dnc.education.academic_api.dto.request.CreateAlunoRequest;
import com.ubs.dnc.education.academic_api.dto.request.CreateEnderecoRequest;
import com.ubs.dnc.education.academic_api.dto.request.UpdateAlunoRequest;
import com.ubs.dnc.education.academic_api.dto.request.UpdateEnderecoRequest;
import com.ubs.dnc.education.academic_api.dto.response.AlunoDTO;
import com.ubs.dnc.education.academic_api.exception.AlunoNotFoundException;
import com.ubs.dnc.education.academic_api.mapper.AlunoMapper;
import com.ubs.dnc.education.academic_api.model.Aluno;
import com.ubs.dnc.education.academic_api.model.Endereco;
import com.ubs.dnc.education.academic_api.repository.AlunoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do AlunoService")
public class AlunoServiceTest {
    @Mock
    private AlunoRepository repository;

    @Mock
    private ViaCepService viaCepService;

    @Mock
    private AlunoMapper mapper;

    @InjectMocks
    private AlunoService service;

    private Aluno aluno;
    private AlunoDTO alunoDTO;
    private CreateAlunoRequest createRequest;
    private UpdateAlunoRequest updateRequest;
    private Endereco endereco;

    @BeforeEach
    void setup() {
        endereco = Endereco.builder()
                .cep("36180000")
                .bairro("Centro")
                .cidade("Rio Pomba")
                .estado("MG")
                .build();

        aluno = Aluno.builder()
                .id(1L)
                .nome("João Silva")
                .cpf("12345678901")
                .email("joao@email.com")
                .telefone("11999999999")
                .endereco(endereco)
                .matricula("20250001")
                .build();

        alunoDTO = AlunoDTO.builder()
                .id(1L)
                .nome("João Silva")
                .cpf("12345678901")
                .email("joao@email.com")
                .telefone("11999999999")
                .matricula("20250001")
                .build();

        CreateEnderecoRequest enderecoRequest = new CreateEnderecoRequest("36180000");

        createRequest = CreateAlunoRequest.builder()
                .nome("João Silva")
                .cpf("12345678901")
                .email("joao@email.com")
                .telefone("11999999999")
                .enderecoRequest(enderecoRequest)
                .build();

        UpdateEnderecoRequest updateEnderecoRequest = new UpdateEnderecoRequest("36180000");

        updateRequest = UpdateAlunoRequest.builder()
                .nome("João Silva Atualizado")
                .cpf("12345678901")
                .email("joao.atualizado@email.com")
                .telefone("11988888888")
                .enderecoRequest(updateEnderecoRequest)
                .build();
    }

    @Test
    @DisplayName("Deve cadastrar aluno com sucesso e buscar endereço via CEP")
    void deveCadastrarAlunoComSucesso() {
        // Arrange
        Aluno alunoSemEndereco = Aluno.builder()
                .nome("João Silva")
                .cpf("12345678901")
                .endereco(Endereco.builder().cep("36180000").build())
                .build();

        when(mapper.toOBJ(createRequest)).thenReturn(alunoSemEndereco);
        when(viaCepService.buscarEndereco("36180000")).thenReturn(endereco);
        when(repository.save(any(Aluno.class))).thenReturn(aluno);
        when(mapper.toDTO(aluno)).thenReturn(alunoDTO);

        // Act
        AlunoDTO resultado = service.cadastrar(createRequest);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("João Silva");
        assertThat(resultado.getCpf()).isEqualTo("12345678901");

        // Verifica se o ViaCEP foi chamado com o CEP correto
        verify(viaCepService, times(1)).buscarEndereco("36180000");

        // Verifica se o aluno foi salvo
        verify(repository, times(1)).save(any(Aluno.class));

        // Verifica se o mapper foi chamado
        verify(mapper, times(1)).toOBJ(createRequest);
        verify(mapper, times(1)).toDTO(aluno);
    }

    @Test
    @DisplayName("Deve listar todos os alunos")
    void deveListarTodosOsAlunos() {
        // Arrange
        Aluno aluno2 = Aluno.builder()
                .id(2L)
                .nome("Maria Santos")
                .build();

        List<Aluno> alunos = Arrays.asList(aluno, aluno2);
        List<AlunoDTO> alunosDTO = Arrays.asList(alunoDTO);

        when(repository.findAll()).thenReturn(alunos);
        when(mapper.toDTOList(alunos)).thenReturn(alunosDTO);

        // Act
        List<AlunoDTO> resultado = service.listarTodos();

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado).isNotEmpty();

        verify(repository, times(1)).findAll();
        verify(mapper, times(1)).toDTOList(alunos);
    }

    @Test
    @DisplayName("Deve buscar aluno por ID com sucesso")
    void deveBuscarAlunoPorIdComSucesso() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(aluno));
        when(mapper.toDTO(aluno)).thenReturn(alunoDTO);

        // Act
        AlunoDTO resultado = service.buscarPorId(1L);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("João Silva");

        verify(repository, times(1)).findById(1L);
        verify(mapper, times(1)).toDTO(aluno);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar aluno inexistente")
    void deveLancarExcecaoAoBuscarAlunoInexistente() {
        // Arrange
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.buscarPorId(999L))
                .isInstanceOf(AlunoNotFoundException.class)
                .hasMessageContaining("não encontrado");

        verify(repository, times(1)).findById(999L);
        verify(mapper, never()).toDTO(any());
    }

    @Test
    @DisplayName("Deve atualizar aluno sem mudar CEP")
    void deveAtualizarAlunoSemMudarCep() {
        // Arrange
        UpdateEnderecoRequest mesmoEndereco = new UpdateEnderecoRequest("36180000"); // Mesmo CEP
        UpdateAlunoRequest updateComMesmoCep = UpdateAlunoRequest.builder()
                .nome("João Silva Atualizado")
                .cpf("12345678901")
                .email("joao.novo@email.com")
                .telefone("11977777777")
                .enderecoRequest(mesmoEndereco)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(aluno));
        when(mapper.toDTO(aluno)).thenReturn(alunoDTO);

        // Act
        service.atualizar(1L, updateComMesmoCep);

        // Assert
        verify(repository, times(1)).findById(1L);

        // Verifica que NÃO chamou ViaCEP (CEP não mudou)
        verify(viaCepService, never()).buscarEndereco(anyString());

        verify(mapper, times(1)).toDTO(aluno);
    }

    @Test
    @DisplayName("Deve atualizar aluno e buscar novo endereço quando CEP muda")
    void deveAtualizarAlunoEBuscarNovoEnderecoDifferenteCep() {
        // Arrange
        UpdateEnderecoRequest novoCep = new UpdateEnderecoRequest("01001000"); // CEP diferente
        UpdateAlunoRequest updateComNovoCep = UpdateAlunoRequest.builder()
                .nome("João Silva Atualizado")
                .cpf("12345678901")
                .email("joao.novo@email.com")
                .telefone("11977777777")
                .enderecoRequest(novoCep)
                .build();

        Endereco novoEndereco = Endereco.builder()
                .cep("01001000")
                .bairro("Sé")
                .cidade("São Paulo")
                .estado("SP")
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(aluno));
        when(viaCepService.buscarEndereco("01001000")).thenReturn(novoEndereco);
        when(mapper.toDTO(aluno)).thenReturn(alunoDTO);

        // Act
        service.atualizar(1L, updateComNovoCep);

        // Assert
        verify(repository, times(1)).findById(1L);

        // Verifica que CHAMOU ViaCEP (CEP mudou)
        verify(viaCepService, times(1)).buscarEndereco("01001000");

        verify(mapper, times(1)).toDTO(aluno);

        // Verifica se o endereço foi atualizado
        assertThat(aluno.getEndereco().getCep()).isEqualTo("01001000");
        assertThat(aluno.getEndereco().getCidade()).isEqualTo("São Paulo");
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar aluno inexistente")
    void deveLancarExcecaoAoAtualizarAlunoInexistente() {
        // Arrange
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.atualizar(999L, updateRequest))
                .isInstanceOf(AlunoNotFoundException.class)
                .hasMessageContaining("Aluno com ID 999 não encontrado");

        verify(repository, times(1)).findById(999L);
        verify(viaCepService, never()).buscarEndereco(anyString());
    }

    @Test
    @DisplayName("Deve remover aluno com sucesso")
    void deveRemoverAlunoComSucesso() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(aluno));
        doNothing().when(repository).delete(aluno);

        // Act
        service.remover(1L);

        // Assert
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).delete(aluno);
    }

    @Test
    @DisplayName("Deve lançar exceção ao remover aluno inexistente")
    void deveLancarExcecaoAoRemoverAlunoInexistente() {
        // Arrange
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> service.remover(999L))
                .isInstanceOf(AlunoNotFoundException.class)
                .hasMessageContaining("Aluno com ID 999 não encontrado");

        verify(repository, times(1)).findById(999L);
        verify(repository, never()).delete(any());
    }
}
