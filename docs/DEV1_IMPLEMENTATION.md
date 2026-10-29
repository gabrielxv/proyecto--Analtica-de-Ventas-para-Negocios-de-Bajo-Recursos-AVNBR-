# Dev 1 — Documentación de implementación

## 1. Propósito y alcance

Este documento registra el trabajo del Desarrollador 1 para el módulo de **categorías, productos e inventario** de AVNBR. Compara la propuesta de `docs/BORRADOR_DESARROLLADOR_1.md` con las clases que se añadieron a la rama `dev1-productos-inventario`.

El borrador se utiliza como punto de partida, no como una especificación inmutable. Los nombres de clases, atributos y métodos de la implementación se escribieron en inglés, como se acordó, y se mantuvo el enfoque por capas del proyecto.

## 2. Clases previstas en el borrador y correspondencia actual

| Nombre en el borrador | Nombre implementado | Capa | Estado |
|---|---|---|---|
| `Categoria` | `Category` | Model | Implementada |
| `Producto` | `Product` | Model | Implementada |
| `Inventario` | `Inventory` | Model / dominio | Implementada |
| `CategoriaRepository` | `CategoryRepository` | Repository | Interfaz creada |
| `ProductoRepository` | `ProductRepository` | Repository | Interfaz creada |
| `CategoriaService` | `CategoryService` | Service | Implementada |
| `ProductoService` | `ProductService` | Service | Implementada |
| `ProductoController` | `ProductController` | Controller | Implementada |

La estructura de paquetes utilizada es `com.negocio.analitica`, con subpaquetes `model`, `repository`, `service` y `controller`. También se añadieron adaptadores `InMemoryCategoryRepository` e `InMemoryProductRepository` para ejecutar el módulo sin base de datos; los datos se pierden al cerrar el programa.

## 3. Model

### 3.1 `Category`

**Propósito:** representar una categoría a la que pueden pertenecer los productos.

**Atributos privados:**
- `Long id`: identificador de la categoría.
- `String name`: nombre de la categoría.
- `String description`: descripción de la categoría.

**Constructores y métodos:**
- Constructor sin argumentos.
- Constructor con `id`, `name` y `description`.
- Getters y setters para los tres atributos.

**Procedencia y cambios:** el borrador proponía `Categoria` con `id`, `nombre` y `descripcion`. Se conservaron los tres datos y se tradujeron el nombre de la clase y los atributos al inglés. Se añadieron los constructores y los métodos de acceso.

### 3.2 `Product`

**Propósito:** representar un producto, su precio, sus existencias y su categoría.

**Atributos privados:**
- `Long id`: identificador del producto.
- `String name`: nombre.
- `double price`: precio.
- `int stock`: existencias actuales.
- `int minimumStock`: nivel mínimo de existencias.
- `Category category`: categoría relacionada.

**Constructores y métodos:**
- Constructor sin argumentos.
- Constructor con todos los atributos.
- Getters y setters para todos los atributos.

**Procedencia y cambios:** el borrador proponía `Producto` con `id`, `nombre`, `precio`, `stock`, `stockMinimo` y `categoria`. Se mantuvieron los mismos datos y la relación con una categoría; los identificadores de código se pasaron al inglés. El tipo de precio continúa siendo `double`, siguiendo la base propuesta.

### 3.3 `Inventory`

**Propósito:** concentrar operaciones de dominio relacionadas con la disponibilidad y los cambios de existencias de un producto.

**Métodos implementados:**
- `hasAvailability(int currentStock, int quantity)`: devuelve `true` si la cantidad solicitada es positiva y el stock alcanza para cubrirla.
- `registerEntry(Product product, int quantity)`: valida que el producto exista y que la cantidad sea positiva; después incrementa el stock.
- `registerExit(Product product, int quantity)`: valida que haya disponibilidad antes de descontar la cantidad solicitada.
- `hasLowStock(Product product)`: comprueba si el stock es menor o igual al mínimo configurado.

**Procedencia y cambios:** el borrador sugería `hayDisponibilidad(int stockActual, int cantidad)`, pero dejaba abierta la estrategia para entradas y salidas. Se adoptó el nombre inglés `hasAvailability` y se añadieron las operaciones `registerEntry`, `registerExit` y `hasLowStock`, junto con validaciones básicas.

**Decisión pendiente:** `Inventory` modifica el atributo `stock` de `Product`; todavía no es una entidad independiente ni tiene almacenamiento propio. Esta decisión debe revisarse con el líder si el contrato de integración requiere otra estrategia.

## 4. Repository

Los repositories definen las operaciones esperadas para acceder a los datos. Se crearon como **interfaces**, sin vincularlas aún a una base de datos o tecnología de persistencia.

### 4.1 `CategoryRepository`

Métodos declarados:
- `save(Category category)`: guardar una categoría.
- `findById(Long id)`: buscar por identificador; devuelve `Optional<Category>`.
- `findAll()`: obtener todas las categorías; devuelve `List<Category>`.
- `deleteById(Long id)`: eliminar por identificador.

### 4.2 `ProductRepository`

Métodos declarados:
- `save(Product product)`: guardar un producto.
- `findById(Long id)`: buscar por identificador; devuelve `Optional<Product>`.
- `findAll()`: obtener todos los productos; devuelve `List<Product>`.
- `deleteById(Long id)`: eliminar por identificador.

**Procedencia y cambios:** el borrador proponía `CategoriaRepository` y `ProductoRepository`, con operaciones CRUD por definir. Se adoptaron los nombres ingleses y se definió una interfaz inicial con operaciones para guardar, buscar, listar y eliminar. La actualización se cubre en los servicios mediante `save`, según la implementación que se conecte posteriormente.

**Pendiente:** implementar estas interfaces y decidir la persistencia. Hasta entonces, no guardan datos por sí solas.

## 5. Service

### 5.1 `CategoryService`

**Responsabilidad:** aplicar reglas básicas de negocio para las categorías y delegar el acceso a datos en `CategoryRepository`.

**Métodos:**
- Crear una categoría.
- Actualizar una categoría existente.
- Buscar una categoría por ID.
- Listar categorías.
- Eliminar una categoría.

Las operaciones validan que la categoría no sea nula y que el nombre no esté vacío; también manejan el caso en que no se encuentra una categoría al actualizar, consultar o eliminar.

**Procedencia y cambios:** el borrador dejaba por definir las operaciones y las validaciones. Se añadieron las operaciones de gestión y validaciones básicas.

### 5.2 `ProductService`

**Responsabilidad:** coordinar la gestión de productos, validar los datos y comprobar que la categoría asociada exista.

**Métodos:**
- Crear un producto.
- Actualizar un producto existente.
- Buscar un producto por ID.
- Listar productos.
- Eliminar un producto.
- `hasAvailability(...)`: consultar si hay existencias suficientes para una cantidad.\n- `registerStockEntry(Long productId, int quantity)`: registrar una entrada de stock por ID y guardar el producto actualizado.\n- `registerStockExit(Long productId, int quantity)`: registrar una salida de stock por ID y guardar el producto actualizado.\n- `hasLowStock(Long productId)`: comprobar si un producto está en su mínimo o por debajo.\n- `findLowStockProducts()`: listar los productos con stock bajo.

**Validaciones añadidas:**
- El producto no puede ser nulo.
- El nombre no puede estar vacío.
- El precio debe ser positivo y finito.
- El stock y el stock mínimo no pueden ser negativos.
- La categoría indicada debe existir.

**Procedencia y cambios:** el borrador ya indicaba validar nombre, precio, categoría, stock y stock mínimo, y contemplaba CRUD y disponibilidad. Se concretaron esas reglas en el servicio. La implementación todavía no representa una venta completa ni reemplaza el contrato que debe coordinarse con el módulo de ventas.

## 6. Controller

### `CategoryController`\n\n**Responsabilidad:** delegar en `CategoryService` las operaciones `createCategory`, `updateCategory`, `getCategory`, `getAllCategories` y `deleteCategory`.\n\n### `ProductController`

**Responsabilidad:** recibir llamadas relacionadas con productos y delegar el trabajo en `ProductService`.

**Métodos:**
- `createProduct(...)`: crear un producto.
- `updateProduct(...)`: actualizar un producto.
- `getProduct(...)`: consultar un producto.
- `getAllProducts()`: listar productos.
- `deleteProduct(...)`: eliminar un producto.

**Procedencia y cambios:** el borrador proponía `ProductoController` y ejemplos de registrar, listar, buscar, actualizar y eliminar. Se mantuvieron esas responsabilidades con nombres en inglés.

**Alcance actual:** todavía no se ha añadido un `CategoryController`; el borrador tampoco lo enumeraba entre sus ocho componentes iniciales.

## 7. Flujo por capas

El flujo objetivo sigue siendo:

```text
UI
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
Implementación de persistencia / base de datos
```

Para categorías, `CategoryService` utiliza `CategoryRepository`. Las clases de modelo representan los datos y `Inventory` contiene operaciones de dominio para las existencias.

La integración desde ventas debe coordinarse con el Desarrollador 2 y el líder. El módulo de ventas no debe duplicar las reglas principales de productos, categorías o stock.

## 8. Cambios añadidos respecto al borrador

1. Se cambiaron los nombres de las clases y de los atributos/métodos al inglés.
2. Se completaron los modelos con constructores, atributos privados, getters y setters.
3. Se definieron interfaces iniciales para `CategoryRepository` y `ProductRepository`, con `Optional` y `List` para representar resultados de consulta.
4. Se concretaron operaciones de creación, actualización, consulta, listado y eliminación en los servicios.
5. Se añadieron validaciones de nombre, precio, stock, stock mínimo y categoría existente.
6. Se añadieron operaciones de inventario para registrar entradas, registrar salidas y detectar stock bajo.
7. Se implementaron `CategoryController` y `ProductController` para delegar las operaciones de sus respectivos servicios.\n8. Se añadieron operaciones de stock al `ProductService` para registrar entradas y salidas y consultar productos con stock bajo.\n9. Se reemplazó la demostración fija de `Application` por un menú interactivo de consola con opciones para gestionar categorías, productos e inventario.

## 9. Estado actual y pendientes

### Añadido al repositorio
- [x] `Category`
- [x] `Product`
- [x] `Inventory`
- [x] `CategoryRepository` (interfaz)
- [x] `ProductRepository` (interfaz)
- [x] `CategoryService`
- [x] `ProductService`
- [x] `ProductController`
- [x] Esta documentación de implementación

### Pendiente
- [ ] Implementar la persistencia concreta y conectar los repositories a la base de datos elegida por el equipo.
- [ ] Compilar el proyecto y resolver posibles errores de integración.\n- [ ] Añadir y ejecutar pruebas automatizadas para los métodos nuevos y las opciones del menú.
- [ ] Añadir y ejecutar pruebas para validaciones, CRUD, entradas/salidas de stock y stock bajo.
- [ ] Acordar con el líder el contrato que utilizará el módulo de ventas para consultar disponibilidad y actualizar existencias.
- [ ] Revisar si el inventario debe continuar como lógica asociada a `Product` o convertirse en una entidad independiente.
- [ ] Confirmar que la estructura y el paquete estén alineados con los acuerdos definitivos del proyecto.

**Nota:** las clases se han añadido a la rama de trabajo, pero no se afirma que hayan sido compiladas o probadas. Los repositories son interfaces y la persistencia todavía no está conectada.
