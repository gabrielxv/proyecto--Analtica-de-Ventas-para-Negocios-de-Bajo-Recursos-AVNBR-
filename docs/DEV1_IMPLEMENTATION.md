# Desarrollador 1 — Productos e inventario

## 1. Propósito

Este documento describe el módulo integrado de categorías, productos e inventario de AVNBR. La implementación conserva la arquitectura y la persistencia SQLite de la rama principal; no reemplaza la aplicación completa por una demostración temporal en memoria.

## 2. Componentes y responsabilidades

| Componente | Capa | Responsabilidad |
|---|---|---|
| `Category` | Model | Representa categorías de productos. |
| `Product` | Model | Representa producto, precio, stock, mínimo y categoría. |
| `Inventory` | Dominio | Valida disponibilidad, entradas, salidas y stock bajo. |
| `CategoryRepository` / `ProductRepository` | Repository | Contratos de persistencia. |
| `SqliteCategoryRepository` / `SqliteProductRepository` | Persistencia | Guardan y consultan categorías y productos en SQLite. |
| `CategoryService` | Service | Aplica reglas de categorías. |
| `ProductService` | Service | Aplica reglas de productos y coordina movimientos de stock. |
| `CategoryController` / `ProductController` | Controller | Exponen operaciones del módulo a la interfaz. |
| `ConsoleMenu` | UI | Permite gestionar el catálogo y ejecutar operaciones de inventario. |

## 3. Reglas de productos

- El producto debe existir para actualizarlo, consultarlo o eliminarlo.
- El nombre no puede estar vacío.
- El precio debe ser positivo y finito.
- El stock y el stock mínimo no pueden ser negativos.
- La categoría asociada debe existir.
- Las operaciones de inventario requieren cantidades positivas.
- Una salida no puede superar las existencias disponibles.
- Una entrada que exceda el límite de un entero se rechaza para evitar desbordamiento.

## 4. Operaciones de inventario

`ProductService` conserva `implements ProductOperations`, por lo que ventas y analítica mantienen el contrato que ya utiliza la arquitectura integrada.

Además de las operaciones existentes, ofrece:

- `registerStockEntry(Long productId, int quantity)`: registra una entrada y persiste el producto actualizado.
- `registerStockExit(Long productId, int quantity)`: registra una salida si hay existencias suficientes y persiste el resultado.
- `hasLowStock(Long productId)`: comprueba si el producto está en el mínimo o por debajo.
- `findLowStockProducts()`: devuelve los productos que requieren reposición.

Las operaciones se delegan desde `ProductController`. La clase `Inventory` concentra las validaciones del movimiento; `ProductService` obtiene el producto y guarda el estado actualizado mediante el repositorio.

## 5. Interfaz de consola

Las opciones de inventario se incorporan al `ConsoleMenu` ya existente, sin sustituir `Application` ni retirar clientes, ventas, analítica, importación CSV o SQLite.

Opciones nuevas:

1. Registrar entrada de inventario.
2. Registrar salida de inventario.
3. Consultar productos con stock bajo.

En el menú principal aparecen como opciones 8, 9 y 10, respectivamente.

## 6. Persistencia e integración

El punto de entrada `Application` sigue inicializando `SqliteDatabase` y los repositorios SQLite. Por tanto, los movimientos guardan el nuevo stock en la base local `data/avnbr.db`; no se utiliza un repositorio en memoria como sustituto de la persistencia integrada.

Ventas y analítica siguen utilizando `ProductOperations` a través de la implementación `ProductService`. No se duplica la regla de disponibilidad en la interfaz.

## 7. Pruebas automatizadas

Se ampliaron las pruebas de `ProductServiceTest` para cubrir:

- Entrada de stock y persistencia del valor actualizado.
- Salida válida y rechazo de cantidades inválidas o superiores al stock.
- Detección de productos en el mínimo o por debajo.
- Rechazo de entradas que provocarían desbordamiento entero.

Verificación recomendada desde la raíz del proyecto:

```bash
mvn clean verify
```

El resultado definitivo de CI debe consultarse en la ejecución asociada al Pull Request de integración; no se declara un resultado exitoso antes de que termine.

## 8. Decisiones de diseño

- Se conserva SQLite y el ensamblado de módulos de `Application`.
- Se conserva `ProductOperations` para no romper dependencias de ventas y analítica.
- La lógica de stock se mantiene en `Inventory` y `ProductService`; no se agrega una entidad o tabla separada porque el alcance actual no lo requiere.
- La interfaz utiliza los controladores existentes en lugar de acceder directamente a repositorios.
- Se evita duplicar reglas de negocio en la consola.

## 9. Alcance y mejoras futuras

El módulo cubre catálogo, movimientos básicos y consulta de stock bajo. Como mejoras posteriores se pueden considerar un historial de movimientos, transacciones atómicas que agrupen ventas y stock, y reportes de reposición. No se agregan ahora porque no son necesarios para integrar las operaciones solicitadas.
