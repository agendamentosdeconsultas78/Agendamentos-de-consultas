package com.consultas.agendamento_api.service;

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
        return cadastrarPaciente(form.getNome(), form.getEmail(), form.getTelefone(), form.getDataNascimento());
    }

    @Transactional
    public Paciente cadastrarPaciente(PacienteRequest request) {
        return cadastrarPaciente(request.nome(), request.email(), request.telefone(), request.dataNascimento());
    }

    private Paciente cadastrarPaciente(String nome, String email, String telefone, java.time.LocalDate dataNascimento) {
        if (pacienteRepository.existsByEmailIgnoreCase(email)) {
            throw new RegraNegocioException("Ja existe um paciente cadastrado com este e-mail.");
        }

        Paciente paciente = new Paciente();
        paciente.setNome(nome);
        paciente.setEmail(email);
        paciente.setTelefone(telefone);
        paciente.setDataNascimento(dataNascimento);
        return pacienteRepository.save(paciente);
    }

    @Transactional
    public Medico cadastrarMedico(MedicoForm form) {
        return cadastrarMedico(form.getNome(), form.getEspecialidade(), form.getCrm(), form.getEmail());
    }

    @Transactional
    public Medico cadastrarMedico(MedicoRequest request) {
        return cadastrarMedico(request.nome(), request.especialidade(), request.crm(), request.email());
    }

    private Medico cadastrarMedico(String nome, String especialidade, String crm, String email) {
        if (medicoRepository.existsByCrmIgnoreCase(crm)) {
            throw new RegraNegocioException("Ja existe um medico cadastrado com este CRM.");
        }

        Medico medico = new Medico();
        medico.setNome(nome);
        medico.setEspecialidade(especialidade);
        medico.setCrm(crm);
        medico.setEmail(email);
        return medicoRepository.save(medico);
    }

    @Transactional
    public Consulta agendarConsulta(ConsultaForm form) {
        return agendarConsulta(form.getPacienteId(), form.getMedicoId(), form.getDataHora(), form.getObservacoes());
    }

    @Transactional
    public Consulta agendarConsulta(ConsultaRequest request) {
        return agendarConsulta(request.pacienteId(), request.medicoId(), request.dataHora(), request.observacoes());
    }

    private Consulta agendarConsulta(
            Long pacienteId,
            Long medicoId,
            java.time.LocalDateTime dataHora,
            String observacoes) {
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new RegraNegocioException("Paciente nao encontrado."));
        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new RegraNegocioException("Medico nao encontrado."));

        boolean horarioOcupado = consultaRepository.existsByMedicoIdAndDataHoraAndStatusNot(
                medico.getId(),
                dataHora,
                StatusConsulta.CANCELADA);

        if (horarioOcupado) {
            throw new RegraNegocioException("O medico ja possui uma consulta ativa neste horario.");
        }

        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setDataHora(dataHora);
        consulta.setObservacoes(observacoes);
        consulta.setStatus(StatusConsulta.AGENDADA);
        return consultaRepository.save(consulta);
    }

    @Transactional
    public Consulta atualizarStatus(Long consultaId, StatusConsulta status) {
        Consulta consulta = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new RegraNegocioException("Consulta nao encontrada."));
        consulta.setStatus(status);
        return consultaRepository.save(consulta);
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

    @Transactional(readOnly = true)
    public Consulta buscarConsultaPorId(Long consultaId) {
        return consultaRepository.findById(consultaId)
                .orElseThrow(() -> new RegraNegocioException("Consulta nao encontrada."));
    }
}
