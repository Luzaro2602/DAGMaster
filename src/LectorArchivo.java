import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Esta clase solo se encarga de leer el archivo de dependencias y llenar el grafo.
 * La separamos de la lógica del grafo para que sea más fácil modificar el formato
 * de entrada sin tocar el resto del código.
 * 
 * Soporta líneas con comentarios (empiezan con #) y líneas vacías.
 * También detecta auto-dependencias (una tarea que depende de sí misma) y muestra 
 * una advertencia.
 */
public class LectorArchivo {

    /**
     * Lee el archivo línea por línea, parsea el formato "tarea: dep1 dep2 ..."
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
                // Eliminar comentarios: todo lo que esté después de # se ignora
                int posComentario = linea.indexOf('#');
                if (posComentario != -1) {
                    linea = linea.substring(0, posComentario);
                }
                linea = linea.trim();
                if (linea.isEmpty()) {
                    continue;   // líneas vacías no aportan nada
                }

                // Separar tarea y dependencias con ':'
                String[] partes = linea.split(":");
                if (partes.length < 1) {
                    System.err.println("⚠️ Error línea " + numLinea + ": falta ':' -> " + linea);
                    continue;
                }

                String tarea = partes[0].trim();
                if (tarea.isEmpty()) {
                    System.err.println("⚠️ Error línea " + numLinea + ": tarea vacía antes de ':'");
                    continue;
                }

                // Siempre registramos la tarea principal
                grafo.agregarTarea(tarea);

                // Si hay dependencias, las procesamos
                if (partes.length > 1 && !partes[1].trim().isEmpty()) {
                    String[] dependencias = partes[1].trim().split("\\s+");
                    for (String dep : dependencias) {
                        dep = dep.trim();
                        if (!dep.isEmpty()) {
                            // Detectar auto-dependencia y mostrar advertencia
                            if (dep.equals(tarea)) {
                                System.err.println("⚠️ Auto-dependencia detectada en línea " + numLinea + 
                                ": " + tarea + " depende de sí misma.");
                            }
                            grafo.agregarDependencia(tarea, dep);
                        }
                    }
                }
            }
        } finally {
            // Asegurar el cierre del archivo, incluso si hay excepción
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