package com.novabank.repository.JDBC;

import com.novabank.config.DatabaseConnection;
import com.novabank.model.Movimiento;
import com.novabank.model.TipoMovimiento;
import com.novabank.repository.interfaz.MovimientoRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovimientoRepositoryJDBC implements MovimientoRepository {

    private final Connection connection;

    public MovimientoRepositoryJDBC() {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public Movimiento guardar(Long cuentaId, Movimiento movimiento) {
        String sql = "INSERT INTO movimientos (cuenta_id, tipo, importe, fecha) VALUES (?, ?, ?, ?) RETURNING id";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, cuentaId);
            stmt.setString(2, movimiento.getTipo().name());
            stmt.setDouble(3, movimiento.getImporte());
            stmt.setTimestamp(4, Timestamp.valueOf(movimiento.getFecha()));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                movimiento.setId(rs.getLong("id"));
                movimiento.setCuentaId(cuentaId);
            }

            return movimiento;

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar movimiento", e);
        }
    }

    @Override
    public List<Movimiento> buscarPorCuentaId(Long cuentaId) {
        String sql = "SELECT * FROM movimientos WHERE cuenta_id = ? ORDER BY fecha DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, cuentaId);
            ResultSet rs = stmt.executeQuery();

            List<Movimiento> movimientos = new ArrayList<>();

            while (rs.next()) {
                movimientos.add(mapRowToMovimiento(rs));
            }

            return movimientos;

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar movimientos por cuenta", e);
        }
    }

    @Override
    public List<Movimiento> buscarPorCuentaIdYRangoFechas(Long cuentaId, LocalDateTime inicio, LocalDateTime fin) {
        String sql = "SELECT * FROM movimientos WHERE cuenta_id = ? AND fecha BETWEEN ? AND ? ORDER BY fecha DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, cuentaId);
            stmt.setTimestamp(2, Timestamp.valueOf(inicio));
            stmt.setTimestamp(3, Timestamp.valueOf(fin));

            ResultSet rs = stmt.executeQuery();

            List<Movimiento> movimientos = new ArrayList<>();

            while (rs.next()) {
                movimientos.add(mapRowToMovimiento(rs));
            }

            return movimientos;

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar movimientos por rango de fechas", e);
        }
    }

    @Override
    public List<Movimiento> buscarTodos() {
        String sql = "SELECT * FROM movimientos ORDER BY fecha DESC";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            List<Movimiento> movimientos = new ArrayList<>();

            while (rs.next()) {
                movimientos.add(mapRowToMovimiento(rs));
            }

            return movimientos;

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar movimientos", e);
        }
    }

    private Movimiento mapRowToMovimiento(ResultSet rs) throws SQLException {

        Long id = rs.getLong("id");
        Long cuentaId = rs.getLong("cuenta_id");
        TipoMovimiento tipo = TipoMovimiento.valueOf(rs.getString("tipo"));
        double importe = rs.getDouble("importe");
        LocalDateTime fecha = rs.getTimestamp("fecha").toLocalDateTime();

        return new Movimiento(id, cuentaId, tipo, importe, fecha);
    }
}