package com.consultas.agendamento_api.api.dto;

import com.consultas.agendamento_api.model.Medico;

public record MedicoResponse(
        Long id,
        String nome,
        String especialidade,
        String crm,
        String email) {

    public static MedicoResponse from(Medico medico) {
        return new MedicoResponse(
                medico.getId(),
                medico.getNome(),
                medico.getEspecialidade(),
                medico.getCrm(),
                medico.getEmail());
    }
}
