# BORRADOR DE DESARROLLO — DESARROLLADOR 1

> Documento de trabajo independiente para que el Desarrollador 1 organice y modifique su módulo.
> Este borrador propone una base, pero NO obliga a conservar nombres, atributos, métodos o implementaciones.
> Los cambios deben continuar respetando el contrato de integración del proyecto.

## Responsabilidad principal

Según el requerimiento del proyecto, el Desarrollador 1 trabaja principalmente en:

- Categorías.
- Productos.
- Inventario.

Clases y componentes inicialmente previstos:

- Categoria
- Producto
- Inventario
- CategoriaRepository
- ProductoRepository
- ProductoService
- CategoriaService
- ProductoController

El desarrollador puede reorganizar esta estructura si encuentra una solución técnicamente más adecuada, siempre que se mantengan claras las responsabilidades y se coordine cualquier cambio que afecte a otros módulos.

---

# 1. MODEL

## 1.1 Categoria

**[D1 — MODEL — BORRADOR]**

Propósito:
- Representar una categoría de productos.
- Mantener únicamente los datos y comportamiento propio de la entidad.

Base sugerida:

```java
package com.negocio.analitica.model;

public class Categoria {

    private Long id;
    private String nombre;
    private String descripcion;

    // TODO D1:
    // Revisar atributos.
    // Decidir si descripcion es necesaria.
    // Agregar constructor(es).
    // Agregar getters/setters o la estrategia de encapsulamiento elegida.
}
```

### Espacio para decisiones del desarrollador

- Atributos definitivos: ______________________________
- Constructores: ______________________________________
- Validaciones propias: ________________________________
- Métodos adicionales: _________________________________

---

## 1.2 Producto

**[D1 — MODEL — BORRADOR]**

Propósito:
- Representar un producto.
- Relacionar el producto con una categoría.
- Mantener los datos necesarios para el control de existencias.

Base sugerida:

```java
package com.negocio.analitica.model;

public class Producto {

    private Long id;
    private String nombre;
    private double precio;
    private int stock;
    private int stockMinimo;
    private Categoria categoria;

    // TODO D1:
    // Revisar tipos de datos.
    // Definir atributos definitivos.
    // Definir constructores.
    // Implementar encapsulamiento.
    // Determinar las validaciones que correspondan.
}
```

### Espacio para decisiones del desarrollador

- Atributos definitivos: ______________________________
- Tipo de identificador: ______________________________
- Manejo del precio: __________________________________
- Relación con Categoria: _____________________________
- Manejo de stock: ____________________________________

---

## 1.3 Inventario

**[D1 — MODEL / DOMINIO — BORRADOR]**

Propósito:
- Representar o gestionar la existencia de productos.
- Centralizar la lógica relacionada con disponibilidad y actualización de stock.

Base orientativa:

```java
package com.negocio.analitica.model;

public class Inventario {

    // TODO D1:
    // Definir si Inventario será una entidad independiente,
    // una clase de dominio o si el control de stock quedará
    // directamente asociado a Producto.

    public boolean hayDisponibilidad(int stockActual, int cantidad) {
        return stockActual >= cantidad;
    }

    // TODO D1:
    // Definir cómo se realizará la entrada y salida de stock.
}
```

**Importante:** esta decisión queda abierta al criterio del Desarrollador 1.

---

# 2. REPOSITORY / PERSISTENCIA

## 2.1 CategoriaRepository

**[D1 — REPOSITORY — BORRADOR]**

Responsabilidad:
- Persistir y consultar categorías.
- No debe contener lógica propia de la interfaz.

```java
package com.negocio.analitica.persistence;

public class CategoriaRepository {

    // TODO D1:
    // Definir operaciones CRUD necesarias.
    // Definir conexión/persistencia según la tecnología elegida.
}
```

Operaciones por definir:
- Crear: ______________________________
- Buscar: _____________________________
- Listar: _____________________________
- Actualizar: _________________________
- Eliminar: ___________________________

---

## 2.2 ProductoRepository

**[D1 — REPOSITORY — BORRADOR]**

```java
package com.negocio.analitica.persistence;

public class ProductoRepository {

    // TODO D1:
    // Definir operaciones CRUD.
    // Definir búsquedas necesarias.
    // Definir manejo de persistencia.
}
```

Posibles operaciones:
- guardar(...)
- buscarPorId(...)
- listar(...)
- actualizar(...)
- eliminar(...)
- buscarPorCategoria(...)

**El desarrollador puede cambiar estas operaciones según la implementación definitiva.**

---

# 3. SERVICE

## 3.1 CategoriaService

**[D1 — SERVICE — BORRADOR]**

Responsabilidad:
- Contener las reglas de negocio relacionadas con categorías.
- Validar los datos que correspondan.
- Utilizar CategoriaRepository para persistencia.

```java
package com.negocio.analitica.service;

public class CategoriaService {

    // TODO D1:
    // Crear dependencia hacia CategoriaRepository.
    // Definir reglas de validación.
    // Definir operaciones del servicio.
}
```

---

## 3.2 ProductoService

**[D1 — SERVICE — BORRADOR]**

Responsabilidad:
- Gestionar las reglas de negocio de Producto.
- Validar datos.
- Coordinar la persistencia.
- Mantener las reglas relacionadas con productos y stock que correspondan al módulo D1.

```java
package com.negocio.analitica.service;

public class ProductoService {

    // TODO D1:
    // Definir validaciones.
    // Definir CRUD.
    // Definir búsquedas.
    // Definir interacción con Inventario.
}
```

### Reglas a considerar

- Precio válido.
- Nombre válido.
- Categoría existente cuando corresponda.
- Stock válido.
- Stock mínimo.
- Disponibilidad antes de una operación de venta.

Estas reglas son una base de análisis; D1 debe definir su implementación definitiva.

---

# 4. CONTROLLER

## ProductoController

**[D1 — CONTROLLER — BORRADOR]**

El Controller debe recibir las acciones de la interfaz y delegar al Service.

```java
package com.negocio.analitica.controller;

public class ProductoController {

    // TODO D1:
    // Definir dependencia hacia ProductoService.
    // Crear métodos necesarios para exponer las operaciones.
}
```

Ejemplos de operaciones a considerar:
- registrarProducto(...)
- listarProductos(...)
- buscarProducto(...)
- actualizarProducto(...)
- eliminarProducto(...)

---

# 5. RELACIÓN CON EL DESARROLLADOR 2

El módulo D1 es propietario de Producto, Categoria e Inventario.

El Desarrollador 2 puede necesitar consultar productos desde Ventas.

**Regla de integración:**

D2 puede utilizar Producto por su identificador/código y solicitar operaciones relacionadas con disponibilidad o actualización de existencias mediante el contrato definido.

D2 NO debe duplicar:
- La clase Producto.
- El CRUD de Producto.
- La lógica principal de stock.
- La lógica principal de categorías.

Si D1 cambia el contrato que utiliza Ventas, debe coordinar el cambio antes de integrarlo.

---

# 6. FLUJO PROPUESTO

```text
UI
 |
 v
ProductoController
 |
 v
ProductoService
 |
 v
ProductoRepository
 |
 v
Base de datos
```

Para inventario:

```text
VentaService (D2)
       |
       v
Contrato de disponibilidad / actualización
       |
       v
Inventario / ProductoService (D1)
       |
       v
Base de datos
```

La forma exacta de implementar esta integración queda a decisión del equipo.

---

# 7. PENDIENTES DE D1

- [ ] Definir estructura definitiva de Categoria.
- [ ] Definir estructura definitiva de Producto.
- [ ] Definir estrategia definitiva para Inventario.
- [ ] Definir repositories.
- [ ] Definir services.
- [ ] Definir controller(s).
- [ ] Definir validaciones.
- [ ] Definir persistencia.
- [ ] Probar CRUD.
- [ ] Probar stock.
- [ ] Definir contrato que utilizará Ventas.
- [ ] Documentar decisiones importantes.

## Decisiones propias del desarrollador

> Espacio libre para que D1 documente cambios respecto a este borrador.

____________________________________________________________

____________________________________________________________

____________________________________________________________

