package com.consultas.agendamento_api.repository;

import com.consultas.agendamento_api.model.Paciente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    boolean existsByEmailIgnoreCase(String email);

    List<Paciente> findAllByOrderByNomeAsc();
}
