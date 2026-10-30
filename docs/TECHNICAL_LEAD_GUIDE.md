# Guía del líder técnico — AVNBR

## Objetivo del rol

El líder técnico es responsable de que el proyecto conserve una arquitectura coherente y de que los módulos de los desarrolladores se integren sin duplicar entidades ni reglas de negocio.

## Decisiones y evidencias en el código

| Responsabilidad | Dónde se observa | Qué debe revisar el líder |
|---|---|---|
| Punto de composición | `Application.java` | Conecta implementaciones concretas con servicios y controladores. |
| Contratos de persistencia | `repository/*Repository.java` | La lógica no debe ejecutar SQL desde UI o controller. |
| Contrato de consulta de clientes | `service/ClienteLookup.java` | Ventas necesita consultar clientes, no administrar todo el módulo. |
| Contrato de productos | `service/ProductOperations.java` | Ventas y analítica usan operaciones mínimas del inventario. |
| Caso de uso de ventas | `service/VentaService.java` | Validaciones, stock, creación de detalles y guardado. |
| Analítica | `service/AnaliticaService.java` | Cálculos sobre ventas y consulta de stock bajo. |
| Importación externa | `CsvClienteImporter.java` | Lectura CSV separada de la interacción de consola. |
| Presentación | `ConsoleMenu.java` | Captura y muestra información; no debe asumir reglas de negocio. |

## Cómo se relaciona el liderazgo con SOLID

- **S:** revisar que cada clase tenga un motivo principal para cambiar.
- **O:** favorecer contratos que permitan agregar implementaciones sin editar la lógica estable.
- **L:** solicitar pruebas que demuestren que las implementaciones respetan sus contratos.
- **I:** evitar interfaces grandes; definir contratos pequeños y orientados al consumidor.
- **D:** hacer que los módulos de negocio dependan de abstracciones y conectar las implementaciones en `Application`.

## Cómo se relaciona el liderazgo con GRASP

El líder debe comprobar que las responsabilidades estén asignadas a la clase que tiene la información o el conocimiento adecuado: entidades como expertas en sus datos, servicios como coordinadores de casos de uso, controladores como receptores de solicitudes y repositorios como responsables de persistencia.

## Lista de revisión antes de integrar un Pull Request

1. ¿La clase nueva tiene una responsabilidad clara?
2. ¿Se reutilizan entidades y servicios existentes?
3. ¿La UI o un controller accede directamente a SQLite?
4. ¿Las dependencias entre módulos están definidas mediante contratos razonables?
5. ¿Se validan los datos en el servicio responsable?
6. ¿Las reglas de inventario siguen centralizadas en el módulo de productos?
7. ¿Las pruebas cubren los casos válidos y los errores?
8. ¿La documentación explica dónde y por qué se aplicó cada principio?
9. ¿Se ejecutó `mvn clean verify` y se registró el resultado real?

## Guion breve para exposición

“Como líder técnico me encargué de mantener la arquitectura y los contratos de integración. Revisé que cada módulo tuviera responsabilidades claras y que los servicios dependieran de abstracciones en lugar de clases concretas cuando era necesario. En esta mejora se introdujeron ClienteLookup y ProductOperations para desacoplar ventas y analítica. También se separó la importación CSV del menú para mejorar la responsabilidad única. La documentación indica qué principios están aplicados, cuáles son parciales y cuáles requieren pruebas adicionales; no declaramos cumplimiento sin evidencia.”
