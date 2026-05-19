# Sistema de Agendamento de Consultas

Projeto academico em Spring Boot para cadastro de pacientes, medicos e consultas. A entrega contem entidades, repositories, controllers MVC e REST, documentacao Swagger, PostgreSQL como banco principal e README atualizado.

## Funcionalidades

- Cadastro de pacientes.
- Cadastro de medicos.
- Agendamento de consultas.
- Atualizacao de status da consulta.
- Validacao para evitar conflito de horario do mesmo medico.
- Interface web com Thymeleaf.
- API REST documentada com Swagger.

## Tecnologias

- Java 21
- Spring Boot 3
- Spring Web
- Spring Data JPA
- Thymeleaf
- PostgreSQL
- H2 Database
- Springdoc OpenAPI / Swagger UI

## Estrutura do projeto

- `model`: entidades `Paciente`, `Medico`, `Consulta` e `StatusConsulta`
- `repository`: repositories JPA
- `service`: regras de negocio
- `web`: controller MVC e formularios
- `api`: controllers REST, DTOs e tratamento de erro
- `config`: datasource e Swagger

## Endpoints REST

- `GET /api/pacientes`
- `POST /api/pacientes`
- `GET /api/medicos`
- `POST /api/medicos`
- `GET /api/consultas`
- `POST /api/consultas`
- `PATCH /api/consultas/{id}/status`

## Swagger

- `http://localhost:8080/swagger-ui.html`
- `http://localhost:8080/v3/api-docs`

## Banco de dados

O banco principal da aplicacao e PostgreSQL.

Use o arquivo `.env.example` como referencia para criar seu `.env`.

Variaveis aceitas:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `DATABASE_PUBLIC_URL`
- `DATABASE_URL`
- `PGUSER`
- `PGPASSWORD`

## Como executar

### Windows com PostgreSQL

```powershell
.\mvnw.cmd spring-boot:run
```

## Observacoes

- O `.env` esta ignorado no git.
- O `.env.example` nao possui credenciais reais.
- O H2 permanece apenas para os testes automatizados.
