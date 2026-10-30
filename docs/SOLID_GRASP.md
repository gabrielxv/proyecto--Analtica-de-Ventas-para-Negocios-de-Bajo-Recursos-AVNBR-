# Aplicación de SOLID y GRASP en AVNBR

## Propósito y criterio de evaluación

Este documento identifica dónde se aplican los principios SOLID y los patrones GRASP en el código actual. Se diferencia entre:

- **Aplicado:** existe una decisión concreta de diseño y puede señalarse en clases o métodos.
- **Parcial:** existe una base, pero faltan extensiones, pruebas o contratos para demostrar el principio por completo.
- **No demostrado:** el diseño actual no ofrece evidencia suficiente. No se debe declarar cumplimiento solo por tener interfaces o clases separadas.

La meta no es crear abstracciones artificiales para marcar una lista, sino mantener responsabilidades claras, bajo acoplamiento y código verificable.

---

## 1. SOLID

### S — Single Responsibility Principle (Responsabilidad única)

**Estado: aplicado, con una mejora concreta incorporada.**

- `ClienteService` concentra validación y operaciones de clientes.
- `VentaService` coordina las reglas del caso de uso de registrar una venta.
- `AnaliticaService` calcula indicadores a partir de las ventas.
- `ProductService` mantiene las reglas de productos e inventario.
- `CsvClienteImporter` se encarga exclusivamente de leer el CSV, interpretar sus filas y delegar el alta de clientes.
- `ConsoleMenu` recibe las entradas y presenta resultados; ya no implementa directamente el recorrido y análisis del archivo CSV.

**Dónde verlo:** `src/main/java/com/negocio/analitica/ConsoleMenu.java` y `CsvClienteImporter.java`; también los servicios dentro de `service/`.

**Cómo se aplicó:** se extrajo el código de lectura del CSV que estaba dentro de `ConsoleMenu` hacia `CsvClienteImporter`. El menú ahora solicita la ruta y muestra el resumen que devuelve el importador. Las reglas de unicidad y validación siguen en `ClienteService`, evitando duplicarlas.

**Beneficio:** los cambios al formato de importación no obligan a modificar el menú y la importación puede probarse por separado.

### O — Open/Closed Principle (Abierto/cerrado)

**Estado: parcial.**

- Los servicios consumen contratos (`ClienteRepository`, `VentaRepository`, `ProductRepository`) y no consultas SQL directamente.
- `VentaService` ahora recibe `ClienteLookup` y `ProductOperations`.
- `AnaliticaService` recibe `ProductOperations`.

**Dónde verlo:** `service/ClienteLookup.java`, `service/ProductOperations.java`, `service/VentaService.java`, `service/AnaliticaService.java` y las interfaces de `repository/`.

**Cómo se aplicó:** se pueden proporcionar implementaciones alternativas de esos contratos sin cambiar la lógica de ventas o analítica. Por ejemplo, un doble de prueba o una futura implementación de consulta de productos.

**Límite:** el sistema todavía no dispone de estrategias extensibles para políticas de descuento, impuestos o métodos de pago. No se introdujeron porque no son requisitos actuales. Cuando aparezcan variaciones reales, deben modelarse mediante políticas/estrategias, no mediante condicionales crecientes.

### L — Liskov Substitution Principle (Sustitución de Liskov)

**Estado: no demostrado todavía; existe una base contractual.**

Los contratos de repositorio y los nuevos puertos permiten intercambiar implementaciones, pero eso no demuestra por sí solo que todas las implementaciones sustitutas respeten el mismo comportamiento.

**Qué debe comprobarse:** una implementación alternativa de `ClienteLookup` debe devolver un cliente válido o lanzar el error de dominio acordado; una implementación de `ProductOperations` debe respetar la semántica de disponibilidad, actualización y listado.

**Próximo control recomendado:** pruebas de contrato compartidas que se ejecuten contra cada implementación real y de prueba. No se declara LSP como completo hasta tener esas pruebas.

### I — Interface Segregation Principle (Segregación de interfaces)

**Estado: aplicado.**

- `ClienteRepository`, `VentaRepository`, `ProductRepository` y `CategoryRepository` separan operaciones por agregado.
- `ClienteLookup` expone únicamente la consulta por ID requerida por ventas.
- `ProductOperations` agrupa solo las operaciones de productos necesarias para registrar ventas y calcular stock bajo.

**Dónde verlo:** interfaces en `repository/` y `service/ClienteLookup.java`, `service/ProductOperations.java`.

**Cómo se aplicó:** `VentaService` no recibe un servicio de clientes completo, sino el contrato mínimo de consulta que necesita. Esto evita obligar al consumidor a conocer operaciones ajenas a su responsabilidad.

### D — Dependency Inversion Principle (Inversión de dependencias)

**Estado: mejorado y aplicado en las dependencias críticas de los servicios.**

Antes, `VentaService` dependía directamente de `ClienteService` y `ProductService`; `AnaliticaService` dependía de `ProductService`. Ahora dependen de abstracciones:

- `VentaService` → `ClienteLookup`, `ProductOperations`, `VentaRepository`.
- `AnaliticaService` → `VentaRepository`, `ProductOperations`.
- `ClienteService` implementa `ClienteLookup`.
- `ProductService` implementa `ProductOperations`.

**Dónde verlo:** declaraciones de clases e inyectores por constructor en los servicios indicados.

**Cómo se aplicó:** las implementaciones concretas se conectan en `Application`, que actúa como punto de composición. Las reglas de negocio dependen de contratos y no de la clase SQLite ni de una implementación concreta de otro servicio.

**Límite:** los controladores aún reciben servicios concretos. Para el tamaño actual del proyecto no es necesario añadir otra capa de interfaces a cada controlador; se reconsiderará si aparecen múltiples implementaciones o pruebas que lo justifiquen.

---

## 2. GRASP

### 1. Experto en información — aplicado

`Venta` calcula su total y unidades vendidas usando sus propios `DetalleVenta`. La información necesaria está dentro de la entidad, por lo que el cálculo no se desplaza a la interfaz.

### 2. Creador — aplicado

`VentaService` construye la venta y sus detalles después de consultar el cliente y los productos, comprobar cantidades y obtener precios vigentes. Es el punto que dispone de la información para crear el objeto completo.

### 3. Controlador — aplicado

`ClienteController`, `VentaController`, `AnaliticaController`, `ProductController` y `CategoryController` reciben las solicitudes de la capa de presentación y delegan en los servicios. No deben contener consultas SQL ni reglas de negocio complejas.

### 4. Bajo acoplamiento — aplicado parcialmente, mejorado

La separación por capas y los repositorios reduce el acoplamiento con SQLite. Los puertos `ClienteLookup` y `ProductOperations` reducen además las dependencias entre ventas, analítica y servicios concretos. Se debe vigilar que no reaparezcan dependencias innecesarias.

### 5. Alta cohesión — aplicado

Los servicios agrupan operaciones relacionadas: clientes en `ClienteService`, ventas en `VentaService`, indicadores en `AnaliticaService`, productos e inventario en `ProductService`. La extracción de `CsvClienteImporter` mejora la cohesión del menú.

### 6. Fabricación pura — aplicado

Los servicios y repositorios son objetos de soporte que separan reglas de negocio y persistencia de las entidades del dominio. Por ejemplo, `VentaService` coordina el caso de uso y `SqliteVentaRepository` persiste la venta.

### 7. Indirección — aplicado

Los controladores y servicios median entre la presentación, las reglas de negocio y la persistencia. Los repositorios y los nuevos puertos evitan que los consumidores accedan directamente a SQLite o dependan de detalles concretos.

### 8. Variaciones protegidas — aplicado parcialmente

Las interfaces de repositorio protegen a los servicios de cambios en el motor de persistencia. `ClienteLookup` y `ProductOperations` protegen a ventas y analítica de cambios en las implementaciones concretas. Las políticas variables de descuentos, impuestos y medios de pago todavía no tienen contratos especializados porque no forman parte del alcance actual.

### 9. Polimorfismo — no demostrado como patrón de dominio

Java utiliza interfaces para permitir implementaciones intercambiables, pero el repositorio no tiene todavía varias políticas de negocio (por ejemplo, distintas reglas de pago o descuento) seleccionadas polimórficamente. No se debe confundir el uso de interfaces de persistencia con demostrar el patrón GRASP Polimorfismo para variaciones de negocio.

---

## 3. Relación con los roles del equipo

### Líder técnico

- Define la arquitectura por capas y los contratos de integración.
- Revisa que cada clase tenga una responsabilidad clara.
- Comprueba que los servicios dependan de abstracciones cuando exista una razón real.
- Revisa que los controladores no contengan reglas de negocio ni acceso SQL.
- Mantiene esta matriz de principios y registra los elementos parciales o no demostrados.
- Coordina pruebas de integración y de contrato antes de aceptar cambios.

### Desarrollador 2

- `ClienteService`: validación, unicidad documental y operaciones de clientes.
- `VentaService`: caso de uso de ventas, validación de cantidades, consulta de cliente y producto, comprobación de stock y persistencia.
- `AnaliticaService`: indicadores calculados a partir de las ventas.
- Consume `ClienteLookup` y `ProductOperations`, sin acoplar la lógica a las implementaciones concretas.
- Mantiene las reglas de stock en el módulo de productos/inventario y no las duplica.

---

## 4. Cómo verificar los cambios

Desde la raíz del proyecto:

```bash
mvn clean verify
```

La verificación debe confirmar compilación y ejecución de las pruebas automatizadas. Además, se recomienda probar manualmente:

1. Registrar un cliente y rechazar un documento duplicado.
2. Registrar una venta válida y comprobar el descuento de stock.
3. Intentar una venta con stock insuficiente y comprobar que se rechaza.
4. Consultar indicadores de un periodo.
5. Importar un CSV válido y otro con filas incorrectas.

**Nota de alcance:** esta documentación describe el diseño y los cambios de esta rama. No afirma que las pruebas hayan pasado hasta que se ejecute `mvn clean verify` en un entorno con Maven disponible.
