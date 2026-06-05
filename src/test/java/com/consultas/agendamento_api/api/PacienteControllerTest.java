package com.consultas.agendamento_api.api;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.consultas.agendamento_api.model.Paciente;
import com.consultas.agendamento_api.service.AgendamentoService;
import com.consultas.agendamento_api.service.RegraNegocioException;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PacienteController.class)
@AutoConfigureMockMvc(addFilters = false)
class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AgendamentoService agendamentoService;

    @Test
    void deveListarPacientes() throws Exception {
        when(agendamentoService.listarPacientes()).thenReturn(List.of(paciente()));

        mockMvc.perform(get("/api/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Ana Silva"))
                .andExpect(jsonPath("$[0].email").value("ana@email.com"));
    }

    @Test
    void deveCadastrarPacienteValido() throws Exception {
        when(agendamentoService.cadastrarPaciente(any(com.consultas.agendamento_api.api.dto.PacienteRequest.class)))
                .thenReturn(paciente());

        mockMvc.perform(post("/api/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Ana Silva",
                                  "email": "ana@email.com",
                                  "telefone": "(85) 99999-0000",
                                  "dataNascimento": "1995-03-10"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Ana Silva"));
    }

    @Test
    void deveRetornarBadRequestQuandoPacienteForInvalido() throws Exception {
        mockMvc.perform(post("/api/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "",
                                  "email": "email-invalido",
                                  "telefone": "",
                                  "dataNascimento": "2099-01-01"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Dados invalidos"));
    }

    @Test
    void deveRetornarBadRequestQuandoEmailJaExistir() throws Exception {
        when(agendamentoService.cadastrarPaciente(any(com.consultas.agendamento_api.api.dto.PacienteRequest.class)))
                .thenThrow(new RegraNegocioException("Ja existe um paciente cadastrado com este e-mail."));

        mockMvc.perform(post("/api/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Ana Silva",
                                  "email": "ana@email.com",
                                  "telefone": "(85) 99999-0000",
                                  "dataNascimento": "1995-03-10"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details[0]", containsString("e-mail")));
    }

    private Paciente paciente() {
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNome("Ana Silva");
        paciente.setEmail("ana@email.com");
        paciente.setTelefone("(85) 99999-0000");
        paciente.setDataNascimento(LocalDate.of(1995, 3, 10));
        return paciente;
    }
}
