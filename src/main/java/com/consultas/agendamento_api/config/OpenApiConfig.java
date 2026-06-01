package com.consultas.agendamento_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI agendamentoOpenApi(Components securityComponents, SecurityRequirement securityRequirement) {
        return new OpenAPI()
                .components(securityComponents)
                .addSecurityItem(securityRequirement)
                .info(new Info()
                        .title("API de Agendamento de Consultas")
                        .description("Cadastro de pacientes, medicos e consultas.")
                        .version("v1")
                        .contact(new Contact().name("Projeto Academico")));
    }
}
