package com.fortbank.ATM.services;

import com.fortbank.ATM.entity.Cliente;
import com.fortbank.ATM.entity.Cuenta;
import com.fortbank.ATM.entity.TipoCuenta;
import com.fortbank.ATM.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor

public class CuentaService {
    private final CuentaRepository cuentaRepository;
    public Cuenta creaCuenta(Cliente cliente, String numero, TipoCuenta tipo, double saldoIcial){
        Cuenta cuenta = Cuenta.builder()
                .numero(numero)
                .tipo(tipo)
                .saldo(saldoIcial)
                .cliente(cliente)
                .build();
                return cuentaRepository.save(cuenta);
    }
    public Optional<Cuenta> buscarPorNumero(String numero){
        return cuentaRepository.findByNumero(numero);
    }

    public Double consultarSaldo(Cuenta cuenta){
        return cuenta.getSaldo();
    }

    public static List<Cuenta> obtenerCuentasCliente(Cliente cliente){
        return cliente.getCuentas();
    }

    public void actualizarSaldo(Cuenta cuenta, double nuevoSaldo){
        cuenta.setSaldo(nuevoSaldo);
        cuentaRepository.save(cuenta);
    }

    public List<Cuenta> buscarPorCliente(Cliente cliente){
        return cuentaRepository.findByCliente(cliente);
    }

    public Cuenta obtenerCuentaPorClienteActual(String username){
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
