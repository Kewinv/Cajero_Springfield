package com.fortbank.ATM.services;

import com.fortbank.ATM.entity.Cliente;
import com.fortbank.ATM.entity.Cuenta;
import com.fortbank.ATM.repository.ClienteRepository;
import com.fortbank.ATM.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetiroService {
    private final CuentaRepository cuentaRepository;
    private final ClienteRepository clienteRepository;
    private final MovimientoService movimientoService;

    public String realizarRetiro(String identificacion, String numeroCuenta, double monto){
        Cliente cliente = clienteRepository.findByIdentificacion(identificacion)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Cuenta cuenta = cuentaRepository.findByNumero(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));

        if(!cuenta.getCliente().equals(cliente)){
            throw new RuntimeException("La cuenta no pertenece al cliente");
        }

        if(cliente.isBloqueado()){
            throw new RuntimeException("El cliente o la cuenta se ha bloqueado");
        }

        boolean exito = movimientoService.realizarRetiro(cuenta, monto);
        if(!exito){
            throw new RuntimeException("No se pudo realizar el retiro");
        }

        return "redirect:cajero/menu?mensaje=Retiro realizado con éxito";
    }
}
