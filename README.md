# Sistema de Agendamento de Consultas

API e interface web em Spring Boot para cadastro de pacientes, medicos e consultas, com autenticacao, controle de perfis, Swagger/OpenAPI, testes automatizados e relatorio JaCoCo.

## Tecnologias

- Java 21
- Spring Boot 3.5
- Spring Web
- Spring Security
- Spring Data JPA
- Thymeleaf
- PostgreSQL
- H2 para testes
- Springdoc OpenAPI / Swagger UI
- JaCoCo

## Como executar

Requisitos:

- JDK 21
- PostgreSQL ou variaveis de banco configuradas

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Testes e cobertura:

```powershell
.\mvnw.cmd clean test
```

Relatorio JaCoCo:

```text
target/site/jacoco/index.html
```

## URLs

| Recurso | URL local |
|---|---|
| Login | `http://localhost:8080/login` |
| Dashboard | `http://localhost:8080/dashboard` |
| Swagger UI | `http://localhost:8080/swagger-ui/index.html` |
| OpenAPI JSON | `http://localhost:8080/v3/api-docs` |
| JaCoCo | `target/site/jacoco/index.html` |

Deploy Railway:

```text
https://agendamentos-de-consultas-production.up.railway.app
```

Swagger no deploy:

```text
https://agendamentos-de-consultas-production.up.railway.app/swagger-ui/index.html
```

## Credenciais

Credenciais padrao de desenvolvimento:

| Perfil | Usuario | Senha |
|---|---|---|
| ADMIN | `admin` | `admin123` |
| ATENDENTE | `atendente` | `atendente123` |

Variaveis para sobrescrever no ambiente:

- `APP_SECURITY_ADMIN_USERNAME`
- `APP_SECURITY_ADMIN_PASSWORD`
- `APP_SECURITY_ATENDENTE_USERNAME`
- `APP_SECURITY_ATENDENTE_PASSWORD`

## Endpoints REST

| Metodo | Endpoint | Perfil | Descricao |
|---|---|---|---|
| GET | `/api/pacientes` | ADMIN, ATENDENTE | Lista pacientes |
| POST | `/api/pacientes` | ADMIN, ATENDENTE | Cadastra paciente |
| GET | `/api/medicos` | ADMIN, ATENDENTE | Lista medicos |
| POST | `/api/medicos` | ADMIN | Cadastra medico |
| GET | `/api/consultas` | ADMIN, ATENDENTE | Lista consultas |
| POST | `/api/consultas` | ADMIN, ATENDENTE | Agenda consulta |
| PATCH | `/api/consultas/{id}/status` | ADMIN | Atualiza status |

## Exemplos cURL

Listar pacientes:

```bash
curl -u admin:admin123 http://localhost:8080/api/pacientes
```

Cadastrar paciente:

```bash
curl -u admin:admin123 -X POST http://localhost:8080/api/pacientes \
  -H "Content-Type: application/json" \
  -d '{"nome":"Ana Silva","email":"ana@email.com","telefone":"(85) 99999-0000","dataNascimento":"1995-03-10"}'
```

Cadastrar medico:

```bash
curl -u admin:admin123 -X POST http://localhost:8080/api/medicos \
  -H "Content-Type: application/json" \
  -d '{"nome":"Dr Joao","especialidade":"Cardiologia","crm":"CRM123","email":"joao@email.com"}'
```

Agendar consulta:

```bash
curl -u admin:admin123 -X POST http://localhost:8080/api/consultas \
  -H "Content-Type: application/json" \
  -d '{"pacienteId":1,"medicoId":1,"dataHora":"2099-06-10T09:00:00","observacoes":"Primeira consulta"}'
```

Atualizar status:

```bash
curl -u admin:admin123 -X PATCH http://localhost:8080/api/consultas/1/status \
  -H "Content-Type: application/json" \
  -d '{"status":"CONFIRMADA"}'
```

## Banco de dados

O banco principal da aplicacao e PostgreSQL. O arquivo `.env.example` lista as variaveis aceitas:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `DATABASE_PUBLIC_URL`
- `DATABASE_URL`
- `PGUSER`
- `PGPASSWORD`

## Estrutura

- `model`: entidades JPA
- `repository`: repositories Spring Data
- `service`: regras de negocio
- `api`: controllers REST, DTOs e tratamento de erro
- `web`: controllers MVC e formularios Thymeleaf
- `config`: seguranca, datasource e OpenAPI
