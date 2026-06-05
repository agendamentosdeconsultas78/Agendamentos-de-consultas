package com.consultas.agendamento_api.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;

public record PacienteRequest(
        @NotBlank(message = "Informe o nome do paciente.")
        String nome,

        @Email(message = "Informe um e-mail valido.")
        @NotBlank(message = "Informe o e-mail do paciente.")
        String email,

        @NotBlank(message = "Informe o telefone do paciente.")
        String telefone,

        @Past(message = "A data de nascimento deve estar no passado.")
        LocalDate dataNascimento) {
}
