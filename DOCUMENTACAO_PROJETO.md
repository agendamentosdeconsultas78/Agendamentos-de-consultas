# Documentacao do Projeto

## 1. Visao geral

Projeto academico desenvolvido em Java 21 com Spring Boot para gerenciar pacientes, medicos e consultas. O sistema possui interface web com Thymeleaf, API REST documentada com Swagger, persistencia com PostgreSQL e testes automatizados com H2.

## 2. Objetivo do sistema

O sistema resolve o problema de organizacao de consultas em clinicas e consultorios, centralizando os cadastros e aplicando regras para evitar conflitos de horario do mesmo medico.

## 3. Arquitetura adotada

O projeto foi organizado em camadas para separar responsabilidades:

- `config`: configuracoes de infraestrutura, banco, Swagger e seguranca.
- `model`: entidades JPA e enum de status.
- `repository`: acesso ao banco com Spring Data JPA.
- `service`: regras de negocio e validacoes centrais.
- `api`: endpoints REST, DTOs e tratamento padrao de erros.
- `web`: controller MVC e formularios da interface Thymeleaf.

Essa divisao facilita manutencao, testes e explicacao em banca porque cada parte do sistema tem um papel claro.

## 4. Fluxo do sistema

1. O usuario acessa a interface web ou a API.
2. O Spring Security autentica o usuario.
3. O controller recebe a requisicao.
4. Os dados de entrada sao validados com Bean Validation.
5. O service aplica as regras de negocio.
6. O repository conversa com o banco usando JPA.
7. O sistema retorna uma pagina HTML ou um DTO JSON.

## 5. Entidades do dominio

### Paciente

Representa a pessoa que sera atendida.

Campos:

- `id`: chave primaria.
- `nome`: nome completo.
- `email`: e-mail unico.
- `telefone`: contato.
- `dataNascimento`: validada para estar no passado.

Anotacoes principais:

- `@Entity`: transforma a classe em entidade JPA.
- `@Table(name = "pacientes")`: define o nome da tabela.
- `@Id`: marca a chave primaria.
- `@GeneratedValue`: gera o identificador automaticamente.
- `@Column`: define restricoes de coluna.
- `@Email`, `@NotBlank`, `@Past`: validacoes de dados.

### Medico

Representa o profissional de saude.

Campos:

- `id`
- `nome`
- `especialidade`
- `crm`
- `email`

Regras importantes:

- `crm` e unico.
- `email` precisa ser valido.

### Consulta

Representa o agendamento entre paciente e medico.

Campos:

- `id`
- `paciente`
- `medico`
- `dataHora`
- `status`
- `observacoes`

Relacionamentos:

- `@ManyToOne(fetch = FetchType.LAZY)` em `paciente`.
- `@ManyToOne(fetch = FetchType.LAZY)` em `medico`.

Isso significa que varias consultas podem apontar para um mesmo medico ou paciente.

### StatusConsulta

Enum usado para padronizar o status da consulta:

- `AGENDADA`
- `CONFIRMADA`
- `CANCELADA`

## 6. DTOs e Forms

No projeto existem dois grupos diferentes de objetos para entrada e saida.

### Forms

Usados para receber dados enviados pela interface web e pela API.

- `PacienteForm`
- `MedicoForm`
- `ConsultaForm`

Esses objetos concentram as validacoes de entrada com anotacoes como:

- `@NotBlank`
- `@NotNull`
- `@Email`
- `@Past`
- `@Future`
- `@DateTimeFormat`

### DTOs de resposta

Usados para devolver apenas os dados que a API precisa expor.

- `PacienteResponse`
- `MedicoResponse`
- `ConsultaResponse`
- `ApiErrorResponse`
- `AtualizarStatusRequest`

Por que isso e importante:

- evita expor a entidade inteira diretamente;
- desacopla a API da estrutura interna do banco;
- deixa a resposta mais limpa e controlada;
- facilita evolucao futura.

## 7. Controllers

### Controllers REST

- `PacienteController`
- `MedicoController`
- `ConsultaController`

Anotacoes mais importantes:

- `@RestController`: retorna JSON.
- `@RequestMapping`: define a rota base.
- `@GetMapping`: lista registros.
- `@PostMapping`: cria registros.
- `@PatchMapping`: atualiza parcialmente.
- `@RequestBody`: le JSON da requisicao.
- `@PathVariable`: captura valores da URL.
- `@ResponseStatus(HttpStatus.CREATED)`: retorna status 201 ao criar.
- `@Valid`: dispara as validacoes automaticamente.

### Controller MVC

- `HomeController`

Ele alimenta a pagina `index.html`, recebe formularios HTML e devolve a view Thymeleaf.

Anotacoes mais importantes:

- `@Controller`: retorna paginas HTML.
- `@GetMapping("/")`: abre a tela principal.
- `@PostMapping`: processa formularios.
- `@ModelAttribute`: cria e injeta os objetos dos formularios no modelo.

## 8. Service e regras de negocio

A classe central e `AgendamentoService`.

Responsabilidades:

- cadastrar pacientes;
- cadastrar medicos;
- agendar consultas;
- atualizar status;
- listar dados;
- buscar consulta por id.

Regras aplicadas:

- nao permite paciente com e-mail duplicado;
- nao permite medico com CRM duplicado;
- nao permite consulta com data passada;
- nao permite duas consultas ativas para o mesmo medico no mesmo horario;
- exige que paciente e medico existam antes do agendamento.

Anotacoes importantes:

- `@Service`: marca a camada de negocio.
- `@Transactional`: garante consistencia na escrita.
- `@Transactional(readOnly = true)`: otimiza operacoes de leitura.

## 9. Repositories

Os repositories usam Spring Data JPA:

- `PacienteRepository`
- `MedicoRepository`
- `ConsultaRepository`

Eles herdam operacoes prontas e ainda declaram metodos por convencao de nomes, por exemplo:

- `existsByEmailIgnoreCase`
- `existsByCrmIgnoreCase`
- `findAllByOrderByNomeAsc`
- `findAllByOrderByDataHoraAsc`
- `existsByMedicoIdAndDataHoraAndStatusNot`

Esse ponto e forte em banca porque mostra uso correto do Spring Data sem SQL manual desnecessario.

## 10. Tratamento de erros

O projeto possui `ApiExceptionHandler` com `@RestControllerAdvice`.

Ele centraliza respostas de erro para a API REST.

Casos tratados:

- `RegraNegocioException`
- `MethodArgumentNotValidException`

Padrao de retorno:

- timestamp
- status HTTP
- descricao do erro
- lista de detalhes

## 11. Banco de dados

### Banco principal

- PostgreSQL

### Banco de teste

- H2 em memoria

### Configuracao especial

`DataSourceConfig` aceita:

- configuracao JDBC tradicional do Spring;
- variaveis `DATABASE_PUBLIC_URL` e `DATABASE_URL` do Railway;
- variaveis `PGUSER` e `PGPASSWORD`.

Tambem converte automaticamente a URL nativa do Railway para o formato JDBC.

## 12. Seguranca e cyber seguranca

Antes desta revisao o projeto nao tinha autenticacao nem autorizacao. Agora foi adicionada uma camada de seguranca com Spring Security.

### O que foi implementado

- autenticacao para interface web e API;
- `BCryptPasswordEncoder` para criptografar senhas;
- RBAC simples com dois papeis;
- controle de acesso por rota;
- Swagger protegido por login;
- usuarios configuraveis por propriedades e variaveis de ambiente.

### Perfis criados

- `ROLE_ADMIN`
- `ROLE_ATENDENTE`

### Regras de acesso

`ATENDENTE` pode:

- listar pacientes, medicos e consultas;
- cadastrar pacientes;
- agendar consultas;
- usar a interface principal;
- consultar a documentacao Swagger autenticado.

`ADMIN` pode tudo que o atendente faz e tambem:

- cadastrar medicos;
- atualizar status de consultas;
- usar endpoints administrativos protegidos.

### Onde isso aparece no codigo

- `SecurityConfig`
- `SecurityProperties`
- `application.properties`
- `.env.example`

### Por que BCrypt e importante

Porque senha nao deve ficar salva em texto puro. O BCrypt gera hash com salt embutido, dificultando ataques por dicionario e reutilizacao direta da senha vazada.

### Por que RBAC e importante

RBAC significa Role-Based Access Control. Em vez de liberar tudo para qualquer usuario, o sistema decide o que cada papel pode fazer. Isso segue o principio do menor privilegio.

## 13. Swagger e API

Endpoints principais:

- `GET /api/pacientes`
- `POST /api/pacientes`
- `GET /api/medicos`
- `POST /api/medicos`
- `GET /api/consultas`
- `POST /api/consultas`
- `PATCH /api/consultas/{id}/status`

Documentacao:

- `http://localhost:8080/swagger-ui.html`
- `http://localhost:8080/v3/api-docs`

Agora o OpenAPI inclui o esquema `basicAuth`, refletindo a autenticacao exigida.

## 14. Passo a passo de construcao do projeto

Esse roteiro pode ser apresentado como cronologia do grupo.

1. Definimos o problema: controlar pacientes, medicos e consultas.
2. Escolhemos Java 21 e Spring Boot para acelerar o backend.
3. Modelamos as entidades principais: `Paciente`, `Medico` e `Consulta`.
4. Criamos o enum `StatusConsulta` para padronizar o estado da consulta.
5. Criamos os repositories com Spring Data JPA.
6. Implementamos o service para centralizar regras de negocio.
7. Adicionamos validacoes com Bean Validation.
8. Criamos a interface web com Thymeleaf.
9. Criamos a API REST separada da interface.
10. Criamos DTOs para expor respostas controladas na API.
11. Adicionamos tratamento global de erros.
12. Configuramos PostgreSQL e suporte ao Railway.
13. Documentamos a API com Swagger.
14. Adicionamos testes com H2 e Mockito.
15. Reforcamos a parte de cyber seguranca com autenticacao, BCrypt e RBAC.

## 15. Divisao da apresentacao entre 4 integrantes

### Integrante 1: visao geral e arquitetura

Estuda e apresenta:

- objetivo do sistema;
- problema resolvido;
- tecnologias usadas;
- estrutura em camadas;
- fluxo completo de uma requisicao.

Frase forte para usar:

"Nos organizamos o projeto em camadas para separar interface, regra de negocio, persistencia e configuracao, deixando o sistema mais manutenivel e mais facil de testar."

### Integrante 2: entidades, JPA e banco

Estuda e apresenta:

- `Paciente`, `Medico`, `Consulta`, `StatusConsulta`;
- anotacoes JPA;
- relacionamentos `@ManyToOne`;
- banco PostgreSQL;
- H2 nos testes;
- `DataSourceConfig`.

Frase forte para usar:

"A modelagem foi feita primeiro no dominio, porque a estrutura das entidades guiou tanto o banco quanto as regras de negocio."

### Integrante 3: service, validacoes e regras de negocio

Estuda e apresenta:

- `AgendamentoService`;
- validacoes com `@Valid`, `@Future`, `@Past`, `@NotBlank`;
- conflito de horario do medico;
- duplicidade de email e CRM;
- `RegraNegocioException` e `ApiExceptionHandler`.

Frase forte para usar:

"A camada service concentra as regras do sistema para que controller e repository nao assumam responsabilidades de negocio."

### Integrante 4: API, DTOs e seguranca

Estuda e apresenta:

- controllers REST;
- diferenca entre `Form`, entidade e DTO;
- Swagger;
- `SecurityConfig`;
- `BCrypt`;
- RBAC com `ADMIN` e `ATENDENTE`.

Frase forte para usar:

"Na parte de seguranca nos aplicamos autenticacao e controle por papel, evitando acesso amplo demais e protegendo tanto a interface quanto a API."

## 16. Possiveis perguntas do professor com respostas

### 1. Por que usar DTO em vez de retornar a entidade?

Resposta:

Porque DTO controla o que a API expoe, reduz acoplamento e evita vazar detalhes internos da entidade.

### 2. Qual a diferenca entre `@Controller` e `@RestController`?

Resposta:

`@Controller` normalmente devolve view HTML. `@RestController` devolve JSON no corpo da resposta.

### 3. O que o `@Valid` faz?

Resposta:

Ele manda o Spring executar as validacoes declaradas no objeto de entrada antes de chamar a regra de negocio.

### 4. Por que usar `@Service`?

Resposta:

Para identificar a camada de negocio e concentrar as regras fora do controller.

### 5. O que o `@Transactional` resolve?

Resposta:

Garante consistencia no banco se uma operacao de escrita falhar no meio do processo.

### 6. O que significa RBAC?

Resposta:

Role-Based Access Control. O acesso e concedido com base no papel do usuario.

### 7. Qual a vantagem do BCrypt?

Resposta:

Ele protege senhas com hash e salt, evitando armazenamento em texto puro.

### 8. Por que usar repository em vez de SQL manual?

Resposta:

Porque Spring Data JPA reduz codigo repetitivo, padroniza acesso e acelera a manutencao.

### 9. Como o sistema evita conflito de horario?

Resposta:

Antes de salvar a consulta, o service consulta o repository para verificar se o medico ja possui consulta ativa naquele horario.

### 10. Por que usar H2 nos testes?

Resposta:

Porque permite executar testes rapidos e isolados sem depender do PostgreSQL real.

### 11. O que o `@ManyToOne` representa em `Consulta`?

Resposta:

Que varias consultas podem se relacionar com um mesmo medico ou paciente.

### 12. Onde esta a parte de cyber seguranca no projeto?

Resposta:

Na autenticacao com Spring Security, no hash de senha com BCrypt, no RBAC por papel e na protecao das rotas da interface, API e Swagger.

## 17. Credenciais padrao para demonstracao

Se nenhuma variavel de ambiente for configurada:

- admin: `admin / admin123`
- atendente: `atendente / atendente123`

Recomendacao para fala:

"Essas credenciais padrao existem apenas para ambiente de desenvolvimento. Em producao elas devem ser trocadas por variaveis seguras."

## 18. Evidencia de validacao

O projeto foi validado com:

- `./mvnw.cmd test`

Resultado:

- build com sucesso;
- 2 testes executados;
- 0 falhas.
