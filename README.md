# Pulse Backend

API para cadastro e consulta de Fabricantes e Produtos, com BFF (Backend for Frontend), documentação OpenAPI/Swagger, testes automatizados e execução local (H2) e em contêiner (Postgres via Docker Compose).

## Sumário
- Visão geral e objetivos
- Arquitetura e módulos
- Tecnologias e versões
- Como executar (local e Docker)
- Banco de dados e dados iniciais
- Endpoints principais (REST e BFF)
- Modelo de erros e validações
- Documentação (Swagger/UI e Postman)
- Testes (unitários e integração)
- Padrões adotados (arquitetura, commits)
- Troubleshooting

---

## Visão geral e objetivos
Este projeto expõe CRUD completo de Fabricantes e Produtos, incluindo:
- Regras de negócio básicas (unicidade de CNPJ e de código de barras).
- Relatório de produtos agrupado por nome do fabricante.
- Paginação e filtros por nome/fabricante.
- BFF com FeignClient para centralizar chamadas do frontend.
- Documentação interativa via Swagger UI e coleção Postman.
- Execução com H2 (dev) e Postgres (Docker).

## Arquitetura e módulos
- Arquitetura Hexagonal (Ports & Adapters):
  - Domínio/UseCase: interfaces em `fabricante.port` e `produto.port`.
  - Serviços (aplicação): `FabricanteService` e `ProdutoService` implementam os casos de uso.
  - Adapters de saída (persistência): `FabricanteAdapter` e `ProdutoAdapter` usando Spring Data JPA.
  - Adapters de entrada (HTTP): `FabricanteController`, `ProdutoController` e `BffController`.
- BFF: endpoints sob `/api/bff` que consomem `/api/fabricantes` e `/api/produtos` via Feign.
- Tratamento de erros global: `GlobalExceptionHandler` + `FeignErrorDecoder` (BFF).

Estrutura principal (resumo):
```
src/main/java/com/domingos/pulse_backend/
  fabricante/ (entidade, controller, service, port, adapter, validação)
  produto/    (entidade, controller, service, port, adapter)
  bff/        (controller, service, clients Feign)
  config/     (Feign, OpenAPI)
  api/        (ErrorResponse)
```

## Tecnologias e versões
- Java 17, Spring Boot 3.3.x
- Spring Web, Spring Data JPA, Validation
- H2 (dev), PostgreSQL (Docker)
- Spring Cloud OpenFeign
- Springdoc OpenAPI (Swagger UI)
- JUnit 5, Mockito, MockMvc
- Docker e Docker Compose

## Como executar
### 1) Execução local (H2)
Requisitos: JDK 17 e Maven.

- Compilar e testar:
```bash
mvn -DskipTests=false clean test
```
- Subir a aplicação:
```bash
mvn spring-boot:run
# ou
mvn -DskipTests=true clean package
java -jar target/pulse-backend-0.0.1-SNAPSHOT.jar
```
- Endereços úteis (local):
  - API base: http://localhost:8081
  - Swagger UI: http://localhost:8081/swagger-ui.html
  - OpenAPI JSON: http://localhost:8081/v3/api-docs
  - H2 Console: http://localhost:8081/h2-console (JDBC: `jdbc:h2:mem:testdb`, user: `sa`, senha vazia)

Configuração local: `src/main/resources/application.properties` (H2 em memória, data.sql habilitado).

### 2) Execução via Docker Compose (Postgres)
Requisitos: Docker e Docker Compose.

- Build das imagens:
```bash
docker compose build
```
- Subir serviços (app + banco):
```bash
docker compose up -d
```
- Logs da aplicação:
```bash
docker compose logs -f app
```
- Endereços úteis (Docker):
  - API base: http://localhost:8081
  - Swagger UI: http://localhost:8081/swagger-ui.html
  - Postgres (host): localhost:5432 | DB: `pulse` | user: `pulse` | pass: `pulse`

Profile Docker: `src/main/resources/application-docker.properties` (usa Postgres e executa `data.sql`).

## Banco de dados e dados iniciais
- H2 (profile padrão) e Postgres (profile `docker`).
- Seeds em `src/main/resources/data.sql`:
  - 2 fabricantes (ACME Indústria, Beta Ltda).
  - 3 produtos vinculados aos fabricantes.

## Endpoints principais
### Fabricantes (`/api/fabricantes`)
- POST `/` — cria fabricante (body: `FabricanteDTO`).
- GET `/` — lista fabricantes.
- GET `/{id}` — busca por ID.
- PUT `/{id}` — atualiza (body: `FabricanteDTO`).
- DELETE `/{id}` — exclui.

### Produtos (`/api/produtos`)
- POST `/` — cria produto (body: `ProdutoDTO`).
- GET `/` — lista produtos (filtro opcional `fabricanteId`).
- GET `/{id}` — busca por ID.
- PUT `/{id}` — atualiza (body: `ProdutoDTO`).
- DELETE `/{id}` — exclui.
- GET `/paged` — paginação e filtros (`nome`, `fabricanteId`, `page`, `size`, `sort`).
- GET `/relatorio` — mapa de produtos por nome do fabricante: `{ "ACME Indústria": [ProdutoResponse,...], ... }`.

### BFF (`/api/bff`)
- Fabricantes: `/fabricantes`, `/fabricantes/{id}` (CRUD).
- Produtos: `/produtos`, `/produtos/{id}` (CRUD), `/produtos/paged`, `/produtos/relatorio`.
- Repasse de cabeçalhos: Authorization e X-Correlation-ID.

## Modelo de erros e validações
- Erros de negócio/404: `ErrorResponse { error, timestamp, path }`.
- Validação de DTO (400): mapa de campos `{ campo: mensagem }`.
- Regras:
  - Fabricante: CNPJ obrigatório, único e válido (validador `@CNPJ`).
  - Produto: `nome` e `codigoBarras` obrigatórios e `fabricanteId` requerido; unicidade de `codigoBarras`.

## Documentação
- Swagger UI: `/swagger-ui.html` (descrições por endpoint e códigos de resposta documentados com `@ApiResponses`).
- OpenAPI JSON: `/v3/api-docs`.
- Postman: `docs/postman/pulse-backend.postman_collection.json` (CRUD, paginação, relatório e BFF). A descrição inclui o formato padrão de erro.

## Testes
- Unitários (Mockito/JUnit): `FabricanteServiceTest`, `ProdutoServiceTest`.
- Integração (MockMvc + H2):
  - `FabricanteControllerIT` — CRUD e validações.
  - `ProdutoControllerIT` — lista, filtro por fabricante, paginação, relatório e validações.
  - `BffIntegrationTest` — delegação para clientes Feign (mockados).

Rodar testes:
```bash
mvn -DskipTests=false test
```

## Padrões adotados
- Arquitetura: Hexagonal (ports/adapters) para desacoplamento entre domínio, HTTP e persistência.
- Feign + ErrorDecoder: padroniza mensagens e mapeia 400/404; repasse de headers (Authorization, X-Correlation-ID).
- Documentação: Springdoc OpenAPI, anotações `@Tag`, `@Operation`, `@ApiResponses` e `@Schema`.
- Commits: preferencialmente com 3 mensagens (-m -m -m). Exemplo:
```
git commit -m "feat: descrição curta e clara" -m "Issue: #123" -m "Link: https://github.com/usuario/repo/issues/123"
```

## Troubleshooting
- H2 console não abre: confirme `spring.h2.console.enabled=true` e URL `jdbc:h2:mem:testdb`.
- Docker build falhou por encoding: o projeto está padronizado em UTF-8 no Maven; evite caracteres com encoding inconsistente em `.properties`.
- Compose: remova a chave `version` (já removida aqui) para evitar avisos; use `docker compose build && docker compose up -d`.
- Porta: por padrão usamos `8081`; ajuste `server.port` se necessário.

---

## Contatos e licenças
- Swagger UI expõe contato do time (OpenAPI Info).
- Licença: MIT (arquivo `LICENSE`).

