package com.consultas.agendamento_api.service;

import com.consultas.agendamento_api.model.Consulta;
import com.consultas.agendamento_api.model.Medico;
import com.consultas.agendamento_api.model.Paciente;
import com.consultas.agendamento_api.model.StatusConsulta;
import com.consultas.agendamento_api.repository.ConsultaRepository;
import com.consultas.agendamento_api.repository.MedicoRepository;
import com.consultas.agendamento_api.repository.PacienteRepository;
import com.consultas.agendamento_api.web.form.ConsultaForm;
import com.consultas.agendamento_api.web.form.MedicoForm;
import com.consultas.agendamento_api.web.form.PacienteForm;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AgendamentoService {

    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final ConsultaRepository consultaRepository;

    public AgendamentoService(
            PacienteRepository pacienteRepository,
            MedicoRepository medicoRepository,
            ConsultaRepository consultaRepository) {
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.consultaRepository = consultaRepository;
    }

    @Transactional
    public Paciente cadastrarPaciente(PacienteForm form) {
        if (pacienteRepository.existsByEmailIgnoreCase(form.getEmail())) {
            throw new RegraNegocioException("Ja existe um paciente cadastrado com este e-mail.");
        }

        Paciente paciente = new Paciente();
        paciente.setNome(form.getNome());
        paciente.setEmail(form.getEmail());
        paciente.setTelefone(form.getTelefone());
        paciente.setDataNascimento(form.getDataNascimento());
        return pacienteRepository.save(paciente);
    }

    @Transactional
    public Medico cadastrarMedico(MedicoForm form) {
        if (medicoRepository.existsByCrmIgnoreCase(form.getCrm())) {
            throw new RegraNegocioException("Ja existe um medico cadastrado com este CRM.");
        }

        Medico medico = new Medico();
        medico.setNome(form.getNome());
        medico.setEspecialidade(form.getEspecialidade());
        medico.setCrm(form.getCrm());
        medico.setEmail(form.getEmail());
        return medicoRepository.save(medico);
    }

    @Transactional
    public Consulta agendarConsulta(ConsultaForm form) {
        Paciente paciente = pacienteRepository.findById(form.getPacienteId())
                .orElseThrow(() -> new RegraNegocioException("Paciente nao encontrado."));
        Medico medico = medicoRepository.findById(form.getMedicoId())
                .orElseThrow(() -> new RegraNegocioException("Medico nao encontrado."));

        boolean horarioOcupado = consultaRepository.existsByMedicoIdAndDataHoraAndStatusNot(
                medico.getId(),
                form.getDataHora(),
                StatusConsulta.CANCELADA);

        if (horarioOcupado) {
            throw new RegraNegocioException("O medico ja possui uma consulta ativa neste horario.");
        }

        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setDataHora(form.getDataHora());
        consulta.setObservacoes(form.getObservacoes());
        consulta.setStatus(StatusConsulta.AGENDADA);
        return consultaRepository.save(consulta);
    }

    @Transactional
    public void atualizarStatus(Long consultaId, StatusConsulta status) {
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new RegraNegocioException("Consulta nao encontrada."));
        consulta.setStatus(status);
        consultaRepository.save(consulta);
    }

    @Transactional(readOnly = true)
    public List<Paciente> listarPacientes() {
        return pacienteRepository.findAllByOrderByNomeAsc();
    }

    @Transactional(readOnly = true)
    public List<Medico> listarMedicos() {
        return medicoRepository.findAllByOrderByNomeAsc();
    }

    @Transactional(readOnly = true)
    public List<Consulta> listarConsultas() {
        return consultaRepository.findAllByOrderByDataHoraAsc();
    }
}
