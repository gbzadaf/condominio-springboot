# CondoAccess API

API REST para controle de acesso de condomínios — gerenciamento de moradores, visitantes, unidades e o fluxo completo de entrada/saída na portaria.

Projeto desenvolvido com foco em boas práticas de arquitetura, Clean Code e princípios SOLID.

---

## Sobre o projeto

O CondoAccess resolve um problema real de qualquer condomínio: **controlar quem entra, quem sai, e quem autorizou**. O sistema modela isso como uma máquina de estados — todo acesso de visitante nasce `PENDING` ou `AUTHORIZED` (dependendo de pré-autorização do morador), pode ser aprovado ou negado pelo porteiro, e é finalizado (`COMPLETED`) quando o visitante registra saída.

### Funcionalidades

- **Gestão de unidades** — cadastro de blocos, números e andares
- **Gestão de moradores** — vinculados a uma unidade, com CPF e telefone únicos
- **Gestão de visitantes** — histórico completo preservado via soft delete
- **Controle de acesso** — registro de entrada/saída com pré-autorização ou aprovação manual do porteiro
- **Autenticação e autorização** — JWT com controle de acesso por perfil (`ADMIN`, `MANAGER`, `GATEKEEPER`, `RESIDENT`)
- **Rate limiting** — proteção contra brute-force nas rotas de autenticação
- **Documentação interativa** — Swagger/OpenAPI

---

## Stack utilizada

| Categoria | Tecnologia |
|---|---|
| Linguagem | Java 21 |
| Framework | Spring Boot 3 |
| Persistência | Spring Data JPA + PostgreSQL |
| Migrations | Flyway |
| Segurança | Spring Security + JWT (JJWT) |
| Documentação | SpringDoc OpenAPI (Swagger) |
| Rate Limiting | Bucket4j |
| Testes | JUnit 5 + Mockito + AssertJ |
| Build | Maven |

---

## Arquitetura

O projeto segue uma arquitetura em camadas tradicional, com separação clara de responsabilidades:

```
controller/   → expõe os endpoints REST, sem lógica de negócio
service/      → contém as regras de negócio e a máquina de estados do AccessRecord
domain/
  entity/     → entidades JPA (modelo de dados)
  enums/      → enums de domínio (UserRole, AccessStatus)
  repository/ → interfaces Spring Data JPA
dto/
  request/    → contratos de entrada da API
  response/   → contratos de saída da API
security/     → JWT (geração, validação, filtro) e rate limiting
config/       → configuração de beans (Security, Swagger)
exception/    → exceções customizadas e tratamento global de erros
```

### Decisões de design

- **UUID como identificador** — em vez de IDs sequenciais, mais adequado para PostgreSQL e evita exposição de volume de dados
- **Soft delete** — nenhum registro é removido fisicamente; entidades possuem o campo `deleted`, preservando histórico (essencial para visitantes e auditoria)
- **DTOs com `record`** — imutáveis, sem boilerplate de getters/construtores
- **Exceções de domínio customizadas** — `ResourceNotFoundException`, `DuplicateResourceException` e `BusinessException`, tratadas globalmente via `@RestControllerAdvice`
- **Autorização por papel** — `@PreAuthorize` nos endpoints sensíveis, com regras diferentes por perfil de usuário

---

## Modelo de dados

```
Unit 1 ──── N Resident
Resident N ──── 1 User (opcional, se o morador tiver login)
AccessRecord N ──── 1 Visitor
AccessRecord N ──── 1 Unit
AccessRecord N ──── 1 Resident (autorizador, opcional)
AccessRecord N ──── 1 User (porteiro)
```

### Máquina de estados do `AccessRecord`

```
PENDING ──aprovado──> AUTHORIZED ──saída registrada──> COMPLETED
   │
   └──negado──> DENIED
```

- Entrada **com** pré-autorização do morador → nasce direto como `AUTHORIZED`
- Entrada **sem** pré-autorização → nasce como `PENDING`, aguardando decisão do porteiro
- Só é possível aprovar/negar registros `PENDING`
- Só é possível registrar saída de registros `AUTHORIZED`

---

## Perfis de acesso

| Perfil | Permissões |
|---|---|
| `ADMIN` | Acesso completo, incluindo gerenciamento de usuários |
| `MANAGER` | Gerencia unidades, moradores e visitantes |
| `GATEKEEPER` | Cadastra visitantes e opera o fluxo de entrada/saída |
| `RESIDENT` | Apenas consulta (leitura) |

---

## Como executar localmente

### Pré-requisitos

- Java 21+
- Maven 3.9+
- PostgreSQL 13+

### Passos

```bash
# 1. Clone o repositório
git clone <url-do-repositorio>
cd condoaccess

# 2. Crie o banco de dados
psql -U postgres -c "CREATE DATABASE condoaccess_db;"

# 3. Configure as variáveis de ambiente
export DATABASE_USERNAME=postgres
export DATABASE_PASSWORD=postgres
export JWT_SECRET=<sua-chave-base64>

# 4. Execute a aplicação (Flyway aplica as migrations automaticamente)
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.

### Documentação interativa (Swagger)

```
http://localhost:8080/swagger-ui/index.html
```

### Executando os testes

```bash
./mvnw test
```

---

## Fluxo de uso da API

```
1. POST /api/v1/auth/register   → cria um usuário
2. POST /api/v1/auth/login      → autentica e retorna o token JWT
3. Demais endpoints              → usar o header Authorization: Bearer <token>
```

### Exemplo: registrar entrada de um visitante

```http
POST /api/v1/access-records
Authorization: Bearer <token>
Content-Type: application/json

{
  "visitorId": "uuid-do-visitante",
  "unitId": "uuid-da-unidade",
  "authorizingResidentId": "uuid-do-morador",
  "gatekeeperId": "uuid-do-porteiro",
  "notes": "Visita agendada"
}
```
---




