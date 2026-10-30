# AVNBR — Análisis de Ventas para Negocios de Bajos Recursos

Sistema Java 17 para pequeños negocios: categorías, productos, inventario, clientes, ventas e indicadores comerciales.

## Ejecución

Requisitos: Java 17 y Maven 3.9 o superior.

Desde la raíz del repositorio:

```bash
mvn clean verify
mvn exec:java -Dexec.mainClass="com.negocio.analitica.Application"
```

La aplicación inicia un menú interactivo en consola. La base SQLite se crea automáticamente en `data/avnbr.db`; los registros se conservan al cerrar y volver a iniciar el programa. La carpeta `data/` está excluida de Git.

## Funciones disponibles en el menú

1. Registrar categorías.
2. Registrar productos asociados a categorías.
3. Registrar clientes.
4. Registrar ventas con uno o varios productos; se valida el stock y se descuenta al confirmar.
5. Consultar categorías, productos, clientes y ventas almacenados.
6. Consultar ingresos, transacciones, unidades, ticket promedio, producto más vendido y alertas de stock bajo.
7. Importar clientes desde un archivo CSV externo.

### Formato CSV de clientes

El archivo debe estar codificado en UTF-8, con encabezado y cuatro columnas en este orden:

```csv
nombre,documento,telefono,direccion
Tienda Central,100200300,3001234567,Calle 10
Minimercado La 12,100200301,3007654321,Carrera 12
```

Los documentos duplicados o las filas inválidas se rechazan sin detener toda la importación. Para esta versión, los campos no deben contener comas internas. Al elegir la opción 7, ingrese la ruta local del archivo.

## Arquitectura

`Consola → Controller → Service → Repository → SQLite`

- **Model:** entidades del dominio.
- **Controller:** coordinación de operaciones.
- **Service:** validaciones y reglas de negocio.
- **Repository:** contratos de persistencia.
- **SQLite/JDBC:** almacenamiento local persistente.

## Tecnologías

- Java 17
- Maven
- SQLite JDBC
- JUnit 5
- GitHub Actions

## Verificación

Ejecute `mvn clean verify` para compilar y ejecutar las pruebas automatizadas.

## Alcance

Esta entrega incorpora una interfaz interactiva de consola y carga externa CSV de clientes. No es todavía una aplicación web ni una API REST. SQLite funciona como base local de un solo entorno; para varios usuarios remotos se requiere una base centralizada y una capa de servicio desplegada.
