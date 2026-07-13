# 💳 Sistema Bancário REST API

API REST completa para gerenciamento bancário com autenticação segura, controle de contas, processamento de transações (Pix e cartão de crédito) e histórico detalhado.

---

## 📊 Badges

![Java](https://img.shields.io/badge/Java-21-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.14-green?style=for-the-badge&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Latest-blue?style=for-the-badge&logo=postgresql)
![JWT](https://img.shields.io/badge/JWT-Auth-purple?style=for-the-badge&logo=auth0)
![Docker](https://img.shields.io/badge/Docker-Containerized-blue?style=for-the-badge&logo=docker)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-85EA2D?style=for-the-badge&logo=swagger)

---

## 📋 Visão Geral

API REST robusta para gerenciamento bancário, implementando as melhores práticas de segurança e arquitetura de software. O sistema oferece funcionalidades completas desde autenticação segura com JWT até processamento complexo de transações financeiras com múltiplos métodos de pagamento.

### ✨ Destaques Principais

- 🔐 **Autenticação JWT** - Segurança em camadas com tokens JWT assinados
- 💰 **Gerenciamento de Contas** - Criação, consulta e controle de saldo e limite de crédito
- 💳 **Múltiplos Métodos de Pagamento** - Suporte a Pix, cartão de crédito e transferências
- 📊 **Histórico Transacional** - Rastreamento completo de todas as operações
- 🏦 **Controle de Limite** - Gestão inteligente de crédito disponível
- 🧮 **Validações Rigorosas** - Validação de dados em todos os endpoints
- 📚 **Documentação Swagger** - Documentação interativa e testes de API
- 🐳 **Containerização Completa** - Docker Compose para ambiente produção-pronta
- 🔄 **Transações Atômicas** - Operações de banco de dados garantidas com @Transactional
- ⚠️ **Tratamento de Exceções** - Respostas estruturadas para todos os cenários de erro

---

## 🚀 Tecnologias

### Backend
- **Linguagem**: Java 21
- **Framework**: Spring Boot 3.5.14
  - Spring Web (REST APIs)
  - Spring Data JPA (Persistência)
  - Spring Security (Autenticação/Autorização)
  - Spring Validation (Validação de dados)
  - Spring HATEOAS (REST avançado)

### Autenticação & Segurança
- **JWT (Java-JWT)**: v4.4.0 - Tokens seguros e assinados
- **Spring Security**: Integrado com JWT
- **Hash de Senhas**: BCrypt via Spring Security

### Banco de Dados & Migrações
- **PostgreSQL**: Banco de dados relacional
- **H2**: Banco em memória para testes
- **Flyway**: v12.6.0 - Versionamento e migração automática de schema

### Documentação & Testes
- **SpringDoc OpenAPI**: v2.8.17 - Swagger UI e OpenAPI
- **Spring Boot Test**: Testes integrados
- **MockMvc**: Testes de endpoints

### Containerização
- **Docker**: Imagem multi-stage para build otimizado
- **Docker Compose**: Orquestração local

---

## 🔧 Configuração

### Pré-requisitos

```
Java 21+
Maven 3.8+
Docker 24+
Docker Compose 2.20+
PostgreSQL 14+ (ou use Docker)
Git
```

### Variáveis de Ambiente

```bash
# Configurações Spring
SPRING_PROFILES_ACTIVE=dev

# Banco de Dados PostgreSQL
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/banco_sistema
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=seu_password_aqui
SPRING_JPA_HIBERNATE_DDL_AUTO=validate

# JWT
JWT_SECRET=sua_chave_secreta_super_segura_com_minimo_32_caracteres
JWT_EXPIRATION=86400000

# Servidor
SERVER_PORT=8080
SERVER_SERVLET_CONTEXT_PATH=/api/v1
```

### Instalação Local

#### 1. Clone o repositório

```bash
git clone https://github.com/RaphaelMun1z/Spring-Sistema-Bancario.git
cd Spring-Sistema-Bancario
```

#### 2. Configure variáveis de ambiente

```bash
# Crie arquivo .env na raiz do projeto
cp .env.example .env

# Edite com suas configurações
nano .env
```

#### 3. Compile o projeto

```bash
# Compile com Maven
mvn clean install -DskipTests

# Ou apenas compile sem testes
mvn clean package -DskipTests
```

#### 4. Execute a aplicação

```bash
# Com Maven
mvn spring-boot:run

# Ou execute o JAR gerado
java -jar target/sistema-bancario-0.0.1-SNAPSHOT.jar
```

#### 5. Acesse a documentação

```
Swagger UI: http://localhost:8080/api/v1/swagger-ui.html
OpenAPI JSON: http://localhost:8080/api/v1/v3/api-docs
Actuator: http://localhost:8080/api/v1/actuator
```

### Docker Compose

#### Arquivo de Configuração

```yaml
version: '3.8'

services:
  postgresql:
    image: postgres:16-alpine
    container_name: banco-postgres
    environment:
      POSTGRES_DB: banco_sistema
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: postgres_password
      TZ: America/Sao_Paulo
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: [ "CMD-SHELL", "pg_isready -U postgres" ]
      interval: 10s
      timeout: 5s
      retries: 5
    networks:
      - banco-network

  api:
    build:
      context: .
      dockerfile: Dockerfile
    container_name: banco-api
    ports:
      - "8080:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgresql:5432/banco_sistema
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres_password
      SPRING_JPA_HIBERNATE_DDL_AUTO: validate
      SPRING_FLYWAY_ENABLED: true
      JWT_SECRET: sua_chave_secreta_super_segura_com_minimo_32_caracteres
      JWT_EXPIRATION: "86400000"
      SERVER_PORT: 8080
      SPRING_PROFILES_ACTIVE: prod
    depends_on:
      postgresql:
        condition: service_healthy
    networks:
      - banco-network
    restart: unless-stopped

volumes:
  postgres_data:
    driver: local

networks:
  banco-network:
    driver: bridge
```

#### Executar com Docker Compose

```bash
# Inicie os serviços
docker-compose up -d

# Verifique o status
docker-compose ps

# Veja os logs da API
docker-compose logs -f api

# Acesse a API
curl http://localhost:8080/api/v1/test/ping

# Interrompa os serviços
docker-compose down

# Limpe tudo (incluindo volumes)
docker-compose down -v
```

---

## 📚 API Endpoints

### Autenticação

| Método | Endpoint | Descrição | Autenticação |
|---|---|---|---|
| POST | `/auth/register` | Registrar novo usuário | ❌ |
| POST | `/auth/login` | Autenticar e obter token JWT | ❌ |
| POST | `/auth/refresh` | Renovar token JWT | ✅ Token |
| GET | `/auth/validate` | Validar token | ✅ Token |

### Contas Bancárias

| Método | Endpoint | Descrição | Autenticação |
|---|---|---|---|
| POST | `/accounts` | Criar nova conta | ✅ JWT |
| GET | `/accounts/{id}` | Obter detalhes da conta | ✅ JWT |
| GET | `/accounts/{id}/balance` | Consultar saldo | ✅ JWT |
| GET | `/accounts/{id}/details` | Obter detalhes completos | ✅ JWT |
| PUT | `/accounts/{id}` | Atualizar conta | ✅ JWT |
| GET | `/accounts` | Listar contas | ✅ JWT |

### Transações

| Método | Endpoint | Descrição | Autenticação |
|---|---|---|---|
| POST | `/transactions/deposit` | Realizar depósito | ✅ JWT |
| POST | `/transactions/withdraw` | Realizar saque | ✅ JWT |
| POST | `/transactions/transfer` | Transferência entre contas | ✅ JWT |
| POST | `/transactions/pix` | Pagamento via Pix | ✅ JWT |
| POST | `/transactions/credit-card` | Pagamento via cartão | ✅ JWT |
| GET | `/transactions/{accountId}` | Histórico de transações | ✅ JWT |
| GET | `/transactions/{id}` | Detalhes da transação | ✅ JWT |

### Clientes

| Método | Endpoint | Descrição | Autenticação |
|---|---|---|---|
| POST | `/customers` | Registrar novo cliente | ❌ |
| GET | `/customers/{cpf}` | Obter dados do cliente | ✅ JWT |
| GET | `/customers/{cpf}/accounts` | Listar contas do cliente | ✅ JWT |
| PUT | `/customers/{cpf}` | Atualizar dados | ✅ JWT |

### Utilitários

| Método | Endpoint | Descrição | Autenticação |
|---|---|---|---|
| GET | `/test/ping` | Verificar saúde da API | ❌ |
| GET | `/actuator/health` | Health check completo | ❌ |

---

## 📖 Exemplo de Uso

### 1. Registrar um Cliente

```bash
curl -X POST http://localhost:8080/api/v1/customers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Raphael Muniz",
    "phone": "(13) 99999-9999",
    "email": "raphael@example.com",
    "password": "senha_segura_123",
    "cpf": "123.123.123-12",
    "birthDate": "2004-10-22"
  }'

# Resposta esperada:
# {
#   "id": "uuid-123",
#   "cpf": "123.123.123-12",
#   "name": "Raphael Muniz",
#   "email": "raphael@example.com"
# }
```

### 2. Criar uma Conta Bancária

```bash
curl -X POST http://localhost:8080/api/v1/accounts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer seu_token_jwt" \
  -d '{
    "customer_cpf": "123.123.123-12"
  }'

# Resposta esperada:
# {
#   "id": "account-uuid-456",
#   "balance": 0.00,
#   "availableLimit": 1400.00,
#   "customerCpf": "123.123.123-12"
# }
```

### 3. Depositar Dinheiro

```bash
curl -X POST http://localhost:8080/api/v1/transactions/deposit \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer seu_token_jwt" \
  -d '{
    "amount": 5000.00,
    "accountId": "account-uuid-456"
  }'

# Resposta esperada:
# {
#   "id": "transaction-uuid",
#   "type": "DEPOSIT",
#   "amount": 5000.00,
#   "moment": "2026-07-13T10:30:45.123456Z",
#   "receiverAccountId": "account-uuid-456"
# }
```

### 4. Transferir via Pix

```bash
curl -X POST http://localhost:8080/api/v1/transactions/pix \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer seu_token_jwt" \
  -d '{
    "type": "PIX",
    "amount": 1500.00,
    "senderAccountId": "account-uuid-456",
    "receiverAccountId": "account-uuid-789"
  }'

# Resposta esperada:
# {
#   "id": "transaction-uuid",
#   "type": "PIX",
#   "amount": 1500.00,
#   "moment": "2026-07-13T11:15:30.654321Z",
#   "senderAccountId": "account-uuid-456",
#   "receiverAccountId": "account-uuid-789"
# }
```

### 5. Consultar Saldo

```bash
curl -X GET http://localhost:8080/api/v1/accounts/account-uuid-456/balance \
  -H "Authorization: Bearer seu_token_jwt"

# Resposta esperada:
# 3500.00
```

---

## 🏗️ Arquitetura

### Estrutura de Pastas

```
Spring-Sistema-Bancario/
│
├── src/
│   ├── main/
│   │   ├── java/sistema_bancario/
│   │   │   ├── controllers/              # REST Controllers
│   │   │   │   ├── AccountController.java
│   │   │   │   ├── AuthController.java
│   │   │   │   ├── CustomerController.java
│   │   │   │   ├── TransactionController.java
│   │   │   │   └── TestController.java
│   │   │   │
│   │   │   ├── services/                 # Lógica de Negócio
│   │   │   │   ├── AccountService.java
│   │   │   │   ├── CustomerService.java
│   │   │   │   ├── AuthService.java
│   │   │   │   ├── payment/
│   │   │   │   │   ├── TransactionService.java
│   │   │   │   │   ├── TransactionValidations.java
│   │   │   │   │   ├── PaymentMethod.java (interface)
│   │   │   │   │   ├── PixPaymentMethod.java
│   │   │   │   │   └── CreditCardPaymentMethod.java
│   │   │   │   └── TestService.java
│   │   │   │
│   │   │   ├── repositories/             # Data Access (JPA)
│   │   │   │   ├── AccountRepository.java
│   │   │   │   ├── CustomerRepository.java
│   │   │   │   └── TransactionRepository.java
│   │   │   │
│   │   │   ├── entities/                 # Modelos JPA
│   │   │   │   ├── Account.java
│   │   │   │   ├── Transaction.java
│   │   │   │   ├── enums/
│   │   │   │   │   └── TransactionTypeEnum.java
│   │   │   │   └── users/
│   │   │   │       └── Customer.java
│   │   │   │
│   │   │   ├── dtos/                     # Data Transfer Objects
│   │   │   │   ├── req/
│   │   │   │   │   ├── AccountReqDTO.java
│   │   │   │   │   ├── CustomerReqDTO.java
│   │   │   │   │   ├── AuthLoginReqDTO.java
│   │   │   │   │   └── TransactionReqDTO.java
│   │   │   │   └── res/
│   │   │   │       ├── AccountDetailsResDTO.java
│   │   │   │       ├── CustomerDetailsResDTO.java
│   │   │   │       ├── TransactionResDTO.java
│   │   │   │       └── AuthTokenResDTO.java
│   │   │   │
│   │   │   ├── config/                   # Configurações
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── OpenApiConfig.java
│   │   │   │   └── TestConfig.java (dados iniciais)
│   │   │   │
│   │   │   ├── security/                 # Segurança
│   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   └── SecurityContextHolder.java
│   │   │   │
│   │   │   ├── exceptions/               # Tratamento de Erros
│   │   │   │   ├── ResourceExceptionHandler.java
│   │   │   │   ├── ResourceNotFoundException.java
│   │   │   │   ├── InvalidCredentialsException.java
│   │   │   │   └── StandardError.java
│   │   │   │
│   │   │   └── SistemaBancarioApplication.java
│   │   │
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       ├── application-prod.yml
│   │       ├── application-test.yml
│   │       └── db/migration/             # Flyway Migrations
│   │           ├── V1__Create_Tables.sql
│   │           ├── V2__Insert_Initial_Data.sql
│   │           └── V3__Add_Indexes.sql
│   │
│   └── test/
│       ├── java/sistema_bancario/
│       │   ├── services/
│       │   ├── controllers/
│       │   └── integration/
│       └── resources/
│           └── application-test.yml
│
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── .gitignore
├── README.md
└── logs/                                # Diretório de logs
```

### Fluxo de Comunicação

```
┌──────────────────────────┐
│    Cliente/Frontend      │
│  (Mobile, Web, etc)      │
└────────────┬─────────────┘
             │ HTTP Request
             │ (com JWT token)
             ▼
┌──────────────────────────────────────────┐
│       Spring Boot REST API                │
│         (Porta 8080)                     │
│                                           │
│  1. JwtAuthenticationFilter               │
│  2. SecurityConfig (validação)            │
│  3. Controller                            │
│  4. Service (lógica de negócio)           │
│  5. Repository (JPA)                      │
└────────┬─────────────────────────────────┘
         │
         ▼
┌──────────────────────────┐
│   PostgreSQL Database    │
│   (Tabelas de negócio)   │
│                          │
│  - tb_customers          │
│  - tb_accounts           │
│  - tb_transactions       │
│  - tb_transaction_types  │
└──────────────────────────┘
```

### Fluxo de Autenticação

```
┌─────────────────┐
│  Login Request  │
│ (email/senha)   │
└────────┬────────┘
         │
         ▼
┌──────────────────────────┐
│   AuthController         │
│   AuthService.login()    │
└────────┬─────────────────┘
         │
         ▼
┌──────────────────────────┐
│ Validar Credenciais      │
│ Spring Security BCrypt   │
└────────┬─────────────────┘
         │
         ▼
┌──────────────────────────┐
│ JwtTokenProvider         │
│ Gerar JWT assinado       │
└────────┬─────────────────┘
         │
         ▼
┌─────────────────────────────────────┐
│ Resposta com Token JWT              │
│ {                                   │
│   "token": "eyJ0eXAi...",           │
│   "expiresIn": 86400000             │
│ }                                   │
└─────────────────────────────────────┘
```

---

## 🔐 Segurança

### Recursos de Segurança Implementados

- 🔒 **JWT (Java Web Tokens)** - Autenticação stateless com tokens assinados
- 🔑 **Spring Security** - Framework de segurança integrado
- 🛡️ **BCrypt** - Hash de senhas seguro com salt aleatório
- 🚫 **CORS Configuration** - Controle de acesso entre origens
- 📝 **Validação de Input** - Bean Validation em todos os endpoints
- 🔍 **SQL Injection Prevention** - Prepared Statements via JPA
- ⏱️ **Token Expiration** - Expiração automática de tokens (24 horas)
- 🔐 **Password Encryption** - Senhas nunca armazenadas em texto plano
- 📊 **Audit Logging** - Logs estruturados de operações sensíveis
- 🚨 **Exception Handling** - Respostas seguras sem expor detalhes internos
- 🌐 **HTTPS Ready** - Pronto para TLS/SSL em produção
- 🧯 **Rate Limiting Ready** - Estrutura pronta para implementar

### Boas Práticas de Segurança

```bash
# Nunca commit credenciais
echo ".env" >> .gitignore
echo "secrets.properties" >> .gitignore

# Use variáveis de ambiente para dados sensíveis
export JWT_SECRET=sua_chave_super_segura_com_minimo_32_caracteres
export SPRING_DATASOURCE_PASSWORD=database_password

# Mantenha dependências atualizadas
mvn versions:display-dependency-updates
mvn dependency-check:check

# Validar tokens antes de acessar dados sensíveis
# @PreAuthorize("hasRole('CUSTOMER')")
```

### Cenários de Segurança Cobertos

- ✅ Autenticação de usuários
- ✅ Autorização baseada em roles
- ✅ Isolamento de dados por cliente
- ✅ Validação de limites de transação
- ✅ Detecção de padrões fraudulentos (estrutura)
- ✅ Logs de auditoria
- ✅ Proteção contra CSRF
- ✅ Validação de CORS

---

## 🧪 Testes

### Executar Testes Unitários

```bash
# Testes unitários
mvn test

# Testes com cobertura de código
mvn test jacoco:report

# Visualizar relatório
open target/site/jacoco/index.html
```

### Executar Testes de Integração

```bash
# Com padrão de nome específico
mvn test -Dtest=*IntegrationTest

# Teste integrado com banco H2
mvn test -P integration
```

### Testes de Endpoints com cURL

```bash
# Health check
curl http://localhost:8080/api/v1/actuator/health

# Teste simples
curl http://localhost:8080/api/v1/test/ping

# Com logs detalhados
curl -v http://localhost:8080/api/v1/test/ping
```

### Teste com Postman/Insomnia

```
1. Importe a coleção: docs/postman_collection.json
2. Configure variáveis de ambiente:
   - {{host}}: http://localhost:8080
   - {{token}}: [seu_token_jwt]
3. Execute a sequência de requisições
```

### Verificar Qualidade de Código

```bash
# SonarQube (se disponível)
mvn clean verify sonar:sonar

# SpotBugs (detectar bugs potenciais)
mvn spotbugs:check

# Checkstyle (estilo de código)
mvn checkstyle:check
```

---

## 🐛 Troubleshooting

### Problema: Erro de conexão com PostgreSQL

**Solução:**
```bash
# Verifique se PostgreSQL está rodando
docker-compose ps

# Teste conexão direta
psql -h localhost -U postgres -d banco_sistema

# Se usando Docker, recrie o container
docker-compose down postgres
docker-compose up -d postgres

# Aguarde healthcheck passar
docker-compose logs postgres | grep "database system is ready"
```

### Problema: JWT Token Inválido ou Expirado

**Solução:**
```bash
# Gere um novo token via login
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "usuario@example.com",
    "password": "senha"
  }'

# Use o novo token nas requisições
curl -H "Authorization: Bearer <novo_token>" http://localhost:8080/api/v1/accounts

# Verifique expiração em jwt.io
```

### Problema: Port 8080 já está em uso

**Solução:**
```bash
# Linux/Mac - Encontre o processo
lsof -i :8080

# Mate o processo
kill -9 <PID>

# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Ou use porta diferente
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### Problema: Flyway migration falhou

**Solução:**
```bash
# Verifique logs da migração
docker-compose logs api | grep -i flyway

# Limpe o schema e recrie (cuidado!)
docker-compose down -v
docker-compose up -d

# Ou repare manualmente
docker exec banco-postgres psql -U postgres -d banco_sistema -c "DROP SCHEMA public CASCADE;"
```

### Problema: Out of Memory ao usar Docker

**Solução:**
```bash
# Aumente limite em docker-compose.yml
services:
  api:
    mem_limit: 1g
    mem_reservation: 512m

# Ou manualmente
docker update --memory 1g container_id

# Reinicie o container
docker-compose restart api
```

### Problema: Erro ao fazer build do Docker

**Solução:**
```bash
# Limpe cache
docker system prune -a

# Rebuild com output detalhado
docker-compose build --no-cache api

# Verifique arquivo Dockerfile
cat Dockerfile

# Build manual para debug
docker build -t banco-api:latest .
```

### Problema: Transação falhou - Saldo insuficiente

**Solução:**
```bash
# Verifique saldo atual
curl -H "Authorization: Bearer $TOKEN" \
  http://localhost:8080/api/v1/accounts/$ACCOUNT_ID/balance

# Deposite dinheiro
curl -X POST http://localhost:8080/api/v1/transactions/deposit \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"amount": 1000.00, "accountId": "'$ACCOUNT_ID'"}'

# Tente novamente
```

---

## 📝 Contribuição

### Guia de Contribuição

Agradecemos contribuições! Para contribuir com este projeto:

#### 1. Faça um Fork

```bash
# Via GitHub
# Clique em "Fork" no repositório

# Ou via GitHub CLI
gh repo fork RaphaelMun1z/Spring-Sistema-Bancario
```

#### 2. Clone seu Fork

```bash
git clone https://github.com/SEU_USUARIO/Spring-Sistema-Bancario.git
cd Spring-Sistema-Bancario
```

#### 3. Crie uma Branch

```bash
# Feature nova
git checkout -b feature/sua-feature

# Bug fix
git checkout -b fix/seu-bugfix

# Melhoria de documentação
git checkout -b docs/sua-doc

# Refatoring
git checkout -b refactor/seu-refactor
```

#### 4. Faça suas Alterações

```bash
# Siga o padrão de código do projeto
# Adicione testes para novas funcionalidades
# Atualize documentação se necessário
```

#### 5. Commit com Mensagens Descritivas

```bash
# Padrão Conventional Commits
git commit -m "feat: adicionar novo método de pagamento"
git commit -m "fix: corrigir bug em cálculo de saldo"
git commit -m "docs: atualizar guia de instalação"
git commit -m "refactor: reorganizar estrutura de pastas"
git commit -m "test: adicionar testes para AccountService"
```

#### 6. Push para seu Fork

```bash
git push origin feature/sua-feature
```

#### 7. Abra um Pull Request

```bash
# Vá para https://github.com/RaphaelMun1z/Spring-Sistema-Bancario
# Clique em "New Pull Request"
# Selecione sua branch
# Descreva as mudanças detalhadamente
# Clique em "Create Pull Request"
```

### Padrões de Código

- **Linguagem**: Java 21 com padrões modernas
- **Estilo**: Google Java Style Guide
- **Nomes**: CamelCase para classes/métodos, snake_case para banco de dados
- **Comments**: Explicar o "porquê", não o "como"
- **Testes**: Cobertura mínima de 80%
- **DTOs**: Usar records do Java 21 quando possível

### Verificação Antes de Submeter

```bash
# Rode testes localmente
mvn clean test

# Build completo
mvn clean install

# Verifique estilo de código
mvn checkstyle:check

# Rode a aplicação e teste manualmente
docker-compose up -d
```

---

## 📞 Suporte e Comunidade

### Canais de Comunicação

- 📧 **Issues**: [GitHub Issues](https://github.com/RaphaelMun1z/Spring-Sistema-Bancario/issues)
- 💬 **Discussões**: [GitHub Discussions](https://github.com/RaphaelMun1z/Spring-Sistema-Bancario/discussions)
- 📚 **Wiki**: [GitHub Wiki](https://github.com/RaphaelMun1z/Spring-Sistema-Bancario/wiki)

### Recursos Úteis

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Guide](https://spring.io/guides/topicals/spring-security-architecture)
- [JWT Introduction](https://jwt.io/introduction)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Flyway Migrations](https://flywaydb.org/)
- [REST API Best Practices](https://restfulapi.net/)

### Reporte um Bug

```markdown
**Descrição do Bug**
Descreva o problema de forma concisa.

**Passos para Reproduzir**
1. Execute... 
2. Vá para...
3. Veja erro...

**Comportamento Esperado**
O que deveria acontecer.

**Comportamento Atual**
O que realmente aconteceu.

**Ambiente**
- OS: Windows/Mac/Linux
- Java: 21
- Spring Boot: 3.5.14
- PostgreSQL: 16

**Logs**
```
Cole os logs relevantes aqui
```

**Evidências**
Screenshots ou vídeo se aplicável
```

---

## 👨‍💻 Autor

**Raphael Muniz**

- GitHub: [@RaphaelMun1z](https://github.com/RaphaelMun1z)
- Email: raphael.muniz@example.com
- LinkedIn: [Raphael Muniz](https://linkedin.com/in/raphaelmuniz)

---

**Última atualização**: 2026-05-17 | **Status do Projeto**: ✅ Ativo e em desenvolvimento

