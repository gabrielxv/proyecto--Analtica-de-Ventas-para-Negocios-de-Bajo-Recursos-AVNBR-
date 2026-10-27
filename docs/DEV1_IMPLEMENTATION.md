# Desarrollador 1 — Implementación de categorías, productos e inventario

## 1. Propósito y alcance

Este documento describe la implementación integrada del módulo de categorías, productos e inventario en el proyecto AVNBR. El borrador original se conserva como antecedente de diseño, pero no debe interpretarse como descripción literal del estado actual: la persistencia SQLite y las implementaciones concretas de los repositorios ya forman parte de la integración de `main`.

Los nombres de clases y métodos están en inglés, mientras que esta documentación explica sus responsabilidades en español. La solución sigue una arquitectura por capas para mantener separadas las entidades, las reglas de negocio, la coordinación y el almacenamiento.

## 2. Resumen de componentes

| Componente | Capa | Responsabilidad actual |
|---|---|---|
| `Category` | Model | Representa una categoría con identificador, nombre y descripción. |
| `Product` | Model | Representa el producto, precio, stock, mínimo de stock y categoría. |
| `Inventory` | Dominio | Contiene operaciones de disponibilidad, entradas, salidas y detección de stock bajo. |
| `CategoryRepository` | Repository | Define el contrato de acceso a categorías. |
| `ProductRepository` | Repository | Define el contrato de acceso a productos. |
| `SqliteCategoryRepository` | Persistencia | Implementa las operaciones de categorías mediante SQLite. |
| `SqliteProductRepository` | Persistencia | Implementa las operaciones de productos mediante SQLite. |
| `CategoryService` | Service | Valida y coordina la gestión de categorías. |
| `ProductService` | Service | Valida productos y coordina el catálogo y las operaciones de inventario. |
| `CategoryController` | Controller | Coordina las operaciones disponibles para categorías. |
| `ProductController` | Controller | Coordina las operaciones disponibles para productos. |
| `SqliteDatabase` | Infraestructura | Inicializa el archivo de base de datos, crea el esquema y configura conexiones SQLite. |

Los nombres anteriores reflejan las familias de componentes de la implementación integrada. Para conocer firmas exactas y detalles de cada método, la fuente definitiva es el código de la rama `main`.

## 3. Modelos del dominio

### 3.1 `Category`

Representa una categoría a la que pertenecen productos. Sus datos principales son identificador, nombre y descripción.

**Por qué existe:** evita tratar la categoría como texto libre repetido en cada producto y permite relacionarla mediante una clave foránea en la base de datos.

### 3.2 `Product`

Representa un producto del catálogo. Incluye identificador, nombre, precio, existencias actuales, stock mínimo y categoría.

**Validaciones importantes:** el nombre debe ser válido, el precio debe ser positivo y finito, el stock y el mínimo no pueden ser negativos y la categoría debe existir.

**Por qué existe:** es la entidad compartida por el catálogo, el inventario y el módulo de ventas. Mantener una representación común evita que cada módulo defina su propia versión de los datos del producto.

### 3.3 `Inventory`

Agrupa operaciones del dominio relacionadas con las existencias:

- Comprobar si una cantidad positiva está disponible.
- Registrar entradas de inventario.
- Registrar salidas solo cuando existe disponibilidad suficiente.
- Detectar stock bajo cuando las existencias están en el mínimo o por debajo.

**Decisión de diseño:** el inventario se mantiene como lógica del dominio asociada a los productos, no como una tabla independiente en el esquema descrito. La persistencia del stock forma parte del registro de productos.

## 4. Contratos de repositorio y persistencia

### 4.1 Interfaces

`CategoryRepository` y `ProductRepository` definen las operaciones de acceso a datos, como guardar, buscar por identificador, listar y eliminar.

**Razón para usar interfaces:** los servicios dependen de un contrato de persistencia, no directamente de sentencias SQL. Esto reduce el acoplamiento y permite reemplazar o probar la implementación con menos cambios en la lógica de negocio.

### 4.2 Implementaciones SQLite

La integración incluye implementaciones concretas SQLite para categorías y productos. Estas implementaciones ejecutan las operaciones de almacenamiento y recuperación usando JDBC.

**Corrección respecto al borrador inicial:** ya no es correcto afirmar que la persistencia está por seleccionar o que los repositorios son solamente interfaces sin implementación. Esa descripción corresponde a una etapa previa del trabajo, no al estado integrado actual.

### 4.3 `SqliteDatabase`

Centraliza la preparación de la base de datos local, la creación del esquema y la configuración de las conexiones, incluidas las claves foráneas.

La base de datos del prototipo se ubica en `data/avnbr.db`. El archivo de datos local no debe subirse al repositorio, porque es un artefacto de ejecución y puede contener información de prueba.

## 5. Servicios y reglas de negocio

### 5.1 `CategoryService`

Coordina las operaciones de creación, actualización, consulta, listado y eliminación de categorías. Valida los datos básicos antes de delegar el almacenamiento en el repositorio.

**Motivo:** mantener las reglas de categorías fuera del controlador y de la capa SQL.

### 5.2 `ProductService`

Coordina las operaciones de creación, actualización, consulta, listado y eliminación de productos, valida los atributos y comprueba la categoría asociada. También ofrece la funcionalidad de consulta de disponibilidad utilizada por otros módulos.

**Reglas relevantes:**

- El producto debe existir para actualizarlo o consultarlo.
- El nombre no puede estar vacío.
- El precio debe ser positivo y finito.
- El stock y el stock mínimo no pueden ser negativos.
- La categoría asociada debe existir.
- Las operaciones de disponibilidad deben rechazar cantidades inválidas o superiores al stock.

**Motivo:** el servicio es el lugar común para las reglas del producto. Esto evita que cada controlador o módulo implemente validaciones diferentes.

## 6. Controladores

### 6.1 `CategoryController`

Coordina las operaciones del módulo de categorías y delega el trabajo en `CategoryService`.

### 6.2 `ProductController`

Coordina las operaciones del módulo de productos y delega el trabajo en `ProductService`.

Los controladores actuales son clases Java de coordinación dentro del prototipo. No deben describirse como endpoints HTTP o una API REST, salvo que se implemente y documente explícitamente esa capa.

## 7. Flujo por capas

El flujo conceptual para productos es:

```text
Application / interfaz de consola
              |
              v
      ProductController
              |
              v
        ProductService
              |
              v
      ProductRepository
              |
              v
  SqliteProductRepository
              |
              v
       SQLite / JDBC
```

El flujo de categorías es equivalente y utiliza `CategoryController`, `CategoryService` y `CategoryRepository`. `SqliteDatabase` centraliza la inicialización y las conexiones.

**Beneficio:** cada capa tiene una función clara y se puede revisar o probar sin concentrar toda la lógica en una sola clase.

## 8. Contrato con el módulo de ventas

El módulo de ventas debe consultar productos y disponibilidad a través de los servicios del módulo dueño del catálogo e inventario. En la implementación integrada, `VentaService` utiliza `ProductService` para validar y actualizar el stock.

Esta decisión evita duplicar la regla de existencias en dos módulos. La venta obtiene el precio del producto registrado en el catálogo, en lugar de confiar en un importe arbitrario recibido de la interfaz o del llamador.

El registro de la venta y la actualización del stock requieren especial cuidado ante fallos. La implementación contempla compensar el stock si falla el guardado de la venta; como mejora futura, se recomienda hacer ambas operaciones atómicas mediante una transacción de base de datos.

## 9. Persistencia y restricciones

El esquema SQLite del prototipo contiene las tablas `categories` y `products` para el módulo de categorías/productos/inventario. La tabla de productos mantiene la relación con categoría mediante clave foránea.

| Regla | Objetivo |
|---|---|
| Nombre de categoría único | Evitar categorías duplicadas por nombre. |
| Precio positivo | Evitar productos con precio cero o negativo. |
| Stock no negativo | Evitar existencias inválidas. |
| Stock mínimo no negativo | Mantener una referencia válida para alertas. |
| Clave foránea a categoría | Impedir productos relacionados con categorías inexistentes. |

Los módulos de clientes y ventas amplían el esquema con `customers`, `sales` y `sale_details`; esos componentes se detallan en [la documentación del Desarrollador 2](DEVELOPER_2_IMPLEMENTATION.md).

## 10. Pruebas y verificación

La rama principal cuenta con una ejecución de GitHub Actions Java CI revisada que terminó correctamente y ejecutó compilación y pruebas. También se dispone de pruebas de integración del flujo de ventas, que comprueban interacciones con productos, existencias y analítica.

El equipo informó que realizó pruebas manuales. Para mantener trazabilidad, conviene registrar los casos concretos, resultados, fecha y responsable en la evidencia de pruebas del equipo. Este documento no inventa resultados de escenarios manuales que no estén detallados en el repositorio.

Para repetir la verificación automatizada desde la raíz del proyecto:

```bash
mvn clean verify
```

## 11. Decisiones técnicas y razones

| Decisión | Razón |
|---|---|
| Nombres de clases y métodos en inglés | Mantener consistencia en los identificadores del código. |
| Separación Model / Controller / Service / Repository | Reducir acoplamiento y facilitar mantenimiento y pruebas. |
| Interfaces de repositorio | Evitar que la lógica de negocio dependa directamente de SQLite. |
| SQLite por JDBC | Ofrecer persistencia local sencilla para el prototipo, sin administrar un servidor. |
| Stock bajo calculado respecto al mínimo | Permitir identificar productos que requieren reposición. |
| `ProductService` como contrato de inventario para ventas | Evitar duplicar validaciones y reglas de stock. |
| Integración mediante pull requests y CI | Mantener trazabilidad y verificar cambios antes de incorporarlos a `main`. |

## 12. Estado actual y mejoras futuras

### Integrado

- [x] Modelos de categorías y productos.
- [x] Lógica de inventario y disponibilidad.
- [x] Interfaces de repositorio.
- [x] Implementaciones SQLite de categorías y productos.
- [x] Servicios de categorías y productos.
- [x] Controladores de categorías y productos.
- [x] Inicialización y esquema SQLite.
- [x] Integración con el módulo de ventas mediante `ProductService`.
- [x] Configuración Maven y verificación automatizada en CI.

### Mejoras posibles

- [ ] Reforzar la atomicidad entre la venta y la actualización de existencias.
- [ ] Revisar el uso de `double` para importes si se exige precisión monetaria estricta.
- [ ] Añadir o ampliar pruebas de límites de stock, entradas/salidas y fallos de persistencia.
- [ ] Evaluar una base centralizada si el sistema debe atender varios usuarios simultáneamente.
- [ ] Mantener actualizada la evidencia de pruebas manuales y los documentos del proyecto.

## 13. Conclusión

El módulo de categorías, productos e inventario ya forma parte de la integración principal y dispone de persistencia SQLite. La arquitectura separa las reglas de negocio del almacenamiento, y el módulo de ventas reutiliza el servicio de productos para consultar y modificar existencias.

El borrador inicial se conserva como antecedente, pero las afirmaciones sobre persistencia no seleccionada, repositorios sin implementación y ausencia de verificación ya no describen el estado actual de `main`.
