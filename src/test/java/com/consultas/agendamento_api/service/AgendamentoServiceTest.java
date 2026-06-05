package com.consultas.agendamento_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.consultas.agendamento_api.api.dto.ConsultaRequest;
import com.consultas.agendamento_api.api.dto.MedicoRequest;
import com.consultas.agendamento_api.api.dto.PacienteRequest;
import com.consultas.agendamento_api.model.Consulta;
import com.consultas.agendamento_api.model.Medico;
import com.consultas.agendamento_api.model.Paciente;
import com.consultas.agendamento_api.model.StatusConsulta;
import com.consultas.agendamento_api.repository.ConsultaRepository;
import com.consultas.agendamento_api.repository.MedicoRepository;
import com.consultas.agendamento_api.repository.PacienteRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private ConsultaRepository consultaRepository;

    private AgendamentoService agendamentoService;

    @BeforeEach
    void setup() {
        agendamentoService = new AgendamentoService(pacienteRepository, medicoRepository, consultaRepository);
    }

    @Test
    void deveCadastrarPacienteQuandoEmailNaoExiste() {
        PacienteRequest request = new PacienteRequest(
                "Ana Silva", "ana@email.com", "(85) 99999-0000", LocalDate.of(1995, 3, 10));
        when(pacienteRepository.existsByEmailIgnoreCase("ana@email.com")).thenReturn(false);
        when(pacienteRepository.save(any(Paciente.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Paciente paciente = agendamentoService.cadastrarPaciente(request);

        assertEquals("Ana Silva", paciente.getNome());
        assertEquals("ana@email.com", paciente.getEmail());
        verify(pacienteRepository).save(any(Paciente.class));
    }

    @Test
    void naoDeveCadastrarPacienteComEmailDuplicado() {
        PacienteRequest request = new PacienteRequest(
                "Ana Silva", "ana@email.com", "(85) 99999-0000", LocalDate.of(1995, 3, 10));
        when(pacienteRepository.existsByEmailIgnoreCase("ana@email.com")).thenReturn(true);

        RegraNegocioException exception =
                assertThrows(RegraNegocioException.class, () -> agendamentoService.cadastrarPaciente(request));

        assertEquals("Ja existe um paciente cadastrado com este e-mail.", exception.getMessage());
        verify(pacienteRepository, never()).save(any());
    }

    @Test
    void deveCadastrarMedicoQuandoCrmNaoExiste() {
        MedicoRequest request = new MedicoRequest("Dr Joao", "Cardiologia", "CRM123", "joao@email.com");
        when(medicoRepository.existsByCrmIgnoreCase("CRM123")).thenReturn(false);
        when(medicoRepository.save(any(Medico.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Medico medico = agendamentoService.cadastrarMedico(request);

        assertEquals("Dr Joao", medico.getNome());
        assertEquals("Cardiologia", medico.getEspecialidade());
        verify(medicoRepository).save(any(Medico.class));
    }

    @Test
    void naoDeveCadastrarMedicoComCrmDuplicado() {
        MedicoRequest request = new MedicoRequest("Dr Joao", "Cardiologia", "CRM123", "joao@email.com");
        when(medicoRepository.existsByCrmIgnoreCase("CRM123")).thenReturn(true);

        RegraNegocioException exception =
                assertThrows(RegraNegocioException.class, () -> agendamentoService.cadastrarMedico(request));

        assertEquals("Ja existe um medico cadastrado com este CRM.", exception.getMessage());
        verify(medicoRepository, never()).save(any());
    }

    @Test
    void deveAgendarConsultaQuandoHorarioEstiverLivre() {
        LocalDateTime dataHora = LocalDateTime.now().plusDays(1);
        ConsultaRequest request = new ConsultaRequest(1L, 2L, dataHora, "Primeira consulta");
        Paciente paciente = paciente(1L, "Ana");
        Medico medico = medico(2L, "Dr Joao");

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(2L)).thenReturn(Optional.of(medico));
        when(consultaRepository.existsByMedicoIdAndDataHoraAndStatusNot(2L, dataHora, StatusConsulta.CANCELADA))
                .thenReturn(false);
        when(consultaRepository.save(any(Consulta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Consulta consulta = agendamentoService.agendarConsulta(request);

        assertEquals(paciente, consulta.getPaciente());
        assertEquals(medico, consulta.getMedico());
        assertEquals(StatusConsulta.AGENDADA, consulta.getStatus());
        verify(consultaRepository).save(any(Consulta.class));
    }

    @Test
    void naoDeveAgendarQuandoPacienteNaoExiste() {
        ConsultaRequest request = new ConsultaRequest(1L, 2L, LocalDateTime.now().plusDays(1), null);
        when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

        RegraNegocioException exception =
                assertThrows(RegraNegocioException.class, () -> agendamentoService.agendarConsulta(request));

        assertEquals("Paciente nao encontrado.", exception.getMessage());
        verify(consultaRepository, never()).save(any());
    }

    @Test
    void naoDeveAgendarQuandoMedicoNaoExiste() {
        ConsultaRequest request = new ConsultaRequest(1L, 2L, LocalDateTime.now().plusDays(1), null);
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente(1L, "Ana")));
        when(medicoRepository.findById(2L)).thenReturn(Optional.empty());

        RegraNegocioException exception =
                assertThrows(RegraNegocioException.class, () -> agendamentoService.agendarConsulta(request));

        assertEquals("Medico nao encontrado.", exception.getMessage());
        verify(consultaRepository, never()).save(any());
    }

    @Test
    void naoDeveAgendarQuandoHorarioDoMedicoEstiverOcupado() {
        LocalDateTime dataHora = LocalDateTime.now().plusDays(1);
        ConsultaRequest request = new ConsultaRequest(1L, 2L, dataHora, null);
        Medico medico = medico(2L, "Dr Joao");

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente(1L, "Ana")));
        when(medicoRepository.findById(2L)).thenReturn(Optional.of(medico));
        when(consultaRepository.existsByMedicoIdAndDataHoraAndStatusNot(2L, dataHora, StatusConsulta.CANCELADA))
                .thenReturn(true);

        RegraNegocioException exception =
                assertThrows(RegraNegocioException.class, () -> agendamentoService.agendarConsulta(request));

        assertEquals("O medico ja possui uma consulta ativa neste horario.", exception.getMessage());
        verify(consultaRepository, never()).save(any());
    }

    @Test
    void deveAtualizarStatusDaConsulta() {
        Consulta consulta = new Consulta();
        consulta.setId(10L);
        consulta.setStatus(StatusConsulta.AGENDADA);
        when(consultaRepository.findById(10L)).thenReturn(Optional.of(consulta));
        when(consultaRepository.save(consulta)).thenReturn(consulta);

        Consulta atualizada = agendamentoService.atualizarStatus(10L, StatusConsulta.CONFIRMADA);

        assertEquals(StatusConsulta.CONFIRMADA, atualizada.getStatus());
        verify(consultaRepository).save(consulta);
    }

    @Test
    void naoDeveAtualizarStatusQuandoConsultaNaoExiste() {
        when(consultaRepository.findById(10L)).thenReturn(Optional.empty());

        RegraNegocioException exception =
                assertThrows(RegraNegocioException.class,
                        () -> agendamentoService.atualizarStatus(10L, StatusConsulta.CONFIRMADA));

        assertEquals("Consulta nao encontrada.", exception.getMessage());
        verify(consultaRepository, never()).save(any());
    }

    private Paciente paciente(Long id, String nome) {
        Paciente paciente = new Paciente();
        paciente.setId(id);
        paciente.setNome(nome);
        return paciente;
    }

    private Medico medico(Long id, String nome) {
        Medico medico = new Medico();
        medico.setId(id);
        medico.setNome(nome);
        return medico;
    }
}
