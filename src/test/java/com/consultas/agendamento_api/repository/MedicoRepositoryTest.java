package com.consultas.agendamento_api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.consultas.agendamento_api.model.Medico;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MedicoRepositoryTest {

    @Autowired
    private MedicoRepository medicoRepository;

    @Test
    void deveEncontrarCrmIgnorandoMaiusculas() {
        medicoRepository.save(medico("Dra Ana", "CRM-CE-123"));

        boolean existe = medicoRepository.existsByCrmIgnoreCase("crm-ce-123");

        assertThat(existe).isTrue();
    }

    @Test
    void deveListarMedicosOrdenadosPorNome() {
        medicoRepository.save(medico("Dr Zeca", "CRM-CE-999"));
        medicoRepository.save(medico("Dra Beatriz", "CRM-CE-111"));

        var medicos = medicoRepository.findAllByOrderByNomeAsc();

        assertThat(medicos).extracting(Medico::getNome).containsExactly("Dr Zeca", "Dra Beatriz");
    }

    private Medico medico(String nome, String crm) {
        Medico medico = new Medico();
        medico.setNome(nome);
        medico.setEspecialidade("Clinica Geral");
        medico.setCrm(crm);
        medico.setEmail(crm.toLowerCase().replace("-", "") + "@clinic.com");
        return medico;
    }
}
