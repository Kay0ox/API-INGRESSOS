# API de Ingressos (Java 21 + Spring Boot)

API REST para venda de ingressos de eventos culturais, com controle de lotação, limite de compra por usuário, autenticação JWT e documentação via Swagger.

## Tecnologias

- Java 21 + Spring Boot 3 (Web, Data JPA, Security, Validation)
- Gradle
- PostgreSQL (ou MySQL)
- JWT (jjwt) e springdoc-openapi (Swagger UI)
- Lombok

## Destaques

- Arquitetura em camadas: controller, service, repository, DTOs e entidades
- Entidades: Usuário, Endereço, Local, Evento e Ingresso
- Login com JWT e rotas protegidas por autenticação
- Tratamento global de erros com `@RestControllerAdvice`

## Onde está o código

O projeto fica na pasta [`ingressos-api/`](./ingressos-api). O [README completo](./ingressos-api/README.md) explica a estrutura, os endpoints e como rodar.

Resumo para rodar:

```bash
cd ingressos-api
./gradlew bootRun
```

Configure o banco em `ingressos-api/src/main/resources/application.yml` antes de rodar.

## Autor

**Kayo Victor Vilar Silva**: [LinkedIn](https://www.linkedin.com/in/kayo-victor-vilar-525205373/) · [GitHub](https://github.com/Kay0ox)
