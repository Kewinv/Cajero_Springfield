package com.fortbank.ATM.services;

import com.fortbank.ATM.entity.Cuenta;
import com.fortbank.ATM.entity.Movimiento;
import com.fortbank.ATM.entity.TipoMovimiento;
import com.fortbank.ATM.repository.CuentaRepository;
import com.fortbank.ATM.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoService {
    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;

    public Movimiento registrarMovimiento(Cuenta cuenta, TipoMovimiento tipo, double monto){
        Movimiento movimiento = Movimiento.builder()
                .tipo(tipo)
                .monto(monto)
                .fecha(LocalDateTime.now())
                .cuenta(cuenta)
                .build();
        return movimientoRepository.save(movimiento);
    }

    public List<Movimiento> obtenerMovimientoPorCuenta(Cuenta cuenta, double monto){
        return movimientoRepository.findByCuenta(cuenta);
    }

    public boolean realizarRetiro(Cuenta cuenta, double monto){
        if (cuenta.getSaldo() >= monto){
            cuenta.setSaldo(cuenta.getSaldo() - monto);
            cuentaRepository.save(cuenta);
            registrarMovimiento(cuenta, TipoMovimiento.RETIRO, monto);
            return true;
        }
        return false;
    }

    public boolean realizarTransferencia(Cuenta origen, Cuenta destino, double monto){
        if (origen.getSaldo() >= monto){
            origen.setSaldo(origen.getSaldo() - monto);
            destino.setSaldo(destino.getSaldo() + monto);
            cuentaRepository.save(origen);
            cuentaRepository.save(destino);

            registrarMovimiento(origen, TipoMovimiento.TRANSFERENCIA, -monto);
            registrarMovimiento(destino, TipoMovimiento.TRANSFERENCIA, monto);
            return true;
        }
        return false;
    }

    public List<Movimiento> buscarPorCuenta(String numeroCuenta){
        Cuenta cuenta = cuentaRepository.findByNumero(numeroCuenta)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
                return movimientoRepository.findByCuentaOrderByfechaDesc(cuenta);
    }

    public boolean realizarConsignacion(Cuenta cuenta, double monto){
        if(monto <= 0){
            throw new IllegalArgumentException("El monto debe ser mayor a cero");
        }
        cuenta.setSaldo(cuenta.getSaldo() + monto);
        cuentaRepository.save(cuenta);
        registrarMovimiento(cuenta, TipoMovimiento.CONSIGNACION, monto);
        return true;
    }

}
