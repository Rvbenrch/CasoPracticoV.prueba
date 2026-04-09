package com.novabank.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/novaBank";
    private static final String USER = "postgres";
    private static final String PASSWORD = "655057621";

    private DatabaseConnection() {
        // Evitar instanciación
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("Error al conectar con la base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo establecer la conexión con la base de datos", e);
        }
    }
}