package com.consultas.agendamento_api.api;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void atendentePodeListarPacientesNaApi() throws Exception {
        mockMvc.perform(get("/api/pacientes")
                        .with(httpBasic("atendente", "atendente123")))
                .andExpect(status().isOk());
    }

    @Test
    void atendenteNaoPodeAcessarTelaDeMedicos() throws Exception {
        mockMvc.perform(get("/medicos")
                        .with(httpBasic("atendente", "atendente123")))
                .andExpect(status().isForbidden());
    }

    @Test
    void atendenteNaoPodeCadastrarMedicoNaApi() throws Exception {
        mockMvc.perform(post("/api/medicos")
                        .with(httpBasic("atendente", "atendente123"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Dra Teste",
                                  "especialidade": "Clinica Geral",
                                  "crm": "CRM123",
                                  "email": "medico@teste.com"
                                }
                                """))
                .andExpect(status().isForbidden());
    }
}
