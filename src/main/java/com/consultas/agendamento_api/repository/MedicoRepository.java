package com.consultas.agendamento_api.repository;

import com.consultas.agendamento_api.model.Medico;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    boolean existsByCrmIgnoreCase(String crm);

    List<Medico> findAllByOrderByNomeAsc();
}
