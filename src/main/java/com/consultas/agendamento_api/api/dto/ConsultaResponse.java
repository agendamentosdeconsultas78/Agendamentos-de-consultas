package com.consultas.agendamento_api.api.dto;

import com.consultas.agendamento_api.model.Consulta;
import com.consultas.agendamento_api.model.StatusConsulta;
import java.time.LocalDateTime;

public record ConsultaResponse(
        Long id,
        Long pacienteId,
        String pacienteNome,
        Long medicoId,
        String medicoNome,
        LocalDateTime dataHora,
        StatusConsulta status,
        String observacoes) {

    public static ConsultaResponse from(Consulta consulta) {
        return new ConsultaResponse(
                consulta.getId(),
                consulta.getPaciente().getId(),
                consulta.getPaciente().getNome(),
                consulta.getMedico().getId(),
                consulta.getMedico().getNome(),
                consulta.getDataHora(),
                consulta.getStatus(),
                consulta.getObservacoes());
    }
}
