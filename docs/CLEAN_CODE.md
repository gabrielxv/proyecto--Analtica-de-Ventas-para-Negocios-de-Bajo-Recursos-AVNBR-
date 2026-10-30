# Normas de Clean Code

1. Usar nombres claros que expresen intención.
2. Mantener una responsabilidad principal por clase.
3. Mantener métodos pequeños y concretos.
4. Evitar duplicación de reglas de negocio.
5. Mantener atributos encapsulados.
6. Separar UI, Controller, Service, Repository y Model.
7. Validar entradas en la capa correspondiente.
8. Evitar comentarios que solo repitan el código.
9. Evitar números y textos mágicos; usar constantes con significado.
10. Manejar errores explícitamente; no ocultar excepciones.
11. Evitar acoplamiento innecesario entre módulos.
12. Antes de integrar, comprobar compilación, contratos, pruebas y responsabilidades.
13. Usar mensajes de commit descriptivos y, preferiblemente, Conventional Commits.

## Aplicación verificable en AVNBR

- `ClienteService` concentra las reglas de clientes.
- `VentaService` coordina el registro de ventas y consume contratos mínimos de cliente y producto.
- `AnaliticaService` calcula indicadores y consume `ProductOperations` para consultar productos.
- `CsvClienteImporter` separa la lectura del archivo de la interacción de consola.
- `Application` actúa como punto de composición de implementaciones concretas.

La relación detallada entre clases y principios SOLID/GRASP está documentada en [SOLID_GRASP.md](SOLID_GRASP.md). Las responsabilidades de revisión e integración del líder técnico están en [TECHNICAL_LEAD_GUIDE.md](TECHNICAL_LEAD_GUIDE.md).
