package com.novabank.repository.JDBC;

import com.novabank.config.DatabaseConnection;
import com.novabank.model.Cliente;
import com.novabank.model.Cuenta;
import com.novabank.repository.interfaz.CuentaRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CuentaRepositoryJDBC implements CuentaRepository {

    private final ClienteRepositoryJDBC clienteRepository;

    public CuentaRepositoryJDBC() {
        this.clienteRepository = new ClienteRepositoryJDBC();
    }

    @Override
    public Cuenta guardar(Cuenta cuenta) {
        String sql = "INSERT INTO cuentas (numero_cuenta, cliente_id, saldo, fecha_creacion) " +
                "VALUES (?, ?, ?, ?) RETURNING id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, cuenta.getNumeroCuenta());
            stmt.setLong(2, cuenta.getTitular().getId());
            stmt.setDouble(3, cuenta.getSaldo());
            stmt.setTimestamp(4, Timestamp.valueOf(cuenta.getFechaCreacion()));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cuenta.setId(rs.getLong("id"));
            }

            return cuenta;

        } catch (SQLException e) {
            throw new RuntimeException("Error al guardar cuenta", e);
        }
    }

    @Override
    public Optional<Cuenta> buscarPorNumeroCuenta(String numeroCuenta) {
        String sql = "SELECT * FROM cuentas WHERE numero_cuenta = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, numeroCuenta);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapRowToCuenta(rs));
            }

            return Optional.empty();

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cuenta por número de cuenta", e);
        }
    }

    @Override
    public List<Cuenta> listarCuentas() {
        String sql = "SELECT * FROM cuentas";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            List<Cuenta> cuentas = new ArrayList<>();

            while (rs.next()) {
                cuentas.add(mapRowToCuenta(rs));
            }

            return cuentas;

        } catch (SQLException e) {
            throw new RuntimeException("Error al listar cuentas", e);
        }
    }

    @Override
    public List<Cuenta> buscarPorClienteId(Long clienteId) {
        String sql = "SELECT * FROM cuentas WHERE cliente_id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setLong(1, clienteId);
            ResultSet rs = stmt.executeQuery();

            List<Cuenta> cuentas = new ArrayList<>();
            while (rs.next()) {
                cuentas.add(mapRowToCuenta(rs));
            }

            return cuentas;

        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar cuentas por cliente", e);
        }
    }

    private Cuenta mapRowToCuenta(ResultSet rs) throws SQLException {

        Long id = rs.getLong("id");
        String numeroCuenta = rs.getString("numero_cuenta");
        Long clienteId = rs.getLong("cliente_id");
        double saldo = rs.getDouble("saldo");
        LocalDateTime fechaCreacion = rs.getTimestamp("fecha_creacion").toLocalDateTime();

        Cliente cliente = clienteRepository.buscarPorId(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado para la cuenta"));



        return new Cuenta(
                id,
                cliente,
                numeroCuenta,
                saldo,
                fechaCreacion);
    }
}