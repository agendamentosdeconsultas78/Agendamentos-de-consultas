package com.consultas.agendamento_api.api;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.consultas.agendamento_api.model.Medico;
import com.consultas.agendamento_api.service.AgendamentoService;
import com.consultas.agendamento_api.service.RegraNegocioException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MedicoController.class)
@AutoConfigureMockMvc(addFilters = false)
class MedicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AgendamentoService agendamentoService;

    @Test
    void deveListarMedicos() throws Exception {
        when(agendamentoService.listarMedicos()).thenReturn(List.of(medico()));

        mockMvc.perform(get("/api/medicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Dr Joao"))
                .andExpect(jsonPath("$[0].crm").value("CRM123"));
    }

    @Test
    void deveCadastrarMedicoValido() throws Exception {
        when(agendamentoService.cadastrarMedico(any(com.consultas.agendamento_api.api.dto.MedicoRequest.class)))
                .thenReturn(medico());

        mockMvc.perform(post("/api/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Dr Joao",
                                  "especialidade": "Cardiologia",
                                  "crm": "CRM123",
                                  "email": "joao@email.com"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.especialidade").value("Cardiologia"));
    }

    @Test
    void deveRetornarBadRequestQuandoMedicoForInvalido() throws Exception {
        mockMvc.perform(post("/api/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "",
                                  "especialidade": "",
                                  "crm": "",
                                  "email": "email-invalido"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Dados invalidos"));
    }

    @Test
    void deveRetornarBadRequestQuandoCrmJaExistir() throws Exception {
        when(agendamentoService.cadastrarMedico(any(com.consultas.agendamento_api.api.dto.MedicoRequest.class)))
                .thenThrow(new RegraNegocioException("Ja existe um medico cadastrado com este CRM."));

        mockMvc.perform(post("/api/medicos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Dr Joao",
                                  "especialidade": "Cardiologia",
                                  "crm": "CRM123",
                                  "email": "joao@email.com"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details[0]", containsString("CRM")));
    }

    private Medico medico() {
        Medico medico = new Medico();
        medico.setId(2L);
        medico.setNome("Dr Joao");
        medico.setEspecialidade("Cardiologia");
        medico.setCrm("CRM123");
        medico.setEmail("joao@email.com");
        return medico;
    }
}
