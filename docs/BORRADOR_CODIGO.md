# BORRADOR DE ESTRUCTURA DEL CODIGO

> Este archivo es una guia inicial. No representa todavia la implementacion final del sistema.
> Cada bloque indica a que parte del proyecto corresponde y quien tiene la responsabilidad principal.

## 1. MODEL - Entidad Producto
**Responsable:** Desarrollador 1

```java
package com.negocio.analitica.model;

// [D1] MODEL
// Representa los datos de un producto.
// No debe contener consultas SQL ni codigo de interfaz.

public class Producto {

    private int id;
    private String nombre;
    private double precio;
    private int stock;

    public Producto(int id, String nombre, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }
}
```

---

## 2. REPOSITORY - Acceso a Producto
**Responsable:** Desarrollador 1

```java
package com.negocio.analitica.persistence;

import com.negocio.analitica.model.Producto;

// [D1] REPOSITORY
// Su responsabilidad es comunicarse con la persistencia.
// En la implementacion real aqui estaria la conexion/consulta a la BD.

public class ProductoRepository {

    public Producto buscarPorId(int id) {
        // BORRADOR: aqui iria la consulta a la base de datos.
        return null;
    }

    public void guardar(Producto producto) {
        // BORRADOR: aqui iria INSERT/UPDATE.
    }
}
```

---

## 3. SERVICE - Regla de negocio
**Responsable:** Desarrollador 1

```java
package com.negocio.analitica.service;

import com.negocio.analitica.model.Producto;
import com.negocio.analitica.persistence.ProductoRepository;

// [D1] SERVICE
// Contiene reglas de negocio.
// No deberia encargarse de mostrar menus ni ejecutar SQL directamente.

public class ProductoService {

    private final ProductoRepository repository;

    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public void registrarProducto(Producto producto) {

        // Ejemplo de validacion de negocio.
        if (producto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor que cero.");
        }

        repository.guardar(producto);
    }
}
```

---

## 4. CONTROLLER - Coordinacion
**Responsable:** estructura definida por el Líder; implementacion segun modulo

```java
package com.negocio.analitica.controller;

import com.negocio.analitica.model.Producto;
import com.negocio.analitica.service.ProductoService;

// [LIDER / INTEGRACION] CONTROLLER
// Recibe una accion de la interfaz y delega al Service.
// No debe ejecutar SQL ni contener toda la regla de negocio.

public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    public void registrar(Producto producto) {
        // El Controller delega la operacion al Service.
        service.registrarProducto(producto);
    }
}
```

---

## 5. UI - Interfaz
**Responsable:** integracion general; cada funcionalidad se conecta con su Controller

```java
package com.negocio.analitica.ui;

import com.negocio.analitica.controller.ProductoController;
import com.negocio.analitica.model.Producto;

// [INTEGRACION] UI
// Recibe datos del usuario.
// No debe acceder directamente al Repository o a la base de datos.

public class Main {

    public static void main(String[] args) {

        // BORRADOR:
        // En la aplicacion final estos datos vendrian de la interfaz.
        Producto producto =
                new Producto(1, "Producto de prueba", 10000, 10);

        // BORRADOR:
        // Aqui se conectaria la UI con el Controller correspondiente.
        ProductoController controller = null;

        // controller.registrar(producto);
    }
}
```

---

## 6. VENTA - Integracion D2 con Producto D1
**Responsable:** Desarrollador 2, respetando el contrato del Líder

```java
// [D2] VENTA
// Una venta necesita identificar el producto existente.
// D2 NO debe duplicar la estructura ni el CRUD de Producto de D1.

public class DetalleVenta {

    private int productoId;
    private int cantidad;
    private double precioUnitario;

    public DetalleVenta(int productoId, int cantidad, double precioUnitario) {
        this.productoId = productoId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }
}
```

---

## 7. INVENTARIO - Integracion con Venta
**Responsable:** Desarrollador 1

```java
// [D1] INVENTARIO
// El inventario controla la disponibilidad.
// VentaService debe solicitar esta operacion,
// no copiar la logica de stock.

public class Inventario {

    public boolean hayDisponibilidad(int stockActual, int cantidadSolicitada) {
        return cantidadSolicitada > 0
                && stockActual >= cantidadSolicitada;
    }
}
```

---

## 8. ANALITICA
**Responsable:** Desarrollador 2, validacion del Líder

```java
// [D2] ANALITICA
// Ejemplo basico de un indicador.
// La implementacion final debe utilizar datos persistidos.

public class AnaliticaService {

    public double calcularTicketPromedio(double totalVentas, int cantidadVentas) {

        if (cantidadVentas == 0) {
            return 0;
        }

        return totalVentas / cantidadVentas;
    }
}
```

---

## 9. EJEMPLO DEL FLUJO COMPLETO

```text
[USUARIO]
    |
    v
[UI / Main]
    |
    v
[Controller]
    |
    v
[Service]
    |
    v
[Repository]
    |
    v
[BASE DE DATOS]
```

// [LIDER] Este flujo es una regla arquitectonica.
// Evita que UI acceda directamente a la base de datos.

---

## 10. REGLAS PARA EL BORRADOR

// [LIDER] No mezclar responsabilidades.
// [D1] Producto, Categoria e Inventario.
// [D2] Cliente, Venta, DetalleVenta y Analitica.
// [LIDER] Arquitectura, contratos, integracion, pruebas y documentacion.
//
// IMPORTANTE:
// Este codigo es solamente una base de referencia.
// Antes de integrarlo al proyecto definitivo debe adaptarse
// a las clases, base de datos y decisiones que tome el equipo.
