import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Encargada de leer el archivo de dependencias y poblar el grafo.
 * Separa la lógica de E/S de la lógica del grafo para mantener la modularidad.
 */
public class LectorArchivo {

    /**
     * Lee el archivo línea por línea, parsea el formato "tarea: dep1 dep2 "
     * y agrega las dependencias al grafo.
     * 
     * @param ruta ruta del archivo .txt (relativa o absoluta)
     * @param grafo instancia de GrafoDependencias donde se almacenarán los datos
     * @throws IOException si el archivo no se encuentra o no se puede leer
     */
    public static void cargar(String ruta, GrafoDependencias grafo) throws IOException {
        BufferedReader lector = null;
        try {
            lector = new BufferedReader(new FileReader(ruta));
            String linea;
            int numLinea = 0;

            while ((linea = lector.readLine()) != null) {
                numLinea++;
                linea = linea.trim();
                if (linea.isEmpty()) {
                    continue;   // Saltar líneas vacías
                }

                // Separar tarea de sus dependencias usando ':' como delimitador
                String[] partes = linea.split(":");
                if (partes.length < 1) {
                    System.err.println("Error línea " + numLinea + ": falta ':' -> " + linea);
                    continue;
                }

                String tarea = partes[0].trim();
                if (tarea.isEmpty()) {
                    System.err.println("Error línea " + numLinea + ": tarea vacía antes de ':'");
                    continue;
                }

                // Registrar la tarea principal (aunque no tenga dependencias)
                grafo.agregarTarea(tarea);

                // Procesar las dependencias si existen
                if (partes.length > 1 && !partes[1].trim().isEmpty()) {
                    // Separar por espacios (uno o más) para obtener cada dependencia
                    String[] dependencias = partes[1].trim().split("\\s+");
                    for (String dep : dependencias) {
                        dep = dep.trim();
                        if (!dep.isEmpty()) {
                            grafo.agregarDependencia(tarea, dep);
                        }
                    }
                }
            }
        } finally {
            // Asegurar el cierre del recurso, incluso si ocurre una excepción
            if (lector != null) {
                try {
                    lector.close();
                } catch (IOException e) {
                    System.err.println("Advertencia: no se pudo cerrar el archivo correctamente");
                    e.printStackTrace();
                }
            }
        }
    }
}