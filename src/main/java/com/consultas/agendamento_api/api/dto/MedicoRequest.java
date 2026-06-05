package com.consultas.agendamento_api.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MedicoRequest(
        @NotBlank(message = "Informe o nome do medico.")
        String nome,

        @NotBlank(message = "Informe a especialidade.")
        String especialidade,

        @NotBlank(message = "Informe o CRM.")
        String crm,

        @Email(message = "Informe um e-mail valido.")
        @NotBlank(message = "Informe o e-mail do medico.")
        String email) {
}
