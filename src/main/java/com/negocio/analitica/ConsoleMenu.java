package com.negocio.analitica;

import com.negocio.analitica.controller.*;
import com.negocio.analitica.model.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Interfaz de consola para captura manual e importación CSV.
 * El menú delega las reglas de negocio en los controladores existentes.
 */
public final class ConsoleMenu {
    private final Scanner scanner;
    private final CategoryController categories;
    private final ProductController products;
    private final ClienteController clientes;
    private final VentaController ventas;
    private final AnaliticaController analitica;

    public ConsoleMenu(Scanner scanner, CategoryController categories, ProductController products,
                       ClienteController clientes, VentaController ventas, AnaliticaController analitica) {
        this.scanner = scanner;
        this.categories = categories;
        this.products = products;
        this.clientes = clientes;
        this.ventas = ventas;
        this.analitica = analitica;
    }

    public void iniciar() {
        boolean continuar = true;
        while (continuar) {
            System.out.println("\n========== AVNBR | MENÚ PRINCIPAL ==========");
            System.out.println("1. Registrar categoría");
            System.out.println("2. Registrar producto");
            System.out.println("3. Registrar cliente");
            System.out.println("4. Registrar venta");
            System.out.println("5. Consultar información almacenada");
            System.out.println("6. Ver indicadores de ventas");
            System.out.println("7. Importar clientes desde archivo CSV");
            System.out.println("0. Salir");
            int opcion = leerEntero("Seleccione una opción: ");
            try {
                switch (opcion) {
                    case 1 -> registrarCategoria();
                    case 2 -> registrarProducto();
                    case 3 -> registrarCliente();
                    case 4 -> registrarVenta();
                    case 5 -> consultarDatos();
                    case 6 -> mostrarIndicadores();
                    case 7 -> importarClientesCsv();
                    case 0 -> { continuar = false; System.out.println("Sesión finalizada. Los datos permanecen guardados."); }
                    default -> System.out.println("Opción no válida.");
                }
            } catch (RuntimeException ex) {
                System.out.println("No fue posible completar la operación: " + ex.getMessage());
            }
        }
    }

    private void registrarCategoria() {
        String nombre = leerTexto("Nombre de la categoría: ");
        String descripcion = leerTexto("Descripción: ");
        Category creada = categories.createCategory(new Category(null, nombre, descripcion));
        System.out.println("Categoría guardada con ID " + creada.getId());
    }

    private void registrarProducto() {
        List<Category> lista = categories.getAllCategories();
        if (lista.isEmpty()) { System.out.println("Primero debe registrar una categoría."); return; }
        System.out.println("\nCategorías disponibles:");
        lista.forEach(c -> System.out.printf("%d - %s%n", c.getId(), c.getName()));
        long categoryId = leerLong("ID de categoría: ");
        Category category = categories.getCategory(categoryId);
        String nombre = leerTexto("Nombre del producto: ");
        double precio = leerDouble("Precio: ");
        int stock = leerEntero("Existencias iniciales: ");
        int minimo = leerEntero("Stock mínimo: ");
        Product creado = products.createProduct(new Product(null, nombre, precio, stock, minimo, category));
        System.out.println("Producto guardado con ID " + creado.getId());
    }

    private void registrarCliente() {
        Cliente cliente = new Cliente(null, leerTexto("Nombre: "), leerTexto("Documento: "),
                leerTexto("Teléfono: "), leerTexto("Dirección: "));
        Cliente creado = clientes.crear(cliente);
        System.out.println("Cliente guardado con ID " + creado.getId());
    }

    private void registrarVenta() {
        List<Cliente> listaClientes = clientes.listar();
        List<Product> listaProductos = products.getAllProducts();
        if (listaClientes.isEmpty() || listaProductos.isEmpty()) {
            System.out.println("Se necesita al menos un cliente y un producto registrado."); return;
        }
        System.out.println("\nClientes:");
        listaClientes.forEach(c -> System.out.printf("%d - %s (%s)%n", c.getId(), c.getNombre(), c.getDocumento()));
        long clienteId = leerLong("ID del cliente: ");
        System.out.println("\nProductos:");
        listaProductos.forEach(p -> System.out.printf("%d - %s | $%.2f | stock: %d%n",
                p.getId(), p.getName(), p.getPrice(), p.getStock()));
        List<DetalleVenta> detalles = new ArrayList<>();
        boolean agregar = true;
        while (agregar) {
            long productId = leerLong("ID del producto: ");
            int cantidad = leerEntero("Cantidad: ");
            detalles.add(new DetalleVenta(null, productId, null, null, cantidad, 0));
            agregar = leerTexto("¿Agregar otro producto? (s/n): ").equalsIgnoreCase("s");
        }
        String metodo = leerTexto("Método de pago: ");
        Venta registrada = ventas.registrar(new Venta(null, null, clienteId, null, metodo, detalles));
        System.out.printf("Venta #%d registrada. Total: $%.2f%n", registrada.getId(), registrada.getTotal());
    }

    private void consultarDatos() {
        System.out.println("\n--- CATEGORÍAS ---");
        categories.getAllCategories().forEach(c -> System.out.printf("%d | %s | %s%n", c.getId(), c.getName(), c.getDescription()));
        System.out.println("\n--- PRODUCTOS ---");
        products.getAllProducts().forEach(p -> System.out.printf("%d | %s | $%.2f | stock %d | mínimo %d%n",
                p.getId(), p.getName(), p.getPrice(), p.getStock(), p.getMinimumStock()));
        System.out.println("\n--- CLIENTES ---");
        clientes.listar().forEach(c -> System.out.printf("%d | %s | documento %s | %s%n",
                c.getId(), c.getNombre(), c.getDocumento(), c.getTelefono()));
        System.out.println("\n--- VENTAS ---");
        ventas.listar().forEach(v -> System.out.printf("%d | %s | %s | $%.2f%n",
                v.getId(), v.getFecha(), v.getClienteNombre(), v.getTotal()));
    }

    private void mostrarIndicadores() {
        LocalDate desde = LocalDate.parse(leerTexto("Fecha inicial (AAAA-MM-DD): "));
        LocalDate hasta = LocalDate.parse(leerTexto("Fecha final (AAAA-MM-DD): "));
        if (hasta.isBefore(desde)) throw new IllegalArgumentException("La fecha final no puede ser anterior a la inicial.");
        System.out.printf("Ingresos: $%.2f%n", analitica.totalVentas(desde, hasta));
        System.out.println("Transacciones: " + analitica.cantidadTransacciones(desde, hasta));
        System.out.println("Unidades vendidas: " + analitica.unidadesVendidas(desde, hasta));
        System.out.printf("Ticket promedio: $%.2f%n", analitica.ticketPromedio(desde, hasta));
        System.out.println("Producto más vendido: " + analitica.productoMasVendido(desde, hasta).orElse("Sin ventas"));
        System.out.println("Productos con stock bajo:");
        analitica.productosConStockBajo().forEach(p ->
                System.out.printf("- %s: %d unidades (mínimo %d)%n", p.getName(), p.getStock(), p.getMinimumStock()));
    }

    /**
     * CSV esperado: nombre,documento,telefono,direccion
     * La primera fila se considera encabezado. Use UTF-8 y campos sin comas internas.
     */
    private void importarClientesCsv() {
        String ruta = leerTexto("Ruta del archivo CSV: ");
        try {
            List<String> lineas = Files.readAllLines(Path.of(ruta));
            int importados = 0;
            int rechazados = 0;
            for (int i = 1; i < lineas.size(); i++) {
                if (lineas.get(i).isBlank()) continue;
                String[] campos = lineas.get(i).split(",", -1);
                if (campos.length != 4) { rechazados++; continue; }
                try {
                    clientes.crear(new Cliente(null, campos[0].trim(), campos[1].trim(),
                            campos[2].trim(), campos[3].trim()));
                    importados++;
                } catch (RuntimeException ex) { rechazados++; }
            }
            System.out.printf("Importación terminada. Guardados: %d | Rechazados: %d%n", importados, rechazados);
        } catch (java.io.IOException ex) {
            throw new IllegalArgumentException("No se pudo leer el archivo: " + ex.getMessage(), ex);
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        String valor = scanner.nextLine().trim();
        if (valor.isEmpty()) throw new IllegalArgumentException("El campo no puede estar vacío.");
        return valor;
    }
    private int leerEntero(String mensaje) {
        try { return Integer.parseInt(leerTexto(mensaje)); }
        catch (NumberFormatException ex) { throw new IllegalArgumentException("Ingrese un número entero válido."); }
    }
    private long leerLong(String mensaje) {
        try { return Long.parseLong(leerTexto(mensaje)); }
        catch (NumberFormatException ex) { throw new IllegalArgumentException("Ingrese un ID numérico válido."); }
    }
    private double leerDouble(String mensaje) {
        try { return Double.parseDouble(leerTexto(mensaje).replace(",", ".")); }
        catch (NumberFormatException ex) { throw new IllegalArgumentException("Ingrese un valor numérico válido."); }
    }
}
