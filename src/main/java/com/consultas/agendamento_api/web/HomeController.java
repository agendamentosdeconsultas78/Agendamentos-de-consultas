package com.consultas.agendamento_api.web;

import com.consultas.agendamento_api.model.StatusConsulta;
import com.consultas.agendamento_api.service.AgendamentoService;
import com.consultas.agendamento_api.service.RegraNegocioException;
import com.consultas.agendamento_api.web.form.ConsultaForm;
import com.consultas.agendamento_api.web.form.MedicoForm;
import com.consultas.agendamento_api.web.form.PacienteForm;
import jakarta.validation.Valid;
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
    public String home(Model model) {
        popularModelo(model);
        return "index";
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
            popularModelo(model);
            return "index";
        }

        try {
            agendamentoService.cadastrarPaciente(pacienteForm);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Paciente cadastrado com sucesso.");
            return "redirect:/";
        } catch (RegraNegocioException ex) {
            model.addAttribute("mensagemErro", ex.getMessage());
            popularModelo(model);
            return "index";
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
            popularModelo(model);
            return "index";
        }

        try {
            agendamentoService.cadastrarMedico(medicoForm);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Medico cadastrado com sucesso.");
            return "redirect:/";
        } catch (RegraNegocioException ex) {
            model.addAttribute("mensagemErro", ex.getMessage());
            popularModelo(model);
            return "index";
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
            popularModelo(model);
            return "index";
        }

        try {
            agendamentoService.agendarConsulta(consultaForm);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Consulta agendada com sucesso.");
            return "redirect:/";
        } catch (RegraNegocioException ex) {
            model.addAttribute("mensagemErro", ex.getMessage());
            popularModelo(model);
            return "index";
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
        return "redirect:/";
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

    private void popularModelo(Model model) {
        model.addAttribute("pacientes", agendamentoService.listarPacientes());
        model.addAttribute("medicos", agendamentoService.listarMedicos());
        model.addAttribute("consultas", agendamentoService.listarConsultas());
        model.addAttribute("statusConsulta", StatusConsulta.values());
    }
}
