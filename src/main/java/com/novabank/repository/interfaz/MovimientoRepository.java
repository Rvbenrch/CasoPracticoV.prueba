package com.novabank.repository.interfaz;

import com.novabank.model.Movimiento;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoRepository {

    Movimiento guardar(Long cuentaId, Movimiento movimiento);

    List<Movimiento> buscarPorCuentaId(Long cuentaId);

    List<Movimiento> buscarPorCuentaIdYRangoFechas(Long cuentaId, LocalDateTime inicio, LocalDateTime fin);

    List<Movimiento> buscarTodos();
}