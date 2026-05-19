package com.consultas.agendamento_api.api.dto;

import com.consultas.agendamento_api.model.StatusConsulta;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusRequest(
        @NotNull(message = "Informe o status da consulta.")
        StatusConsulta status) {
}
