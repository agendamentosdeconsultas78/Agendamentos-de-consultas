package com.consultas.agendamento_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI agendamentoOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Agendamento de Consultas")
                        .description("Cadastro de pacientes, medicos e consultas.")
                        .version("v1")
                        .contact(new Contact().name("Projeto Academico")));
    }
}
