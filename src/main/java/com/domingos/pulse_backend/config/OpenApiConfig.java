package com.domingos.pulse_backend.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configura as informações básicas do OpenAPI/Swagger da aplicação,
 * incluindo título, versão, contato e link do repositório.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Pulse Backend API")
                        .version("v1")
                        .description("API de exemplo para gerenciar Fabricantes e Produtos")
                        .contact(new Contact().name("Equipe de Desenvolvimento").email("dev@pulse.local"))
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT"))
                )
                .externalDocs(new ExternalDocumentation()
                        .description("Repositorio do projeto")
                        .url("https://github.com/domingosChaves/pulse-backend"));
    }
}
