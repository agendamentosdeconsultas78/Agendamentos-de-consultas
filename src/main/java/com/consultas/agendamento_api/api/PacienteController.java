package com.consultas.agendamento_api.api;

import com.consultas.agendamento_api.api.dto.PacienteRequest;
import com.consultas.agendamento_api.api.dto.PacienteResponse;
import com.consultas.agendamento_api.service.AgendamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Pacientes", description = "Operacoes de cadastro e consulta de pacientes")
public class PacienteController {

    private final AgendamentoService agendamentoService;

    public PacienteController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping
    @Operation(summary = "Lista pacientes", description = "Retorna todos os pacientes cadastrados ordenados por nome.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pacientes listados com sucesso"),
            @ApiResponse(responseCode = "401", description = "Autenticacao obrigatoria")
    })
    public List<PacienteResponse> listar() {
        return agendamentoService.listarPacientes()
                .stream()
                .map(PacienteResponse::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra paciente", description = "Cria um paciente com dados validados.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Paciente cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou e-mail duplicado"),
            @ApiResponse(responseCode = "401", description = "Autenticacao obrigatoria")
    })
    public PacienteResponse cadastrar(@Valid @RequestBody PacienteRequest request) {
        return PacienteResponse.from(agendamentoService.cadastrarPaciente(request));
    }
}
