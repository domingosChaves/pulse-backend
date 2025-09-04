# Pulse Backend

API para cadastro e consulta de Fabricantes e Produtos, com BFF (Backend for Frontend), documentação OpenAPI/Swagger, testes automatizados e execução local (H2) e em contêiner (Postgres via Docker Compose).

## Sumário
- Visão geral e objetivos
- Arquitetura e módulos
- Tecnologias e versões
- Como executar (local e Docker)
- Execução em EC2 (AWS) – Front + Back com IP público
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
- BFF com FeignClient.
- Seeds diferenciados para dev (H2) e Docker (Postgres) com carga massiva no Docker.

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
  - Health: http://localhost:8081/health e http://localhost:8081/api/health
  - H2 Console: http://localhost:8081/h2-console (JDBC: `jdbc:h2:mem:testdb`, user: `sa`, senha vazia)

Configuração local: `src/main/resources/application.properties` (H2 em memória, data.sql habilitado).

### 2) Execução via Docker Compose (Postgres)
Requisitos: Docker e Docker Compose.

- Build das imagens:
```bash
docker compose build
```
- Subir serviços (app + banco) já com base populada:
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
  - Health: http://localhost:8081/health
  - Postgres (host): localhost:5432 | DB: `pulse` | user: `pulse` | pass: `pulse`

Profile Docker: `src/main/resources/application-docker.properties` (usa Postgres e executa `data-docker.sql`).

## Execução em EC2 (AWS) – Front + Back com IP público
Objetivo: executar frontend (Nginx) e backend (API) em Docker nas EC2, acessando via IP público.

- Requisitos de rede (Security Groups):
  - Frontend: abrir porta 80 para 0.0.0.0/0.
  - Backend: abrir porta 8081; em dev, 0.0.0.0/0; em prod, restringir ao SG do frontend ou ao IP público do front.

- Backend (API):
  - Porta container: 8081. Publicar: `-p 8081:8081`.
  - Variáveis mínimas:
    - `AUTH_JWT_SECRET` (obrigatória), `AUTH_JWT_EXP_SECONDS=3600`.
    - `CORS_ALLOWED_ORIGINS` (ex.: `http://<IP_PUBLICO_FRONT>`).
    - `OAUTH_ALLOWED_REDIRECTS` (ex.: `http://<IP_PUBLICO_FRONT>/auth/callback`).
    - Opcional DB: `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD` (ou use H2 padrão).
  - Exemplo (mesma instância do front; usa metadata para origin):
```bash
BACK_ORIGIN="http://$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4)"
docker run -d --name pulse-backend -p 8081:8081 \
  -e AUTH_JWT_SECRET=troque-me \
  -e AUTH_JWT_EXP_SECONDS=3600 \
  -e CORS_ALLOWED_ORIGINS="$BACK_ORIGIN" \
  -e OAUTH_ALLOWED_REDIRECTS="$BACK_ORIGIN/auth/callback" \
  <sua-imagem-backend>
```

- Frontend (Nginx):
  - Porta container: 80. Publicar: `-p 80:80`.
  - Variável para proxy:
    - `BACKEND_UPSTREAM="<IP_PUBLICO_BACKEND>:8081"`.
    - Se front e back estiverem na mesma instância: `"$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4):8081"`.
  - Exemplo:
```bash
docker run -d --name pulse-frontend -p 80:80 \
  -e BACKEND_UPSTREAM="$(curl -s http://169.254.169.254/latest/meta-data/public-ipv4):8081" \
  <sua-imagem-frontend>
```

- Alinhamento com o front:
  - Nginx do front deve proxiar `/api` → `BACKEND_UPSTREAM`.
  - Backend exige JWT nas rotas `/api/**` exceto `/api/auth/**` e health.
  - OAuth no backend deve aceitar `redirect_uri` `http://<IP_PUBLICO_FRONT>/auth/callback` (ajuste em `OAUTH_ALLOWED_REDIRECTS`).
  - CORS deve permitir `http://<IP_PUBLICO_FRONT>` (ajuste em `CORS_ALLOWED_ORIGINS`).

- Critérios de aceite:
  - `http://<IP_PUBLICO_FRONT>/` abre o app e `/login`/`/register` funcionam.
  - `/api` no front proxia para `http://<IP_PUBLICO_BACK>:8081`.
  - OAuth redireciona e finaliza no callback do IP público.
  - Rotas protegidas respondem 401 sem token e 200 com token.
  - Health do backend responde 200 em `/api/health`.

- Observações:
  - Se IP público mudar (stop/start), reexportar variáveis via metadata no próximo start do container resolve.
  - Se front e back estiverem no mesmo host e hairpin via IP público não funcionar, usar `127.0.0.1:8081` como exceção técnica.

## Banco de dados e dados iniciais
- H2 (profile padrão): executa `data.sql`.
- Postgres (profile `docker`): executa `data-docker.sql` com volume maior de dados (fabricantes e dezenas de produtos) para testes de paginação/relatórios.

## Endpoints principais
### Fabricantes (`/api/fabricantes`)
- POST `/` — cria fabricante (body: `FabricanteDTO`).
- GET `/` — lista fabricantes (retorna `FabricanteResponse { id, nome, descricao }`).
- GET `/{id}` — busca por ID.
- PUT `/{id}` — atualiza (body: `FabricanteDTO`).
- DELETE `/{id}` — exclui.

### Produtos (`/api/produtos`)
- POST `/` — cria produto (body: `ProdutoDTO`).
- GET `/` — lista produtos (filtro opcional `fabricanteId`).
- GET `/{id}` — busca por ID.
- PUT `/{id}` — atualiza (body: `ProdutoDTO`).
- DELETE `/{id}` — exclui.
- GET `/paged` — paginação e filtros (`nome`, `fabricanteId`, `page`, `size`, `sort`) com resposta `{ content, page, size, totalElements, totalPages, sort }`.
- GET `/relatorio` — mapa de produtos por nome do fabricante: `{ "ACME Indústria": [ProdutoResponse,...], ... }`.

### BFF (`/api/bff`)
- Fabricantes: `/fabricantes`, `/fabricantes/{id}` (CRUD).
- Produtos: `/produtos`, `/produtos/{id}` (CRUD), `/produtos/paged`, `/produtos/relatorio`.
- Repasse de cabeçalhos: Authorization e X-Correlation-ID.

## Modelo de erros e validações
- Erros de negócio/404/401/409: `ErrorResponse { error, timestamp, path }` (LocalDateTime serializado via JavaTimeModule).
- Validação de DTO (400): mapa de campos `{ campo: mensagem }`.
- Regras:
  - Fabricante: `nome` 1..120; `descricao` opcional até 500; CNPJ obrigatório, único e válido.
  - Produto: `nome` 1..120; `descricao` opcional até 500; `fabricanteId` requerido; unicidade de `codigoBarras`.

## Documentação
- Swagger UI: `/swagger-ui.html`.
- OpenAPI JSON: `/v3/api-docs`.
- Postman: `docs/postman/pulse-backend.postman_collection.json`.

## Testes
- Unitários (Mockito/JUnit) e Integração (MockMvc + H2).

Rodar testes:
```bash
mvn -DskipTests=false test
```

## Padrões adotados
- Arquitetura: Hexagonal (ports/adapters).
- Feign + ErrorDecoder: padroniza mensagens e repassa Authorization e X-Correlation-ID.
- Documentação: Springdoc OpenAPI.
- Commits: -m -m -m (Issue/Link). 

## Troubleshooting
- LocalDateTime em erros: já configurado via ObjectMapper do Spring.
- Compose: use `docker compose build` e `docker compose up -d`.
- Porta padrão: `8081`.
- `data-docker.sql`: idempotente; em Postgres use profile `docker`.
