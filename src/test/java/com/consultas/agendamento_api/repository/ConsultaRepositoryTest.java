package com.consultas.agendamento_api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.consultas.agendamento_api.model.Consulta;
import com.consultas.agendamento_api.model.Medico;
import com.consultas.agendamento_api.model.Paciente;
import com.consultas.agendamento_api.model.StatusConsulta;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ConsultaRepositoryTest {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Test
    void deveIdentificarHorarioOcupadoParaMedicoComConsultaAtiva() {
        Medico medico = medicoRepository.save(medico("Dra Ana", "CRM-1"));
        Paciente paciente = pacienteRepository.save(paciente("Carlos", "carlos@email.com"));
        LocalDateTime horario = LocalDateTime.of(2099, 6, 10, 9, 0);
        consultaRepository.save(consulta(paciente, medico, horario, StatusConsulta.AGENDADA));

        boolean ocupado = consultaRepository.existsByMedicoIdAndDataHoraAndStatusNot(
                medico.getId(), horario, StatusConsulta.CANCELADA);

        assertThat(ocupado).isTrue();
    }

    @Test
    void deveIgnorarConsultaCanceladaAoVerificarHorarioOcupado() {
        Medico medico = medicoRepository.save(medico("Dr Bruno", "CRM-2"));
        Paciente paciente = pacienteRepository.save(paciente("Marina", "marina@email.com"));
        LocalDateTime horario = LocalDateTime.of(2099, 6, 11, 9, 0);
        consultaRepository.save(consulta(paciente, medico, horario, StatusConsulta.CANCELADA));

        boolean ocupado = consultaRepository.existsByMedicoIdAndDataHoraAndStatusNot(
                medico.getId(), horario, StatusConsulta.CANCELADA);

        assertThat(ocupado).isFalse();
    }

    @Test
    void deveListarConsultasOrdenadasPorDataHora() {
        Medico medico = medicoRepository.save(medico("Dra Clara", "CRM-3"));
        Paciente paciente = pacienteRepository.save(paciente("Rita", "rita@email.com"));
        LocalDateTime depois = LocalDateTime.of(2099, 6, 12, 9, 0);
        LocalDateTime antes = LocalDateTime.of(2099, 6, 9, 9, 0);
        consultaRepository.save(consulta(paciente, medico, depois, StatusConsulta.AGENDADA));
        consultaRepository.save(consulta(paciente, medico, antes, StatusConsulta.AGENDADA));

        var consultas = consultaRepository.findAllByOrderByDataHoraAsc();

        assertThat(consultas).extracting(Consulta::getDataHora).containsExactly(antes, depois);
        assertThat(consultas.get(0).getPaciente().getNome()).isEqualTo("Rita");
    }

    private Paciente paciente(String nome, String email) {
        Paciente paciente = new Paciente();
        paciente.setNome(nome);
        paciente.setEmail(email);
        paciente.setTelefone("(85) 99999-0000");
        paciente.setDataNascimento(LocalDate.of(1992, 2, 20));
        return paciente;
    }

    private Medico medico(String nome, String crm) {
        Medico medico = new Medico();
        medico.setNome(nome);
        medico.setEspecialidade("Ortopedia");
        medico.setCrm(crm);
        medico.setEmail(crm.toLowerCase().replace("-", "") + "@clinic.com");
        return medico;
    }

    private Consulta consulta(Paciente paciente, Medico medico, LocalDateTime dataHora, StatusConsulta status) {
        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setDataHora(dataHora);
        consulta.setStatus(status);
        consulta.setObservacoes("Retorno");
        return consulta;
    }
}
