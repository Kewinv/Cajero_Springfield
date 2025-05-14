package com.fortbank.ATM.controller;

import com.fortbank.ATM.entity.Cliente;
import com.fortbank.ATM.services.ClienteService;
import com.fortbank.ATM.services.CuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin") // "http" = protocolo de transferencia de hiper texto
public class AdminController {

    private final ClienteService clienteService;
    private final CuentaService cuentaService;

    @GetMapping // Creación de rutas, define lo que ira despues de "/admin"
    public String adminHome(){
        return "admin/index"; // Retorna la vista "admin/index.html"
    }

    @GetMapping("/crear-cliente")
    public String mostrarFormularioCliente(Model model){
        model.addAttribute("cliente", new Cliente());
        return "admin/crear-cliente";

    }

    @PostMapping("/crear-cliente")
    public String crearCliente(@ModelAttribute("cliente") Cliente cliente){
        clienteService.crearCliente(cliente);
        return "redirect:/admin"; // Redirige a la ruta "/admin"
    }

}
