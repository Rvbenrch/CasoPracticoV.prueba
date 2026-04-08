package com.novabank;

import com.novabank.menus.MenuCliente;
import com.novabank.menus.MenuConsultas;
import com.novabank.menus.MenuCuentas;
import com.novabank.menus.MenuOperaciones;
import com.novabank.repository.ClienteRepositoryMemory;
import com.novabank.repository.CuentaRepositoryMemory;
import com.novabank.service.ClienteService;
import com.novabank.service.ConsultaService;
import com.novabank.service.CuentaService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // REPOSITORIOS
        ClienteRepositoryMemory clienteRepository = new ClienteRepositoryMemory();
        CuentaRepositoryMemory cuentaRepository = new CuentaRepositoryMemory();

        // SERVICIOS
        ClienteService clienteService = new ClienteService(clienteRepository);
        CuentaService cuentaService = new CuentaService(cuentaRepository, clienteService);
        ConsultaService consultaService = new ConsultaService(cuentaService);

        // MENÚS
        MenuCliente menuCliente = new MenuCliente(scanner, clienteService);
        MenuCuentas menuCuentas = new MenuCuentas(scanner, cuentaService, clienteService);
        MenuOperaciones menuOperaciones = new MenuOperaciones(scanner, cuentaService);
        MenuConsultas menuConsultas = new MenuConsultas(scanner, consultaService);

        int opcion;

        // MENU PRINCIPAL
        do {
            System.out.println("\n====================================");
            System.out.println("NOVABANK - SISTEMA DE OPERACIONES");
            System.out.println("====================================");
            System.out.println("1. Gestión de clientes");
            System.out.println("2. Gestión de cuentas");
            System.out.println("3. Operaciones financieras");
            System.out.println("4. Consultas");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        menuCliente.mostrar();
                        break;

                    case 2:
                        menuCuentas.mostrar();
                        break;

                    case 3:
                        menuOperaciones.mostrar();
                        break;

                    case 4:
                        menuConsultas.mostrar();
                        break;

                    case 5:
                        System.out.println("Saliendo del sistema...");
                        break;

                    default:
                        System.out.println("Opción inválida.");
                }

            } catch (NumberFormatException e) {
                System.out.println("ERROR: Debe introducir un número válido.");
                opcion = 0;
            }

        } while (opcion != 5);

        scanner.close();
    }
}