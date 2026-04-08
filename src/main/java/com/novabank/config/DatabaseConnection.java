package com.novabank.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static Connection connection;

    private static final String URL = "jdbc:postgresql://localhost:5432/novabank";
    private static final String USER = "postgres";
    private static final String PASSWORD = "tu_password";

    private DatabaseConnection() {
        // Constructor privado para evitar instanciación
    }

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexión a PostgreSQL establecida correctamente.");
            } catch (SQLException e) {
                System.err.println("Error al conectar con la base de datos: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return connection;
    }
}