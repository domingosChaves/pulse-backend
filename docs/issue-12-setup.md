# Issue 12 — Configurar ambiente backend (Spring Boot + H2)

Objetivo
- Criar e documentar o ambiente base do backend usando Spring Boot e banco H2 em memória para desenvolvimento dos microsserviços de Fabricantes e Produtos.

O que este projeto já inclui
- Projeto Maven Spring Boot (main class: com.domingos.pulse_backend.PulseBackendApplication).
- Dependências: spring-boot-starter-web, spring-boot-starter-data-jpa, spring-boot-starter-validation, spring-cloud-starter-openfeign, h2.
- Configuração H2 em `src/main/resources/application.properties` com datasource in-memory e console habilitado.
- Script inicial `src/main/resources/data.sql` para popular dados de exemplo (fabricantes e produtos).
- Endpoints REST para Fabricantes e Produtos e BFF via Feign.
- Documentação OpenAPI (Swagger) e coleção Postman em `docs/postman`.

Como rodar localmente
1. Build:
   - Windows: `./mvnw.cmd -DskipTests package`
   - Linux/macOS: `./mvnw -DskipTests package`

2. Rodar a aplicação:
   - Windows: `./mvnw.cmd -DskipTests spring-boot:run`
   - Linux/macOS: `./mvnw -DskipTests spring-boot:run`

3. Acessos úteis (porta configurada atualmente no projeto: 8081):
   - API base: http://localhost:8081/
   - Swagger UI: http://localhost:8081/swagger-ui.html ou /swagger-ui/index.html
   - OpenAPI JSON: http://localhost:8081/v3/api-docs
   - H2 Console: http://localhost:8081/h2-console
     - JDBC URL: jdbc:h2:mem:testdb
     - User: sa
     - Password: (vazio)

Validação após start
- Verifique tabelas e dados iniciais no H2 Console.
- Teste endpoints listagem: GET /api/fabricantes e GET /api/produtos
- Teste BFF: GET /api/bff/fabricantes e /api/bff/produtos

Observações e boas práticas
- `spring.jpa.defer-datasource-initialization=true` garante que o data.sql rode após o Hibernate criar o schema.
- Para ambientes de produção, utilize um banco persistente (Postgres, MySQL) e não execute data.sql automaticamente.
- A coleção Postman está em `docs/postman/pulse-backend.postman_collection.json`.

Próximos passos sugeridos (opcionais)
- Adicionar scripts Flyway/Liquibase para gestão de migrações.
- Configurar perfis Spring (dev/prod) com configurações de datasource separadas.
- Integrar CI para rodar build e testes automaticamente.

