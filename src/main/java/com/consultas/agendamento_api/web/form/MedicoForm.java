package com.consultas.agendamento_api.web.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class MedicoForm {

    @NotBlank(message = "Informe o nome do medico.")
    private String nome;

    @NotBlank(message = "Informe a especialidade.")
    private String especialidade;

    @NotBlank(message = "Informe o CRM.")
    private String crm;

    @Email(message = "Informe um e-mail valido.")
    @NotBlank(message = "Informe o e-mail do medico.")
    private String email;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
