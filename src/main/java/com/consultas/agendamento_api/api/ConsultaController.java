package com.consultas.agendamento_api.api;

import com.consultas.agendamento_api.api.dto.AtualizarStatusRequest;
import com.consultas.agendamento_api.api.dto.ConsultaResponse;
import com.consultas.agendamento_api.service.AgendamentoService;
import com.consultas.agendamento_api.web.form.ConsultaForm;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    private final AgendamentoService agendamentoService;

    public ConsultaController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping
    public List<ConsultaResponse> listar() {
        return agendamentoService.listarConsultas()
                .stream()
                .map(ConsultaResponse::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConsultaResponse agendar(@Valid @RequestBody ConsultaForm form) {
        return ConsultaResponse.from(agendamentoService.agendarConsulta(form));
    }

    @PatchMapping("/{id}/status")
    public ConsultaResponse atualizarStatus(@PathVariable Long id, @Valid @RequestBody AtualizarStatusRequest request) {
        agendamentoService.atualizarStatus(id, request.status());
        return agendamentoService.listarConsultas()
                .stream()
                .filter(consulta -> consulta.getId().equals(id))
                .findFirst()
                .map(ConsultaResponse::from)
                .orElseThrow(() -> new IllegalStateException("Consulta atualizada nao encontrada."));
    }
}
