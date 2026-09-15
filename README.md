# Associação Gaia API

Backend do sistema da **Associação Gaia**, uma associação de artesãos. Construído com **Spring Boot**, **Java** e **MySQL** (via Spring Data JPA), expõe uma API REST para cadastro de usuários, autenticação via **JWT** e controle de acesso baseado no tipo de usuário.

> Este README descreve o projeto **como ele está implementado hoje**, com base na leitura do código-fonte. Trechos marcados como "ainda não implementado" são funcionalidades previstas mas que ainda não têm código correspondente.

---

## Sumário

- [Visão geral](#visão-geral)
- [Modelo de domínio](#modelo-de-domínio)
- [Autenticação e controle de acesso](#autenticação-e-controle-de-acesso)
- [Endpoints da API](#endpoints-da-api)
- [Tratamento de erros](#tratamento-de-erros)
- [Estrutura de pacotes](#estrutura-de-pacotes)
- [Como rodar o projeto](#como-rodar-o-projeto)
- [Documentação das classes (Javadoc)](#documentação-das-classes-javadoc)
- [Roadmap / próximos passos](#roadmap--próximos-passos)

---

## Visão geral

Todo usuário que se cadastra na plataforma nasce com o tipo **`VISITANTE`**. A partir daí, existem (ou existirão) três papéis no sistema:

| Tipo | Descrição |
|---|---|
| `VISITANTE` | Tipo padrão de todo novo cadastro. Acesso básico. |
| `ARTESAO` | Usuário promovido a artesão. **A promoção depende de uma solicitação aprovada por um `ADMINISTRADOR` — esse fluxo ainda não está implementado no código.** |
| `ADMINISTRADOR` | Acesso irrestrito, incluindo a listagem completa de usuários. |

A autenticação é feita via **token JWT**: o usuário faz login com e-mail/senha, recebe um token, e passa a enviá-lo no cabeçalho `Authorization: Bearer <token>` nas requisições seguintes. A API não mantém sessão no servidor (é *stateless*).

## Modelo de domínio

### `Usuario`
Entidade central do sistema (tabela `usuario`). Guarda nome, e-mail (único), senha (sempre armazenada com hash, nunca em texto puro), telefone, tipo de usuário e data de cadastro. Um usuário pode opcionalmente ter um perfil de `Artesao` vinculado.

### `Artesao` e `Trabalho`
Já existem como entidades JPA (tabelas `artesao` e `trabalho`) e representam, respectivamente, o **perfil público de um artesão** (biografia, redes sociais, cidade etc.) e os **trabalhos/peças** publicados por ele.

> ⚠️ **Importante:** no código atual não existe nenhum `Service` ou `Controller` que crie, edite ou exponha essas entidades via API. Elas são a modelagem de banco já pronta para quando o fluxo de "virar artesão" e "publicar trabalhos" for implementado — mas hoje são inacessíveis pela API REST.

## Autenticação e controle de acesso

O controle de acesso é aplicado em duas camadas:

1. **Nível de rota** (`SecurityConfig`): define regras fixas por método HTTP + caminho.
   - `POST /api/usuarios` (cadastro) → público
   - `POST /api/usuarios/login` (login) → público
   - `GET /api/usuarios` (listagem completa) → somente `ADMINISTRADOR`
   - qualquer outra rota → exige autenticação (token JWT válido)

2. **Nível de método** (`@PreAuthorize` nos controllers): usado quando a regra depende do usuário autenticado.
   - `GET /api/usuarios/{id}` e `GET /api/usuarios/email/{email}` → liberado para o **próprio usuário** (dono do id/e-mail) ou para um `ADMINISTRADOR`.

O fluxo técnico por trás disso:
- `TokenService` gera e valida o JWT (HMAC, com tempo de expiração configurável).
- `SecurityFilter` intercepta toda requisição, extrai o token do cabeçalho `Authorization`, valida e — se válido — autentica o usuário no contexto do Spring Security.
- `UsuarioDetails` adapta a entidade `Usuario` para o contrato `UserDetails` do Spring Security, convertendo o `TipoUsuario` em uma authority no formato `ROLE_<TIPO>` (ex.: `ROLE_ADMINISTRADOR`).

## Endpoints da API

Prefixo comum: `/api/usuarios`

| Método | Caminho | Acesso | Descrição |
|---|---|---|---|
| `POST` | `/api/usuarios` | Público | Cadastra um novo usuário (sempre como `VISITANTE`). |
| `POST` | `/api/usuarios/login` | Público | Autentica por e-mail/senha e retorna um token JWT. |
| `GET` | `/api/usuarios` | `ADMINISTRADOR` | Lista todos os usuários cadastrados. |
| `GET` | `/api/usuarios/{id}` | Próprio usuário ou `ADMINISTRADOR` | Busca um usuário pelo id. |
| `GET` | `/api/usuarios/email/{email}` | Próprio usuário ou `ADMINISTRADOR` | Busca um usuário pelo e-mail. |

Nenhum endpoint retorna a senha do usuário — as respostas usam `UsuarioResponseDTO`, que deliberadamente omite esse campo.

## Tratamento de erros

Erros de negócio são centralizados em `GlobalExceptionHandler`:

| Exceção | Situação | Status HTTP |
|---|---|---|
| `EmailJaCadastradoException` | Cadastro com e-mail já existente | `409 Conflict` |
| `CredenciaisInvalidasException` | Login com e-mail inexistente ou senha incorreta | `401 Unauthorized` |

## Estrutura de pacotes

```
br.edu.ifsp.associacaogaia
├── config/         → configuração de segurança (SecurityConfig)
├── controller/      → controllers REST (UsuarioController)
├── dto/             → objetos de transporte de dados (request/response)
├── exception/       → exceções de negócio + handler global
├── model/           → entidades JPA (Usuario, Artesao, Trabalho, TipoUsuario)
├── repository/      → repositórios Spring Data JPA
├── security/        → filtro JWT e adaptador de autenticação
└── service/         → regras de negócio (UsuarioService, TokenService)
```

## Como rodar o projeto

Pré-requisitos: JDK 21, MySQL, Maven (ou o wrapper `./mvnw` incluso no projeto).

```bash
./mvnw spring-boot:run
```

Configure as variáveis de conexão com o banco e as propriedades de segurança (`api.security.token.secret`, `api.security.token.expiracao-minutos`) no `application.properties`/`application.yml` (não incluídas neste README por não fazerem parte do código-fonte lido).

## Documentação das classes (Javadoc)

Todas as classes do projeto foram documentadas com comentários **Javadoc** (propósito da classe, atributos relevantes, parâmetros, retornos e exceções de cada método público). Além dos comentários no próprio código-fonte, este pacote inclui um **site HTML navegável** (pasta `javadoc-site/`, se você recebeu o zip completo) gerado a partir desses comentários, para consulta sem precisar abrir a IDE.

## Roadmap / próximos passos

Com base no que já existe no banco de dados (entidades `Artesao` e `Trabalho`) mas não na API, os próximos passos naturais do projeto parecem ser:

- [ ] Fluxo de solicitação de promoção `VISITANTE` → `ARTESAO`, com aprovação por um `ADMINISTRADOR`.
- [ ] `ArtesaoController`/`ArtesaoService` para gerenciar o perfil público do artesão.
- [ ] `TrabalhoController`/`TrabalhoService` para publicação de trabalhos.
- [ ] Endpoint de atualização de dados do próprio usuário (hoje não há `setSenha()` nem endpoint de edição/troca de senha).

*(Este roadmap é uma inferência a partir da estrutura de dados já existente, não uma confirmação de que está planejado — vale validar com o time.)*
