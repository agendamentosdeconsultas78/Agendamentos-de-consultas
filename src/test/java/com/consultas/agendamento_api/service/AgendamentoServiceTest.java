package com.consultas.agendamento_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.consultas.agendamento_api.model.Medico;
import com.consultas.agendamento_api.model.Paciente;
import com.consultas.agendamento_api.repository.ConsultaRepository;
import com.consultas.agendamento_api.repository.MedicoRepository;
import com.consultas.agendamento_api.repository.PacienteRepository;
import com.consultas.agendamento_api.web.form.ConsultaForm;
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
    void naoDeveAgendarQuandoHorarioDoMedicoEstiverOcupado() {
        ConsultaForm form = new ConsultaForm();
        form.setPacienteId(1L);
        form.setMedicoId(2L);
        form.setDataHora(LocalDateTime.now().plusDays(1));

        Paciente paciente = new Paciente();
        paciente.setId(1L);

        Medico medico = new Medico();
        medico.setId(2L);

        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(medicoRepository.findById(2L)).thenReturn(Optional.of(medico));
        when(consultaRepository.existsByMedicoIdAndDataHoraAndStatusNot(any(), any(), any())).thenReturn(true);

        RegraNegocioException exception =
                assertThrows(RegraNegocioException.class, () -> agendamentoService.agendarConsulta(form));

        assertEquals("O medico ja possui uma consulta ativa neste horario.", exception.getMessage());
        verify(consultaRepository, never()).save(any());
    }
}
