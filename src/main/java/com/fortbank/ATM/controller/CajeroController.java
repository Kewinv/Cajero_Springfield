package com.fortbank.ATM.controller;

import com.fortbank.ATM.entity.Cliente;
import com.fortbank.ATM.entity.Cuenta;
import com.fortbank.ATM.repository.CuentaRepository;
import com.fortbank.ATM.services.ClienteService;
import com.fortbank.ATM.services.CuentaService;
import com.fortbank.ATM.services.MovimientoService;
import com.fortbank.ATM.services.RetiroService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cajero")

public class CajeroController {
    private final ClienteService clienteService;
    private final CuentaService cuentaService;
    private final CuentaRepository cuentaRepository;
    private final MovimientoService movimientoService;
    private final RetiroService retiroService;

    @GetMapping
    public String loginForm(){
        return "cajero/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String numeroCuenta,
                        @RequestParam String pin, HttpSession session, Model model ){
        var cuenta = cuentaService.buscarPorNumero(numeroCuenta);
        if(cuenta.isEmpty()){
            model.addAttribute("error", "Cuenta no encontrada o no existente");
            return "cajero/login";

        }
        Cliente cliente = cuenta.get().getCliente();

        if(cliente.isBloqueado()){
            model.addAttribute("error", "El cliente ya se encuentra bloqueado");
            return "cajero/login";
        }

        if(cliente.getPin().equals(pin)){
            clienteService.incrementarIntento(cliente);
            if(cliente.getIntentos()>=3){
                clienteService.bloquearCliente(cliente);
                model.addAttribute("error", "Cuenta bloqueada por intentos fallidos");
            }else model.addAttribute("error", "Pin incorrecto");
            return "cajero/login";
        }
        clienteService.reiniciarIntentos(cliente);
        session.setAttribute("cliente", cliente);
        return "redirect:/cajero/menu";
    }

    @GetMapping("/menu")
    public String menu(HttpSession session, Model model){
        Cliente cliente = (Cliente) session.getAttribute("cliente");
        if(cliente == null){
            return "redirect:/cajero";

        }
        model.addAttribute("cliente", cliente);
        model.addAttribute("cuentas", cuentaService.buscarPorCliente(cliente));
        return "cajero/menu";
    }

}
