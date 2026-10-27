# Arquitectura del proyecto

Arquitectura por capas: UI, Controller, Service, Repository/Persistence, Model y Database.

Flujo obligatorio: UI -> Controller -> Service -> Repository -> Database.

El lider controla arquitectura, contratos de integracion, calidad, pruebas y documentacion.

Los modulos de productos/inventario y clientes/ventas/analitica deben respetar los contratos definidos por el lider.