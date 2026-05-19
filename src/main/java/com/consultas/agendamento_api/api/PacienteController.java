package com.consultas.agendamento_api.api;

import com.consultas.agendamento_api.api.dto.PacienteResponse;
import com.consultas.agendamento_api.service.AgendamentoService;
import com.consultas.agendamento_api.web.form.PacienteForm;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final AgendamentoService agendamentoService;

    public PacienteController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping
    public List<PacienteResponse> listar() {
        return agendamentoService.listarPacientes()
                .stream()
                .map(PacienteResponse::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PacienteResponse cadastrar(@Valid @RequestBody PacienteForm form) {
        return PacienteResponse.from(agendamentoService.cadastrarPaciente(form));
    }
}
