package com.consultas.agendamento_api.api.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.consultas.agendamento_api.model.Consulta;
import com.consultas.agendamento_api.model.Medico;
import com.consultas.agendamento_api.model.Paciente;
import com.consultas.agendamento_api.model.StatusConsulta;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class DtoMappingTest {

    @Test
    void deveMapearPacienteParaResponse() {
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNome("Ana Silva");
        paciente.setEmail("ana@email.com");
        paciente.setTelefone("(85) 99999-0000");
        paciente.setDataNascimento(LocalDate.of(1995, 3, 10));

        PacienteResponse response = PacienteResponse.from(paciente);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.nome()).isEqualTo("Ana Silva");
        assertThat(response.email()).isEqualTo("ana@email.com");
        assertThat(response.telefone()).isEqualTo("(85) 99999-0000");
        assertThat(response.dataNascimento()).isEqualTo(LocalDate.of(1995, 3, 10));
    }

    @Test
    void deveMapearMedicoParaResponse() {
        Medico medico = new Medico();
        medico.setId(2L);
        medico.setNome("Dr Joao");
        medico.setEspecialidade("Cardiologia");
        medico.setCrm("CRM123");
        medico.setEmail("joao@email.com");

        MedicoResponse response = MedicoResponse.from(medico);

        assertThat(response.id()).isEqualTo(2L);
        assertThat(response.nome()).isEqualTo("Dr Joao");
        assertThat(response.especialidade()).isEqualTo("Cardiologia");
        assertThat(response.crm()).isEqualTo("CRM123");
        assertThat(response.email()).isEqualTo("joao@email.com");
    }

    @Test
    void deveMapearConsultaParaResponse() {
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNome("Ana Silva");

        Medico medico = new Medico();
        medico.setId(2L);
        medico.setNome("Dr Joao");

        LocalDateTime dataHora = LocalDateTime.of(2099, 6, 10, 9, 0);
        Consulta consulta = new Consulta();
        consulta.setId(10L);
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setDataHora(dataHora);
        consulta.setStatus(StatusConsulta.CONFIRMADA);
        consulta.setObservacoes("Retorno");

        ConsultaResponse response = ConsultaResponse.from(consulta);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.pacienteId()).isEqualTo(1L);
        assertThat(response.pacienteNome()).isEqualTo("Ana Silva");
        assertThat(response.medicoId()).isEqualTo(2L);
        assertThat(response.medicoNome()).isEqualTo("Dr Joao");
        assertThat(response.dataHora()).isEqualTo(dataHora);
        assertThat(response.status()).isEqualTo(StatusConsulta.CONFIRMADA);
        assertThat(response.observacoes()).isEqualTo("Retorno");
    }
}
