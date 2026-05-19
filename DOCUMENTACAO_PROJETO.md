# Documentação do Projeto

## 1. Tema

Sistema de agendamento de consultas médicas desenvolvido para projeto de faculdade, usando Spring Boot no backend, Thymeleaf no frontend e PostgreSQL na persistência dos dados.

## 2. Objetivo do sistema

O objetivo do projeto é permitir o cadastro de pacientes e médicos, além do agendamento e acompanhamento de consultas médicas em uma interface web simples, organizada e funcional.

## 3. Problema resolvido

Em clínicas e consultórios, o controle manual de horários pode gerar conflitos, retrabalho e perda de informação. O sistema centraliza os registros e aplica regras para impedir que o mesmo médico tenha duas consultas ativas no mesmo horário.

## 4. Tecnologias utilizadas

- Java 21
- Spring Boot 3
- Spring MVC
- Spring Data JPA
- Thymeleaf
- PostgreSQL
- H2 para testes
- HTML5
- CSS3

## 5. Funcionalidades implementadas

- Cadastro de pacientes
- Cadastro de médicos
- Agendamento de consultas
- Controle de status da consulta
- Listagem geral das consultas
- Indicadores rápidos no topo da interface
- Validação de conflitos de horário
- Integração com PostgreSQL
- Suporte a variáveis do Railway

## 6. Estrutura do backend

### 6.1 Entidades

#### Paciente

Campos:

- id
- nome
- email
- telefone
- dataNascimento

#### Medico

Campos:

- id
- nome
- especialidade
- crm
- email

#### Consulta

Campos:

- id
- paciente
- medico
- dataHora
- status
- observacoes

#### StatusConsulta

Enum usado para representar:

- AGENDADA
- CONFIRMADA
- CANCELADA

### 6.2 Camadas criadas

- `config`: configuração do datasource e leitura das variáveis do Railway
- `model`: entidades JPA e enum de status
- `repository`: acesso ao banco de dados
- `service`: regras de negócio
- `web`: controller da página e formulários

## 7. Regras de negócio

- O paciente não pode ser cadastrado com e-mail repetido.
- O médico não pode ser cadastrado com CRM repetido.
- A consulta deve ser marcada para uma data futura.
- Um médico não pode ter duas consultas ativas no mesmo horário.
- O status da consulta pode ser alterado para confirmada ou cancelada.

## 8. Estrutura do frontend

O frontend foi desenvolvido com Thymeleaf, HTML e CSS, sem necessidade de framework JavaScript.

### 8.1 Seções da tela principal

- Apresentação do sistema
- Cards com totais de pacientes, médicos e consultas
- Formulário de paciente
- Formulário de médico
- Formulário de consulta
- Tabela de consultas

### 8.2 Estilo visual

Foi criada uma interface com:

- visual claro
- cartões com transparência suave
- tipografia mais moderna
- layout responsivo
- destaque visual para status

## 9. Banco de dados

O sistema foi preparado para PostgreSQL e aceita:

- configuração JDBC padrão do Spring
- variáveis `DATABASE_PUBLIC_URL` e `DATABASE_URL` do Railway

Foi implementada uma conversão automática de URL no formato:

`postgresql://usuario:senha@host:porta/banco`

para:

`jdbc:postgresql://host:porta/banco?sslmode=require`

## 10. Arquivos principais criados ou ajustados

- `pom.xml`
- `src/main/resources/application.properties`
- `src/main/java/com/consultas/agendamento_api/config/DataSourceConfig.java`
- `src/main/java/com/consultas/agendamento_api/service/AgendamentoService.java`
- `src/main/java/com/consultas/agendamento_api/web/HomeController.java`
- `src/main/resources/templates/index.html`
- `src/main/resources/static/css/styles.css`
- `README.md`
- `.env.example`

## 11. Fluxo de uso do sistema

1. Cadastrar um paciente.
2. Cadastrar um médico.
3. Informar paciente, médico, data e observações no formulário de consulta.
4. Salvar o agendamento.
5. Acompanhar a consulta pela tabela e atualizar seu status quando necessário.

## 12. Como executar

1. Configurar as variáveis de ambiente do banco.
2. Executar o projeto com Maven ou pela IDE.
3. Acessar `http://localhost:8080`.

## 13. Resultado entregue

Foi implementado um sistema web completo para o contexto do trabalho acadêmico, com backend em Spring Boot, persistência em PostgreSQL, frontend integrado e documentação do funcionamento do projeto.

## 14. Melhorias futuras

- autenticação de usuários
- filtro de consultas por data
- tela separada para relatórios
- API REST completa
- dashboard administrativo com gráficos
