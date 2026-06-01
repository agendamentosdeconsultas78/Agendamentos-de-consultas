package com.consultas.agendamento_api.web;

import com.consultas.agendamento_api.model.StatusConsulta;
import com.consultas.agendamento_api.model.Consulta;
import com.consultas.agendamento_api.model.Medico;
import com.consultas.agendamento_api.model.Paciente;
import com.consultas.agendamento_api.service.AgendamentoService;
import com.consultas.agendamento_api.service.RegraNegocioException;
import com.consultas.agendamento_api.web.form.ConsultaForm;
import com.consultas.agendamento_api.web.form.MedicoForm;
import com.consultas.agendamento_api.web.form.PacienteForm;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    private final AgendamentoService agendamentoService;

    public HomeController(AgendamentoService agendamentoService) {
        this.agendamentoService = agendamentoService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        popularModelo(model, "dashboard");
        return "dashboard";
    }

    @GetMapping("/pacientes")
    public String pacientes(Model model) {
        popularModelo(model, "pacientes");
        return "pacientes";
    }

    @GetMapping("/medicos")
    public String medicos(Model model) {
        popularModelo(model, "medicos");
        return "medicos";
    }

    @GetMapping("/consultas")
    public String consultas(Model model) {
        popularModelo(model, "consultas");
        return "consultas";
    }

    @PostMapping("/pacientes")
    public String cadastrarPaciente(
            @Valid @ModelAttribute("pacienteForm") PacienteForm pacienteForm,
            BindingResult bindingResult,
            @ModelAttribute("medicoForm") MedicoForm medicoForm,
            @ModelAttribute("consultaForm") ConsultaForm consultaForm,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            popularModelo(model, "pacientes");
            return "pacientes";
        }

        try {
            agendamentoService.cadastrarPaciente(pacienteForm);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Paciente cadastrado com sucesso.");
            return "redirect:/pacientes";
        } catch (RegraNegocioException ex) {
            model.addAttribute("mensagemErro", ex.getMessage());
            popularModelo(model, "pacientes");
            return "pacientes";
        }
    }

    @PostMapping("/medicos")
    public String cadastrarMedico(
            @ModelAttribute("pacienteForm") PacienteForm pacienteForm,
            @Valid @ModelAttribute("medicoForm") MedicoForm medicoForm,
            BindingResult bindingResult,
            @ModelAttribute("consultaForm") ConsultaForm consultaForm,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            popularModelo(model, "medicos");
            return "medicos";
        }

        try {
            agendamentoService.cadastrarMedico(medicoForm);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Medico cadastrado com sucesso.");
            return "redirect:/medicos";
        } catch (RegraNegocioException ex) {
            model.addAttribute("mensagemErro", ex.getMessage());
            popularModelo(model, "medicos");
            return "medicos";
        }
    }

    @PostMapping("/consultas")
    public String agendarConsulta(
            @ModelAttribute("pacienteForm") PacienteForm pacienteForm,
            @ModelAttribute("medicoForm") MedicoForm medicoForm,
            @Valid @ModelAttribute("consultaForm") ConsultaForm consultaForm,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            popularModelo(model, "consultas");
            return "consultas";
        }

        try {
            agendamentoService.agendarConsulta(consultaForm);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Consulta agendada com sucesso.");
            return "redirect:/consultas";
        } catch (RegraNegocioException ex) {
            model.addAttribute("mensagemErro", ex.getMessage());
            popularModelo(model, "consultas");
            return "consultas";
        }
    }

    @PostMapping("/consultas/{id}/status")
    public String atualizarStatus(
            @PathVariable Long id,
            @RequestParam StatusConsulta status,
            RedirectAttributes redirectAttributes) {
        try {
            agendamentoService.atualizarStatus(id, status);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Status da consulta atualizado.");
        } catch (RegraNegocioException ex) {
            redirectAttributes.addFlashAttribute("mensagemErro", ex.getMessage());
        }
        return "redirect:/consultas";
    }

    @ModelAttribute("pacienteForm")
    public PacienteForm pacienteForm() {
        return new PacienteForm();
    }

    @ModelAttribute("medicoForm")
    public MedicoForm medicoForm() {
        return new MedicoForm();
    }

    @ModelAttribute("consultaForm")
    public ConsultaForm consultaForm() {
        return new ConsultaForm();
    }

    private void popularModelo(Model model, String paginaAtiva) {
        List<Paciente> pacientes = agendamentoService.listarPacientes();
        List<Medico> medicos = agendamentoService.listarMedicos();
        List<Consulta> consultas = agendamentoService.listarConsultas();

        model.addAttribute("pacientes", pacientes);
        model.addAttribute("medicos", medicos);
        model.addAttribute("consultas", consultas);
        model.addAttribute("consultasRecentes", consultas.stream().limit(5).toList());
        model.addAttribute("statusConsulta", StatusConsulta.values());
        model.addAttribute("paginaAtiva", paginaAtiva);
    }
}
