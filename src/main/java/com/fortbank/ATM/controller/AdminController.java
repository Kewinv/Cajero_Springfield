package com.fortbank.ATM.controller;

import com.fortbank.ATM.entity.Cliente;
import com.fortbank.ATM.entity.Cuenta;
import com.fortbank.ATM.entity.TipoCuenta;
import com.fortbank.ATM.services.ClienteService;
import com.fortbank.ATM.services.CuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping("/crear-cuenta")
    public String mostrarFormularioCuenta(Model model){
        model.addAttribute("cuenta", new Cuenta());
        return "admin/crear-cuenta";
    }

    @PostMapping("/crear-cuenta")
    public String crearCuenta(@RequestParam String identificacion,
                              @RequestParam String numero,
                              @RequestParam TipoCuenta tipo,
                              @RequestParam double saldo){
        Cliente cliente = clienteService.buscarPorIdentificacion(identificacion).orElseThrow();
        cuentaService.creaCuenta(cliente,numero,tipo,saldo);
        return "redirect:/admin";
    }

    @GetMapping("(desbloquear")
    public String mostrarDesbloqueo(){
        return "admin/desbloquear";
    }

    @PostMapping("(desbloquear")
    public String mostrarDesbloqueo(@RequestParam String identificacion,
                                    @RequestParam String nuevoPin){

        clienteService.desbloquarCliente(identificacion, nuevoPin);
        return "redirect:/admin";
    }
}
