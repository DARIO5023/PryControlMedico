package com.medico.app.web.controllers;

import com.medico.app.web.models.entities.Paciente;
import com.medico.app.web.models.services.IPacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

import javax.validation.Valid;

@Controller
@RequestMapping(value="/paciente")
public class PacienteController {

    @Autowired
    //private IPacienteDAO service;
    private IPacienteService service;

    @GetMapping(value="/create" )
    public String create(Model model){
    	
        Paciente paciente=new Paciente();
        model.addAttribute("title","Agregar nuevo Paciente");
        model.addAttribute("paciente",paciente);
        return "paciente/form";
    }

    @PostMapping(value="/save" )
    public String save(@Valid Paciente paciente,BindingResult result,
    		Model model,    		 
    		RedirectAttributes message, SessionStatus session){
        try{
        	if(result.hasErrors()) {        		
        		return "paciente/form";
        	}    
        	String msg=paciente.getIdpersona()==null ? paciente.getNombre()+ " ha sido registrado" : paciente.getNombre()+" ha sido actualizado";
            service.save(paciente);
            model.addAttribute("title","Agregar nuevo Paciente");
            message.addFlashAttribute("success", msg);
        }catch (Exception ex){
            model.addAttribute("error: ",ex.toString());
        }
        return "redirect:/paciente/list";
    }

    @GetMapping(value="/retrieve/{id}" )
    public String retrieve(@PathVariable(value = "id") Integer id,
                           Model model){
        Paciente paciente=service.findById(id);
        model.addAttribute("paciente",paciente);
        return "paciente/card";
    }

    @GetMapping(value="/update/{id}" )
    public String update(@PathVariable(value = "id") Integer id,
                         Model model){
    	
        Paciente paciente=service.findById(id);
        model.addAttribute("paciente",paciente);
        model.addAttribute("title","Actualizar datos del Paciente");
        return "paciente/form";
    }

    @GetMapping(value="/delete/{id}" )
    public String delete(@PathVariable(value = "id") Integer id,
                         Model model,RedirectAttributes message){
        try{
            service.delete(id);
            message.addFlashAttribute("success", "Paciente eliminado correctamente");
        }catch (Exception ex){
            model.addAttribute("error: ",ex.toString());
        }
        return "redirect:/paciente/list";
    }

    @GetMapping(value="/list" )
    public String list(Model model){
        List<Paciente> pacientes=service.findAll();
        model.addAttribute("title","Listado de Pacientes");
        model.addAttribute("pacientes",pacientes);
        return "paciente/list";
    }
}