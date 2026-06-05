package com.consultas.agendamento_api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.consultas.agendamento_api.model.Paciente;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PacienteRepositoryTest {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Test
    void deveEncontrarEmailIgnorandoMaiusculas() {
        pacienteRepository.save(paciente("Ana Silva", "ana@email.com"));

        boolean existe = pacienteRepository.existsByEmailIgnoreCase("ANA@EMAIL.COM");

        assertThat(existe).isTrue();
    }

    @Test
    void deveListarPacientesOrdenadosPorNome() {
        pacienteRepository.save(paciente("Zelia Costa", "zelia@email.com"));
        pacienteRepository.save(paciente("Bruno Lima", "bruno@email.com"));

        var pacientes = pacienteRepository.findAllByOrderByNomeAsc();

        assertThat(pacientes).extracting(Paciente::getNome).containsExactly("Bruno Lima", "Zelia Costa");
    }

    private Paciente paciente(String nome, String email) {
        Paciente paciente = new Paciente();
        paciente.setNome(nome);
        paciente.setEmail(email);
        paciente.setTelefone("(85) 99999-0000");
        paciente.setDataNascimento(LocalDate.of(1990, 1, 10));
        return paciente;
    }
}
