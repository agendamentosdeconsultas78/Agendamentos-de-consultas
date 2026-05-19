package com.consultas.agendamento_api.repository;

import com.consultas.agendamento_api.model.Consulta;
import com.consultas.agendamento_api.model.StatusConsulta;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    boolean existsByMedicoIdAndDataHoraAndStatusNot(Long medicoId, LocalDateTime dataHora, StatusConsulta status);

    @EntityGraph(attributePaths = {"paciente", "medico"})
    List<Consulta> findAllByOrderByDataHoraAsc();
}
