package parcial1.java;

import parcial1.java.dao.*;
import parcial1.java.model.*;
import parcial1.java.util.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class App {
    private static Scanner scanner;
    private static final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private static final PedidoDAO pedidoDAO = new PedidoDAOImpl();

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        try {
            crearTablasSiNoExisten();
            int opcion;
            do {
                mostrarMenu();
                opcion = leerEntero("Seleccione una opción: ");
                switch (opcion) {
                    case 1: crearCliente(); break;
                    case 2: listarClientes(); break;
                    case 3: buscarClientePorId(); break;
                    case 4: actualizarCliente(); break;
                    case 5: eliminarCliente(); break;
                    case 6: crearPedido(); break;
                    case 7: listarPedidos(); break;
                    case 8: listarPedidosPorCliente(); break;
                    case 9: actualizarPedido(); break;
                    case 10: eliminarPedido(); break;
                    case 0: System.out.println("Saliendo..."); break;
                    default: System.out.println("Opción inválida");
                }
            } while (opcion != 0);
        } catch (Exception e) {
            System.err.println("Error en la aplicación: " + e.getMessage());
        } finally {
            if (scanner != null) {
                try { scanner.close(); } catch (Exception ignored) {}
            }
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n--- Menú ---");
        System.out.println("1. Crear cliente");
        System.out.println("2. Listar clientes");
        System.out.println("3. Buscar cliente por ID");
        System.out.println("4. Actualizar cliente");
        System.out.println("5. Eliminar cliente");
        System.out.println("6. Crear pedido");
        System.out.println("7. Listar pedidos");
        System.out.println("8. Listar pedidos por cliente");
        System.out.println("9. Actualizar pedido");
        System.out.println("10. Eliminar pedido");
        System.out.println("0. Salir");
    }

    private static void crearCliente() {
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        if (Validador.esVacio(nombre)) { System.out.println("Nombre no puede estar vacío"); return; }
        System.out.print("Email: ");
        String email = scanner.nextLine();
        if (Validador.esVacio(email)) { System.out.println("Email no puede estar vacío"); return; }
        Cliente cliente = new Cliente(0, nombre, email, LocalDate.now());
        clienteDAO.crear(cliente);
        System.out.println("Cliente creado: " + cliente);
    }

    private static void listarClientes() {
        List<Cliente> clientes = clienteDAO.listarTodos();
        clientes.forEach(System.out::println);
    }

    private static void buscarClientePorId() {
        int id = leerEntero("ID de cliente: ");
        Cliente cliente = clienteDAO.buscarPorId(id);
        if (cliente != null) System.out.println(cliente);
        else System.out.println("No encontrado");
    }

    private static void actualizarCliente() {
        int id = leerEntero("ID de cliente a actualizar: ");
        Cliente cliente = clienteDAO.buscarPorId(id);
        if (cliente == null) { System.out.println("No encontrado"); return; }
        System.out.print("Nuevo nombre (actual: " + cliente.getNombre() + "): ");
        String nombre = scanner.nextLine();
        if (!Validador.esVacio(nombre)) cliente.setNombre(nombre);
        System.out.print("Nuevo email (actual: " + cliente.getEmail() + "): ");
        String email = scanner.nextLine();
        if (!Validador.esVacio(email)) cliente.setEmail(email);
        clienteDAO.actualizar(cliente);
        System.out.println("Actualizado: " + cliente);
    }

    private static void eliminarCliente() {
        int id = leerEntero("ID de cliente a eliminar: ");
        boolean eliminado = clienteDAO.eliminar(id);
        if (eliminado) {
            System.out.println("Cliente eliminado");
        } else {
            System.out.println("No se encontró un cliente con ese ID");
        }
    }

    private static void crearPedido() {
        int clienteId = leerEntero("ID del cliente: ");
        Cliente cliente = clienteDAO.buscarPorId(clienteId);
        if (cliente == null) { System.out.println("Cliente no existe"); return; }
        System.out.print("Monto: ");
        double monto = Double.parseDouble(scanner.nextLine());
        Pedido pedido = new Pedido(0, LocalDate.now(), monto, clienteId);
        pedidoDAO.crear(pedido);
        System.out.println("Pedido creado: " + pedido);
    }

    private static void listarPedidos() {
        List<Pedido> pedidos = pedidoDAO.listarTodos();
        pedidos.forEach(System.out::println);
    }

    private static void listarPedidosPorCliente() {
        int clienteId = leerEntero("ID del cliente: ");
        List<Pedido> pedidos = pedidoDAO.listarPorCliente(clienteId);
        pedidos.forEach(System.out::println);
    }

    private static void actualizarPedido() {
        int id = leerEntero("ID de pedido a actualizar: ");
        Pedido pedido = pedidoDAO.buscarPorId(id);
        if (pedido == null) { System.out.println("No encontrado"); return; }
        System.out.print("Nuevo monto (actual: " + pedido.getMonto() + "): ");
        String montoStr = scanner.nextLine();
        if (!Validador.esVacio(montoStr)) pedido.setMonto(Double.parseDouble(montoStr));
        pedidoDAO.actualizar(pedido);
        System.out.println("Actualizado: " + pedido);
    }

    private static void eliminarPedido() {
        int id = leerEntero("ID de pedido a eliminar: ");
        boolean eliminado = pedidoDAO.eliminar(id);
        if (eliminado) {
            System.out.println("Pedido eliminado");
        } else {
            System.out.println("No se encontró un pedido con ese ID");
        }
    }

    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (true) {
            try {
                if (!scanner.hasNextLine()) {
                    throw new IllegalStateException("No hay entrada disponible en consola.");
                }
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Ingrese un número válido: ");
            }
        }
    }

    private static void crearTablasSiNoExisten() {
        String sqlClientes = "CREATE TABLE IF NOT EXISTS clientes (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "nombre VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) NOT NULL, " +
                "fecha_registro DATE NOT NULL)";
        String sqlPedidos = "CREATE TABLE IF NOT EXISTS pedidos (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "fecha DATE NOT NULL, " +
                "monto DOUBLE NOT NULL, " +
                "cliente_id INT NOT NULL, " +
                "FOREIGN KEY (cliente_id) REFERENCES clientes(id) ON DELETE CASCADE)";
        try (java.sql.Connection conn = DatabaseUtil.getConnection();
             java.sql.Statement stmt = conn.createStatement()) {
            stmt.execute(sqlClientes);
            stmt.execute(sqlPedidos);
        } catch (Exception e) {
            System.err.println("Error creando tablas: " + e.getMessage());
        }
    }
}
