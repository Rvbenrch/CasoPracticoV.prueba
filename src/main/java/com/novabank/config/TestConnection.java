package com.novabank.config;

import java.sql.Connection;
import java.sql.DriverManager;

public class TestConnection {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "655057621";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Conexión OK");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}