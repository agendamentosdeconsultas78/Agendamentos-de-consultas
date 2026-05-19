package com.consultas.agendamento_api.web.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public class PacienteForm {

    @NotBlank(message = "Informe o nome do paciente.")
    private String nome;

    @Email(message = "Informe um e-mail valido.")
    @NotBlank(message = "Informe o e-mail do paciente.")
    private String email;

    @NotBlank(message = "Informe o telefone do paciente.")
    private String telefone;

    @Past(message = "A data de nascimento deve estar no passado.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataNascimento;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
