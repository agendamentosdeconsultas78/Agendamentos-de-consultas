package com.consultas.agendamento_api.api;

import com.consultas.agendamento_api.api.dto.AtualizarStatusRequest;
import com.consultas.agendamento_api.api.dto.ConsultaRequest;
import com.consultas.agendamento_api.api.dto.ConsultaResponse;
import com.consultas.agendamento_api.service.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Consultas", description = "Operacoes de agendamento e status de consultas")
public class ConsultaController {

    private final AgendamentoService agendamentoService;

    public ConsultaController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping
    @Operation(summary = "Lista consultas", description = "Retorna todas as consultas ordenadas por data e hora.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consultas listadas com sucesso"),
            @ApiResponse(responseCode = "401", description = "Autenticacao obrigatoria")
    })
    public List<ConsultaResponse> listar() {
        return agendamentoService.listarConsultas()
                .stream()
                .map(ConsultaResponse::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Agenda consulta", description = "Cria uma consulta e impede conflito de horario do mesmo medico.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Consulta agendada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou regra de negocio violada"),
            @ApiResponse(responseCode = "401", description = "Autenticacao obrigatoria")
    })
    public ConsultaResponse agendar(@Valid @RequestBody ConsultaRequest request) {
        return ConsultaResponse.from(agendamentoService.agendarConsulta(request));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualiza status", description = "Atualiza o status de uma consulta. Endpoint restrito ao perfil ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Consulta inexistente ou status invalido"),
            @ApiResponse(responseCode = "401", description = "Autenticacao obrigatoria"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public ConsultaResponse atualizarStatus(@PathVariable Long id, @Valid @RequestBody AtualizarStatusRequest request) {
        return ConsultaResponse.from(agendamentoService.atualizarStatus(id, request.status()));
    }
}
