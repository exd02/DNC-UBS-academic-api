# Academic Management API

API REST para gerenciamento acadêmico de alunos e professores, desenvolvida seguindo as melhores práticas de mercado ensinadas no curso da DNC.

---

## Arquitetura e Boas Práticas

Este projeto implementa as boas práticas ensinadas nas aulas do curso:

### [Aula 26 - Persistência de Dados com Spring Data JPA](https://itsdnc.notion.site/Aula-26-Persist-ncia-de-Dados-com-Spring-Data-JPA-2cb1b4d4252c8153a1a1ed68bcc11a15)

- Uso de Spring Data JPA para persistência
- Repositories com interface `JpaRepository`
- Entidades com anotações JPA (`@Entity`, `@Id`, `@GeneratedValue`)
- Relacionamentos mapeados (`@Embedded`, herança com `@MappedSuperclass`)
- Configuração de datasource e properties

---

### [Aula 27 - Boas Práticas no Desenvolvimento de APIs REST](https://itsdnc.notion.site/Aula-27-Boas-Pr-ticas-no-Desenvolvimento-de-APIs-REST-2cb1b4d4252c81a0862ddbe3154479d2)

**Arquitetura em Camadas:**
```
Controller → Service → Repository → DB
    ↓          ↓
   DTO       Mapper
```

**Práticas implementadas:**

1. Uso de DTOs (Data Transfer Objects)
   - `CreateAlunoRequest` para criação
   - `UpdateAlunoRequest` para atualização
   - `AlunoDTO` para resposta
   - Separação clara entre modelo de domínio e API

2. Mapeamento com MapStruct
   - Conversão automática entre entidades e DTOs

3. Status HTTP Corretos
   - `201 Created` - POST (criação)
   - `200 OK` - GET, PATCH (consulta/atualização)
   - `204 No Content` - DELETE (remoção)
   - `404 Not Found` - Recurso não encontrado
   - `400 Bad Request` - Validação falhou
   - `502 Bad Gateway` - Erro em API externa

4. Versionamento de API
   - `/api/v1/aluno`
   - `/api/v1/professor`

5. Validações com Bean Validation
   - Campos obrigatórios
   - Validações de formato (CPF, email)

6. Tratamento Global de Exceções
   - `@RestControllerAdvice` para centralizar tratamento
   - Respostas de erro padronizadas

7. Documentação com Swagger/OpenAPI
   - Endpoints documentados
   - Schemas de request/response

8. Logs Estruturados
   - Logs em todos os endpoints
   - Informações de contexto (IDs, operações)

9. Testes Automatizados

10. Logs
   - O sistema implementa logs estruturados para as exceptions e acessos a API

---

### [Aula 30 - Consumindo JSON com ObjectMapper (Jackson)](https://itsdnc.notion.site/Aula-30-Consumindo-JSON-com-ObjectMapper-Jackson-2cb1b4d4252c81b7a344ef73e364f9cb)

- Uso de ObjectMapper para deserialização de JSON
- Anotações Jackson (`@JsonProperty`, `@JsonIgnoreProperties`)
- Mapeamento seletivo de campos da API externa

---

### [Aula 32 - Boas Práticas na Integração com APIs Externas](https://itsdnc.notion.site/Aula-32-Boas-Pr-ticas-na-Integra-o-com-APIs-Externas-2cb1b4d4252c812083aacef3a2fb0174)

**Práticas implementadas:**

1. Tratamento de Erros Específicos
   - Exceção para CEP inválido (`CepInvalidoException`)
   - Exceção para CEP não encontrado (`CepNaoEncontradoException`)
   - Exceção para erro na API (`CepErroConsultaException`)

2. Encapsulamento da Integração
   - Service dedicado (`ViaCepService`)
   - Lógica isolada e reutilizável
   - Fácil de mockar em testes

3. Logs de Integração
   - Logs de requisições para API externa
   - Logs de respostas e erros

---

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.4.1**
- **Spring Data JPA**
- **H2 Database**
- **MapStruct** (mapeamento de objetos)
- **Lombok** (redução de boilerplate)
- **Bean Validation** (validações)
- **Swagger/OpenAPI** (documentação)
- **JUnit 5 + Mockito** (testes unitários)
- **WireMock** (testes de integração com APIs)
- **SLF4J + Logback** (logs)

---

## Estrutura do Projeto

```
src/main/java/com/ubs/dnc/education/academic_api/
├── controller/          # Camada de apresentação (REST)
│   ├── AlunoController.java
│   └── ProfessorController.java
├── service/            # Camada de lógica de negócio
│   ├── AlunoService.java
│   ├── ProfessorService.java
│   └── ViaCepService.java
├── repository/         # Camada de acesso a dados
│   ├── AlunoRepository.java
│   └── ProfessorRepository.java
├── model/             # Entidades de domínio
│   ├── Pessoa.java
│   ├── Aluno.java
│   ├── Professor.java
│   └── Endereco.java
├── dto/               # Objetos de transferência
│   ├── request/       # DTOs de entrada
│   └── response/      # DTOs de saída
├── mapper/            # Conversores (MapStruct)
│   ├── AlunoMapper.java
│   └── ProfessorMapper.java
└── exception/         # Exceções e handlers
    ├── GlobalExceptionHandler.java
    ├── AlunoNotFoundException.java
    ├── ProfessorNotFoundException.java
    └── Cep*Exception.java

src/test/java/
├── controller/        # Testes de API (MockMvc)
├── service/          # Testes unitários (Mockito)
└── integration/      # Testes end-to-end
```

---

## Como Executar

### Pré-requisitos
- Java 21+
- Maven 3.X+

### 1. Clone o repositório
```bash
git clone <repository-url>
cd academic-api
```

### 2. Execute a aplicação
```bash
mvn spring-boot:run
```

### 3. Acesse a documentação
```
http://localhost:8080/swagger-ui.html
```

---

## Diferenciais Implementados

1. Código limpo e organizado seguindo princípios SOLID
2. Separação de responsabilidades com camadas bem definidas
3. Testes abrangentes
4. Documentação completa (Swagger + README)
5. Logs profissionais estruturados e informativos
6. Tratamento robusto de erros com exceções específicas
7. Integração com API externa (ViaCEP)
8. Validações completas com Bean Validation
9. Padrão REST com status HTTP corretos
10. Versionamento de API (`/api/v1`)

---

Desenvolvido como projeto do curso da plataforma DNC - Capacitação Trainee UBS.
