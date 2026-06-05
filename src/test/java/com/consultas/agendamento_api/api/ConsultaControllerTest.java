package com.consultas.agendamento_api.api;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.consultas.agendamento_api.model.Consulta;
import com.consultas.agendamento_api.model.Medico;
import com.consultas.agendamento_api.model.Paciente;
import com.consultas.agendamento_api.model.StatusConsulta;
import com.consultas.agendamento_api.service.AgendamentoService;
import com.consultas.agendamento_api.service.RegraNegocioException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ConsultaController.class)
@AutoConfigureMockMvc(addFilters = false)
class ConsultaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AgendamentoService agendamentoService;

    @Test
    void deveListarConsultas() throws Exception {
        when(agendamentoService.listarConsultas()).thenReturn(List.of(consulta(StatusConsulta.AGENDADA)));

        mockMvc.perform(get("/api/consultas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pacienteNome").value("Ana Silva"))
                .andExpect(jsonPath("$[0].medicoNome").value("Dr Joao"));
    }

    @Test
    void deveAgendarConsultaValida() throws Exception {
        when(agendamentoService.agendarConsulta(any(com.consultas.agendamento_api.api.dto.ConsultaRequest.class)))
                .thenReturn(consulta(StatusConsulta.AGENDADA));

        mockMvc.perform(post("/api/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "pacienteId": 1,
                                  "medicoId": 2,
                                  "dataHora": "2099-06-10T09:00:00",
                                  "observacoes": "Primeira consulta"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("AGENDADA"));
    }

    @Test
    void deveRetornarBadRequestQuandoConsultaForInvalida() throws Exception {
        mockMvc.perform(post("/api/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "pacienteId": null,
                                  "medicoId": null,
                                  "dataHora": "2000-01-01T09:00:00"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Dados invalidos"));
    }

    @Test
    void deveRetornarBadRequestQuandoHorarioEstiverOcupado() throws Exception {
        when(agendamentoService.agendarConsulta(any(com.consultas.agendamento_api.api.dto.ConsultaRequest.class)))
                .thenThrow(new RegraNegocioException("O medico ja possui uma consulta ativa neste horario."));

        mockMvc.perform(post("/api/consultas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "pacienteId": 1,
                                  "medicoId": 2,
                                  "dataHora": "2099-06-10T09:00:00"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details[0]", containsString("horario")));
    }

    @Test
    void deveAtualizarStatus() throws Exception {
        when(agendamentoService.atualizarStatus(eq(10L), eq(StatusConsulta.CONFIRMADA)))
                .thenReturn(consulta(StatusConsulta.CONFIRMADA));

        mockMvc.perform(patch("/api/consultas/10/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "status": "CONFIRMADA"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMADA"));
    }

    @Test
    void deveRetornarBadRequestQuandoStatusForNulo() throws Exception {
        mockMvc.perform(patch("/api/consultas/10/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "status": null
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Dados invalidos"));
    }

    private Consulta consulta(StatusConsulta status) {
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNome("Ana Silva");

        Medico medico = new Medico();
        medico.setId(2L);
        medico.setNome("Dr Joao");

        Consulta consulta = new Consulta();
        consulta.setId(10L);
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setDataHora(LocalDateTime.of(2099, 6, 10, 9, 0));
        consulta.setStatus(status);
        consulta.setObservacoes("Primeira consulta");
        return consulta;
    }
}
