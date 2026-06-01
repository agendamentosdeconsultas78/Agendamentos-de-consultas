# Guia de Apresentacao da Equipe

## Roteiro curto de abertura

"Nosso projeto e um sistema de agendamento de consultas desenvolvido em Java com Spring Boot. A aplicacao possui interface web, API REST, persistencia com PostgreSQL, validacoes de negocio e uma camada de seguranca com autenticacao, criptografia de senha com BCrypt e controle de acesso por papeis."

## Ordem sugerida

1. Integrante 1 abre com contexto, objetivo e arquitetura.
2. Integrante 2 explica modelagem, banco e JPA.
3. Integrante 3 detalha service, validacoes e regras.
4. Integrante 4 fecha com API, DTOs, Swagger e seguranca.

## Integrante 1

### O que falar

- qual problema o sistema resolve;
- por que foi escolhido Spring Boot;
- como as camadas foram separadas;
- fluxo completo da requisicao.

### Resumo decoravel

"A arquitetura foi separada em config, model, repository, service, api e web. Isso ajuda a manter o codigo organizado, reaproveitavel e mais facil de testar."

### Perguntas provaveis

- O que faz o Spring Boot?
- Por que dividir em camadas?
- Qual a diferenca entre MVC web e API REST?

## Integrante 2

### O que falar

- entidades `Paciente`, `Medico`, `Consulta`;
- enum `StatusConsulta`;
- `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column`;
- relacionamento `@ManyToOne`;
- PostgreSQL no ambiente principal e H2 nos testes.

### Resumo decoravel

"A modelagem do dominio foi a base do projeto. A partir das entidades nos definimos a estrutura do banco, os relacionamentos e as regras de persistencia."

### Perguntas provaveis

- O que o `@ManyToOne` significa?
- Por que `FetchType.LAZY`?
- Por que usar enum para status?

## Integrante 3

### O que falar

- papel do `AgendamentoService`;
- validacoes com `@Valid`, `@Future`, `@Past`, `@NotBlank`;
- regra que impede dois horarios iguais para o mesmo medico;
- excecao de negocio e handler global.

### Resumo decoravel

"Toda regra importante foi colocada no service, para que controller nao vire regra de negocio e repository nao vire camada de decisao."

### Perguntas provaveis

- Onde as regras de negocio ficam?
- O que o `@Transactional` faz?
- Como o sistema evita consultas duplicadas no mesmo horario?

## Integrante 4

### O que falar

- diferenca entre entidade, form e DTO;
- controllers REST;
- Swagger;
- `SecurityConfig`;
- `BCryptPasswordEncoder`;
- RBAC com `ADMIN` e `ATENDENTE`.

### Resumo decoravel

"A API nao retorna diretamente as entidades; ela usa DTOs para expor apenas o necessario. Na seguranca, usamos autenticacao e controle por papel para aplicar menor privilegio."

### Perguntas provaveis

- O que e DTO?
- Por que usar BCrypt?
- O que significa RBAC?
- Quem pode acessar cada rota?

## Possivel encerramento

"O resultado foi um sistema funcional, com separacao em camadas, regras de negocio consistentes, interface web, API documentada e preocupacao real com seguranca no acesso."
