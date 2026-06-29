# Ingressos API — Gestão de Ingressos (Eventos Culturais)

API REST em **Java 21 + Spring Boot 3.x + Gradle** para venda de ingressos de eventos
culturais, com controle de lotação, limite de compra por usuário, autenticação JWT
e documentação interativa via Swagger/OpenAPI.

## Stack

- Java 21
- Spring Boot 3.3.x (Web, Data JPA, Security, Validation)
- Gradle
- PostgreSQL (padrão) ou MySQL (alternativa pronta no `application.yml`)
- JWT (`io.jsonwebtoken` / jjwt)
- springdoc-openapi (Swagger UI)
- Lombok

## Estrutura do projeto

```
src/main/java/com/ingressos/api/
├── IngressosApplication.java
├── config/            → SecurityConfig, OpenApiConfig
├── security/          → TokenService, JwtAuthenticationFilter
├── model/
│   ├── entity/         → Usuario, Endereco, Local, Evento, Ingresso, Role
│   └── dto/             → FormDTOs (entrada, com validação) e DTOs (saída)
├── repository/        → Spring Data JPA repositories
├── service/           → regras de negócio
├── controller/        → endpoints REST
└── exception/         → BusinessException, ResourceNotFoundException,
                          ErrorResponse, GlobalExceptionHandler (@RestControllerAdvice)
```

## Entidades e relacionamentos

| Entidade  | Relacionamento |
|-----------|-----------------|
| `Usuario` | `@OneToOne` com `Endereco` · `@OneToMany` com `Ingresso` |
| `Endereco`| pertence a um `Usuario` |
| `Local`   | `@OneToMany` com `Evento` (um local recebe vários eventos) |
| `Evento`  | `@ManyToOne` com `Local` · `@OneToMany` com `Ingresso` |
| `Ingresso`| entidade de transação central: `@ManyToOne` com `Usuario` e `@ManyToOne` com `Evento` |

## Regra de negócio crítica (`IngressoService`)

Ao chamar `POST /ingressos`, antes de emitir o ingresso o sistema valida, **nesta ordem**:

1. **Capacidade máxima**: conta quantos ingressos já foram vendidos para o evento e
   compara com a capacidade efetiva, que é o **menor valor** entre
   `Evento.capacidadeMaxima` e `Local.capacidadeMaxima` (a capacidade do local nunca
   pode ser ultrapassada, mesmo que o evento tenha configurado um valor maior).
   Se atingida → `400 Bad Request`.
2. **Limite por usuário**: um mesmo usuário pode comprar **no máximo 5 ingressos**
   para o mesmo evento. Acima disso → `400 Bad Request`.

Essas validações ficam centralizadas em `IngressoService.validarCapacidadeELimitePorUsuario(...)`.

## Métricas em memória (`GET /ingressos/resumo`)

Calculadas com **streams Java**, sobre a lista completa retornada do banco (sem
agregações SQL), em `IngressoService.obterResumo()`:

- `totalIngressosVendidos`
- `receitaTotal` (soma de `valorPago`)
- `taxaOcupacaoMediaPercentual` (média, entre os eventos com venda, de `vendidos / capacidadeEfetiva * 100`)
- `eventoComMaisVendas` (id, nome e quantidade vendida do evento líder em vendas)

## Segurança (JWT)

- `POST /usuarios` — público (cadastro de cliente)
- `POST /auth/login` — público (retorna o token)
- `/swagger-ui/**`, `/v3/api-docs/**` — público (documentação)
- **Todas as demais rotas** exigem o header:

```
Authorization: Bearer <TOKEN>
```

`Usuario` implementa `UserDetails` diretamente. `TokenService` gera/valida o JWT
(HS256). `JwtAuthenticationFilter` intercepta toda requisição, valida o token e
popula o `SecurityContext`.

## Tratamento de erros

`GlobalExceptionHandler` (`@RestControllerAdvice`) padroniza todas as respostas de
erro em JSON, por exemplo:

```json
{
  "timestamp": "2026-06-25T10:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Capacidade máxima do local/evento já foi atingida...",
  "path": "/ingressos"
}
```

- `400` → falha de validação (`@Valid`) ou violação de regra de negócio (`BusinessException`)
- `404` → registro não encontrado (`ResourceNotFoundException`)
- `401` → credenciais inválidas / token ausente ou expirado

## Configuração do banco de dados

Edite `src/main/resources/application.yml` (ou use variáveis de ambiente):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ingressos_db
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:postgres}
```

Para usar **MySQL**, comente o bloco do PostgreSQL e descomente o bloco MySQL já
deixado pronto (com `com.mysql:mysql-connector-j`, já incluído no `build.gradle`).

Variáveis de ambiente suportadas:

| Variável | Descrição | Padrão |
|---|---|---|
| `DB_USERNAME` | usuário do banco | `postgres` |
| `DB_PASSWORD` | senha do banco | `postgres` |
| `JWT_SECRET` | chave secreta do JWT (use uma forte em produção) | valor de exemplo |
| `JWT_EXPIRATION_MS` | validade do token em ms | `86400000` (24h) |

## Como rodar

```bash
# 1. Crie o banco (Postgres como exemplo)
createdb ingressos_db

# 2. Exporte as variáveis (opcional, há defaults)
export DB_USERNAME=postgres
export DB_PASSWORD=postgres
export JWT_SECRET="uma-chave-bem-grande-e-aleatoria-aqui"

# 3. Rode a aplicação
./gradlew bootRun
```

> Este projeto foi gerado fora de um ambiente com acesso ao Maven Central, então as
> dependências do Gradle ainda não foram baixadas/compiladas aqui. Ao rodar
> `./gradlew bootRun` (ou abrir no IntelliJ/VS Code) na sua máquina, o Gradle
> baixará tudo normalmente.

A aplicação sobe em `http://localhost:8080`.

## Documentação interativa

Swagger UI: **http://localhost:8080/swagger-ui.html**
OpenAPI JSON: **http://localhost:8080/v3/api-docs**

## Fluxo de teste sugerido (cURL)

```bash
# 1) Cadastrar usuário (rota pública)
curl -X POST http://localhost:8080/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Maria Silva",
    "email": "maria@email.com",
    "senha": "senha123",
    "endereco": {
      "rua": "Av. Paulista",
      "numero": "1000",
      "cidade": "São Paulo",
      "estado": "SP",
      "cep": "01310-100"
    }
  }'

# 2) Login (rota pública)
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "maria@email.com", "senha": "senha123"}'
# -> retorna { "token": "...", "tipo": "Bearer", "expiraEmMs": 86400000 }

TOKEN="<cole o token aqui>"

# 3) Cadastrar um Local (rota protegida)
curl -X POST http://localhost:8080/locais \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nome": "Teatro Municipal", "endereco": "Praça Ramos de Azevedo", "cidade": "São Paulo", "capacidadeMaxima": 500}'

# 4) Cadastrar um Evento vinculado ao Local (rota protegida)
curl -X POST http://localhost:8080/eventos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nome": "Concerto de Verão", "dataEvento": "2026-12-20T20:00:00", "precoIngresso": 120.00, "capacidadeMaxima": 500, "localId": 1}'

# 5) Comprar um ingresso (rota protegida)
curl -X POST http://localhost:8080/ingressos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"eventoId": 1}'

# 6) Ver o resumo estatístico (rota protegida)
curl -X GET http://localhost:8080/ingressos/resumo \
  -H "Authorization: Bearer $TOKEN"
```

## Padrão DTO

- **FormDTO** (entrada): `UsuarioFormDTO`, `EnderecoFormDTO`, `LocalFormDTO`,
  `EventoFormDTO`, `IngressoFormDTO`, `LoginFormDTO` — todos com anotações
  Jakarta Validation (`@NotBlank`, `@Email`, `@NotNull`, `@Size`, `@Min`,
  `@DecimalMin`, `@Future`, `@Pattern`).
- **DTO** (saída): `UsuarioDTO`, `EnderecoDTO`, `LocalDTO`, `EventoDTO`,
  `IngressoDTO`, `ResumoIngressosDTO` — cada um com um `fromEntity(...)` estático
  para conversão a partir da entidade.

## Possíveis próximos passos

- Adicionar paginação em `GET /eventos` e `GET /locais`.
- Restringir `POST /locais` e `POST /eventos` ao papel `ORGANIZADOR`/`ADMIN`
  (já existe o campo `Role` no `Usuario`; basta adicionar `@PreAuthorize`).
- Testes automatizados (unitários para `IngressoService` e de integração para
  os controllers).
