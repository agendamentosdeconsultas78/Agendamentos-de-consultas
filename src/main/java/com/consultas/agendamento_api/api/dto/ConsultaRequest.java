package com.consultas.agendamento_api.api.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ConsultaRequest(
        @NotNull(message = "Selecione o paciente.")
        Long pacienteId,

        @NotNull(message = "Selecione o medico.")
        Long medicoId,

        @NotNull(message = "Informe a data e horario.")
        @Future(message = "A consulta precisa ser agendada para uma data futura.")
        LocalDateTime dataHora,

        String observacoes) {
}
