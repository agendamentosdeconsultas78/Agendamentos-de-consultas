package com.consultas.agendamento_api.api;

import com.consultas.agendamento_api.api.dto.MedicoRequest;
import com.consultas.agendamento_api.api.dto.MedicoResponse;
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
@RequestMapping("/api/medicos")
@Tag(name = "Medicos", description = "Operacoes de cadastro e consulta de medicos")
public class MedicoController {

    private final AgendamentoService agendamentoService;

    public MedicoController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping
    @Operation(summary = "Lista medicos", description = "Retorna todos os medicos cadastrados ordenados por nome.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Medicos listados com sucesso"),
            @ApiResponse(responseCode = "401", description = "Autenticacao obrigatoria")
    })
    public List<MedicoResponse> listar() {
        return agendamentoService.listarMedicos()
                .stream()
                .map(MedicoResponse::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Cadastra medico", description = "Cria um medico. Endpoint restrito ao perfil ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Medico cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados invalidos ou CRM duplicado"),
            @ApiResponse(responseCode = "401", description = "Autenticacao obrigatoria"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    public MedicoResponse cadastrar(@Valid @RequestBody MedicoRequest request) {
        return MedicoResponse.from(agendamentoService.cadastrarMedico(request));
    }
}
