package com.consultas.agendamento_api.api;

import com.consultas.agendamento_api.api.dto.MedicoResponse;
import com.consultas.agendamento_api.service.AgendamentoService;
import com.consultas.agendamento_api.web.form.MedicoForm;
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
@RequestMapping("/api/medicos")
public class MedicoController {

    private final AgendamentoService agendamentoService;

    public MedicoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping
    public List<MedicoResponse> listar() {
        return agendamentoService.listarMedicos()
                .stream()
                .map(MedicoResponse::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicoResponse cadastrar(@Valid @RequestBody MedicoForm form) {
        return MedicoResponse.from(agendamentoService.cadastrarMedico(form));
    }
}
