package com.consultas.agendamento_api.web.form;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class WebFormTest {

    @Test
    void deveManipularPacienteForm() {
        LocalDate nascimento = LocalDate.of(1995, 3, 10);
        PacienteForm form = new PacienteForm();

        form.setNome("Ana Silva");
        form.setEmail("ana@email.com");
        form.setTelefone("(85) 99999-0000");
        form.setDataNascimento(nascimento);

        assertThat(form.getNome()).isEqualTo("Ana Silva");
        assertThat(form.getEmail()).isEqualTo("ana@email.com");
        assertThat(form.getTelefone()).isEqualTo("(85) 99999-0000");
        assertThat(form.getDataNascimento()).isEqualTo(nascimento);
    }

    @Test
    void deveManipularMedicoForm() {
        MedicoForm form = new MedicoForm();

        form.setNome("Dr Joao");
        form.setEspecialidade("Cardiologia");
        form.setCrm("CRM123");
        form.setEmail("joao@email.com");

        assertThat(form.getNome()).isEqualTo("Dr Joao");
        assertThat(form.getEspecialidade()).isEqualTo("Cardiologia");
        assertThat(form.getCrm()).isEqualTo("CRM123");
        assertThat(form.getEmail()).isEqualTo("joao@email.com");
    }

    @Test
    void deveManipularConsultaForm() {
        LocalDateTime dataHora = LocalDateTime.of(2099, 6, 10, 9, 0);
        ConsultaForm form = new ConsultaForm();

        form.setPacienteId(1L);
        form.setMedicoId(2L);
        form.setDataHora(dataHora);
        form.setObservacoes("Primeira consulta");

        assertThat(form.getPacienteId()).isEqualTo(1L);
        assertThat(form.getMedicoId()).isEqualTo(2L);
        assertThat(form.getDataHora()).isEqualTo(dataHora);
        assertThat(form.getObservacoes()).isEqualTo("Primeira consulta");
    }
}
