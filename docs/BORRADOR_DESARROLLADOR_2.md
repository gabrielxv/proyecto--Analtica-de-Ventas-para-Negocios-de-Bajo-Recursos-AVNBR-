# BORRADOR DE DESARROLLO — DESARROLLADOR 2

> Documento de trabajo independiente para que el Desarrollador 2 organice y modifique su módulo.
> Este borrador propone una base, pero NO obliga a conservar nombres, atributos, métodos o implementaciones.
> Los cambios deben continuar respetando el contrato de integración del proyecto.

## Responsabilidad principal

Según el requerimiento del proyecto, el Desarrollador 2 trabaja principalmente en:

- Clientes.
- Ventas.
- Detalles de venta.
- Analítica.

Clases y componentes inicialmente previstos:

- Cliente
- Venta
- DetalleVenta
- ClienteRepository
- VentaRepository
- ClienteService
- VentaService
- AnaliticaService

El desarrollador puede reorganizar esta estructura si encuentra una solución técnicamente más adecuada, siempre que se mantengan claras las responsabilidades y se coordine cualquier cambio que afecte a otros módulos.

---

# 1. MODEL

## 1.1 Cliente

**[D2 — MODEL — BORRADOR]**

Propósito:
- Representar a los clientes registrados.
- Mantener los datos propios del cliente.

Base sugerida:

```java
package com.negocio.analitica.model;

public class Cliente {

    private Long id;
    private String nombre;
    private String documento;
    private String telefono;
    private String direccion;

    // TODO D2:
    // Revisar atributos.
    // Definir campos obligatorios.
    // Definir constructores.
    // Definir estrategia de encapsulamiento.
}
```

### Espacio para decisiones del desarrollador

- Atributos definitivos: ______________________________
- Documento obligatorio: ______________________________
- Campos adicionales: _________________________________
- Validaciones: _______________________________________

---

## 1.2 Venta

**[D2 — MODEL — BORRADOR]**

Propósito:
- Representar una venta.
- Relacionar la venta con un cliente.
- Mantener fecha, total y demás datos definidos por el proyecto.

Base orientativa:

```java
package com.negocio.analitica.model;

import java.time.LocalDateTime;
import java.util.List;

public class Venta {

    private Long id;
    private LocalDateTime fecha;
    private double total;
    private Cliente cliente;
    private List<DetalleVenta> detalles;

    // TODO D2:
    // Revisar atributos.
    // Definir relación con Cliente.
    // Definir manejo de detalles.
    // Definir cálculo del total.
}
```

**El desarrollador puede cambiar la estructura según la solución que implemente.**

---

## 1.3 DetalleVenta

**[D2 — MODEL — BORRADOR]**

Propósito:
- Representar cada producto incluido dentro de una venta.
- Identificar el producto utilizado.
- Registrar cantidad y precio aplicado.

Base orientativa:

```java
package com.negocio.analitica.model;

public class DetalleVenta {

    private Long id;
    private Long productoId;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;

    // TODO D2:
    // Definir si se almacena productoId o una referencia a Producto.
    // Definir cálculo del subtotal.
    // Definir validaciones.
}
```

**Importante:** Producto pertenece al módulo D1. D2 no debe crear una segunda clase Producto.

---

# 2. REPOSITORY / PERSISTENCIA

## 2.1 ClienteRepository

**[D2 — REPOSITORY — BORRADOR]**

Responsabilidad:
- Persistir y consultar clientes.

```java
package com.negocio.analitica.persistence;

public class ClienteRepository {

    // TODO D2:
    // Definir operaciones CRUD.
    // Definir búsquedas necesarias.
    // Definir persistencia.
}
```

Operaciones por definir:
- guardar(...)
- buscarPorId(...)
- listar(...)
- actualizar(...)
- eliminar(...)
- buscarPorDocumento(...)

El desarrollador puede modificar esta propuesta.

---

## 2.2 VentaRepository

**[D2 — REPOSITORY — BORRADOR]**

Responsabilidad:
- Persistir ventas y sus detalles.
- Permitir consultas necesarias para la analítica.

```java
package com.negocio.analitica.persistence;

public class VentaRepository {

    // TODO D2:
    // Definir cómo se guarda una venta.
    // Definir cómo se guardan sus detalles.
    // Definir consultas por fecha o período.
}
```

Operaciones a considerar:
- guardar(...)
- buscarPorId(...)
- listar(...)
- buscarPorFecha(...)
- buscarPorPeriodo(...)

---

# 3. SERVICE

## 3.1 ClienteService

**[D2 — SERVICE — BORRADOR]**

Responsabilidad:
- Aplicar reglas de negocio de clientes.
- Validar información.
- Coordinar ClienteRepository.

```java
package com.negocio.analitica.service;

public class ClienteService {

    // TODO D2:
    // Definir reglas de validación.
    // Definir operaciones CRUD.
    // Definir búsquedas.
}
```

---

## 3.2 VentaService

**[D2 — SERVICE — BORRADOR]**

Responsabilidad:
- Registrar ventas.
- Validar los detalles.
- Calcular totales.
- Coordinar la interacción con el módulo de Producto/Inventario de D1.
- Persistir la venta.

Base orientativa:

```java
package com.negocio.analitica.service;

public class VentaService {

    // TODO D2:
    // Definir flujo definitivo para registrar una venta.
    // Consultar disponibilidad mediante el contrato con D1.
    // Evitar duplicar la lógica de inventario.
    // Guardar la venta después de superar las validaciones.
}
```

### Flujo conceptual

```text
VentaService
    |
    |-- validar cliente
    |
    |-- validar detalles
    |
    |-- consultar disponibilidad ------> D1 / Inventario
    |
    |-- registrar venta
    |
    |-- solicitar actualización -------> D1 / Inventario
    |
    v
VentaRepository
```

La implementación exacta queda a decisión de D2 y debe coordinarse con D1.

---

# 4. ANALÍTICA

## AnaliticaService

**[D2 — SERVICE — BORRADOR]**

Responsabilidad:
- Convertir los datos de ventas en indicadores útiles.

Indicadores contemplados en el requerimiento:

- Total de ventas por período.
- Cantidad de transacciones.
- Unidades vendidas.
- Ticket promedio.
- Producto más vendido por unidades.
- Ventas por categoría.
- Ventas por método de pago.
- Productos con bajo stock.
- Comparación entre períodos.

Base orientativa:

```java
package com.negocio.analitica.service;

public class AnaliticaService {

    // TODO D2:
    // Definir métodos para cada indicador.
    // Definir consultas necesarias.
    // Definir estructuras de respuesta.
    // Evitar mezclar presentación con lógica analítica.

    public double calcularTicketPromedio(
            double totalVentas,
            int cantidadVentas) {

        if (cantidadVentas == 0) {
            return 0;
        }

        return totalVentas / cantidadVentas;
    }
}
```

El ejemplo anterior es únicamente una referencia. D2 puede cambiar completamente la implementación.

---

# 5. CONTROLLER

## VentaController

**[D2 — CONTROLLER — BORRADOR]**

```java
package com.negocio.analitica.controller;

public class VentaController {

    // TODO D2:
    // Definir operaciones necesarias para registrar,
    // consultar y listar ventas.
}
```

---

## ClienteController

**[D2 — CONTROLLER — BORRADOR]**

```java
package com.negocio.analitica.controller;

public class ClienteController {

    // TODO D2:
    // Definir operaciones CRUD y consultas.
}
```

---

## AnaliticaController

**[D2 — CONTROLLER — BORRADOR]**

```java
package com.negocio.analitica.controller;

public class AnaliticaController {

    // TODO D2:
    // Recibir parámetros de consulta.
    // Delegar al AnaliticaService.
    // Entregar resultados a la interfaz.
}
```

---

# 6. INTEGRACIÓN CON DESARROLLADOR 1

Producto, Categoría e Inventario pertenecen a D1.

D2 puede consumir estos datos, pero no debe duplicar sus entidades ni sus reglas principales.

Para una venta:

1. D2 identifica el producto.
2. D2 solicita validación de disponibilidad al módulo de D1.
3. D2 registra la venta si las validaciones son correctas.
4. D2 solicita la actualización de existencias según el contrato acordado.
5. D1 mantiene la responsabilidad sobre la lógica de inventario.

**Si cambia la forma de comunicación entre ambos módulos, el cambio debe coordinarse antes de integrarlo.**

---

# 7. FLUJO PROPUESTO

```text
UI
 |
 v
VentaController
 |
 v
VentaService
 |
 +----> ClienteService / ClienteRepository
 |
 +----> Contrato Producto/Inventario de D1
 |
 v
VentaRepository
 |
 v
Base de datos
```

Analítica:

```text
UI
 |
 v
AnaliticaController
 |
 v
AnaliticaService
 |
 v
Consultas de ventas / persistencia
 |
 v
Indicadores
```

---

# 8. PENDIENTES DE D2

- [ ] Definir estructura definitiva de Cliente.
- [ ] Definir estructura definitiva de Venta.
- [ ] Definir estructura definitiva de DetalleVenta.
- [ ] Definir repositories.
- [ ] Definir services.
- [ ] Definir controllers.
- [ ] Definir validaciones.
- [ ] Definir flujo de registro de venta.
- [ ] Definir integración con D1.
- [ ] Implementar indicadores.
- [ ] Probar ventas de uno o varios productos.
- [ ] Probar consultas por período.
- [ ] Probar indicadores.
- [ ] Documentar decisiones importantes.

## Decisiones propias del desarrollador

> Espacio libre para que D2 documente cambios respecto a este borrador.

____________________________________________________________

____________________________________________________________

____________________________________________________________

