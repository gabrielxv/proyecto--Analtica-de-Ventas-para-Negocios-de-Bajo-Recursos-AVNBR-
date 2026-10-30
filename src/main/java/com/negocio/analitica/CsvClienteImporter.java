package com.negocio.analitica;

import com.negocio.analitica.controller.ClienteController;
import com.negocio.analitica.model.Cliente;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Responsabilidad única: leer el formato CSV sencillo de clientes y delegar
 * la validación/alta en ClienteController.
 *
 * Formato esperado: nombre,documento,telefono,direccion.
 * Esta versión admite campos sin comas internas ni comillas escapadas.
 */
public final class CsvClienteImporter {
    private final ClienteController clientes;

    public CsvClienteImporter(ClienteController clientes) {
        if (clientes == null) throw new IllegalArgumentException("ClienteController es obligatorio.");
        this.clientes = clientes;
    }

    public ResultadoImportacion importar(Path archivo) {
        if (archivo == null) throw new IllegalArgumentException("La ruta del CSV es obligatoria.");
        try {
            List<String> lineas = Files.readAllLines(archivo, StandardCharsets.UTF_8);
            int importados = 0;
            int rechazados = 0;
            for (int i = 1; i < lineas.size(); i++) {
                if (lineas.get(i).isBlank()) continue;
                String[] campos = lineas.get(i).split(",", -1);
                if (campos.length != 4) {
                    rechazados++;
                    continue;
                }
                try {
                    clientes.crear(new Cliente(null, campos[0].trim(), campos[1].trim(),
                            campos[2].trim(), campos[3].trim()));
                    importados++;
                } catch (RuntimeException ex) {
                    rechazados++;
                }
            }
            return new ResultadoImportacion(importados, rechazados);
        } catch (IOException ex) {
            throw new IllegalArgumentException("No se pudo leer el archivo: " + ex.getMessage(), ex);
        }
    }

    public record ResultadoImportacion(int importados, int rechazados) { }
}
