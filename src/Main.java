import java.io.IOException;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Punto de entrada del programa.
 * Se encarga de la lectura del archivo, la construcción del grafo,
 * la detección de ciclos y la futura generación del orden topológico.
 * 
 * permite al usuario seleccionar qué archivo de dependencias leer.
 */
public class Main {
    
    // Directorio donde buscar los archivos .txt (por defecto, el actual)
    private static final String DIRECTORIO = ".";
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String nombreArchivo = null;
        
        // Si se pasó un argumento por línea de comandos, lo usamos directamente
        if (args.length > 0) {
            nombreArchivo = args[0];
            System.out.println("Usando archivo desde argumento: " + nombreArchivo);
        } else {
            // Mostrar menú interactivo
            nombreArchivo = seleccionarArchivo(scanner);
            if (nombreArchivo == null) {
                System.out.println("Saliendo del programa.");
                scanner.close();
                return;
            }
        }
        
        // Procesar el archivo seleccionado
        procesarArchivo(nombreArchivo);
        scanner.close();
    }
    
    /**
     * Muestra un menú para que el usuario seleccione un archivo .txt
     * @param scanner Scanner para leer la entrada del usuario
     * @return nombre del archivo seleccionado, o null si el usuario decide salir
     */
    private static String seleccionarArchivo(Scanner scanner) {
        while (true) {
            System.out.println("\n===== ANALIZADOR DE DEPENDENCIAS =====");
            System.out.println("1. Usar archivo por defecto (dependencias.txt)");
            System.out.println("2. Listar archivos .txt disponibles");
            System.out.println("3. Escribir el nombre de un archivo");
            System.out.println("4. Salir");
            System.out.print("Elige una opción (1-4): ");
            
            String opcion = scanner.nextLine().trim();
            
            switch (opcion) {
                case "1":
                    return "dependencias.txt";
                case "2":
                    listarArchivosTxt();
                    // Después de listar, volvemos al menú
                    break;
                case "3":
                    System.out.print("Ingresa el nombre del archivo (ej. ciclo_simple.txt): ");
                    String nombre = scanner.nextLine().trim();
                    if (nombre.isEmpty()) {
                        System.out.println("Nombre vacío. Vuelve a intentar.");
                        break;
                    }
                    // Verificar que el archivo existe
                    File archivo = new File(DIRECTORIO, nombre);
                    if (archivo.exists() && archivo.isFile()) {
                        return nombre;
                    } else {
                        System.out.println("Error: No se encuentra el archivo '" + nombre + "'");
                        System.out.println("Asegúrate de que esté en la carpeta: " + new File(DIRECTORIO).getAbsolutePath());
                    }
                    break;
                case "4":
                    return null;
                default:
                    System.out.println("Opción no válida. Intenta de nuevo.");
            }
        }
    }
    
    /**
     * Lista todos los archivos con extensión .txt en el directorio actual
     */
    private static void listarArchivosTxt() {
        File carpeta = new File(DIRECTORIO);
        File[] archivos = carpeta.listFiles((dir, name) -> name.toLowerCase().endsWith(".txt"));
        
        if (archivos == null || archivos.length == 0) {
            System.out.println("No se encontraron archivos .txt en el directorio actual.");
            System.out.println("Directorio actual: " + carpeta.getAbsolutePath());
            return;
        }
        
        System.out.println("\n--- Archivos .txt encontrados ---");
        for (int i = 0; i < archivos.length; i++) {
            System.out.println((i+1) + ". " + archivos[i].getName());
        }
        System.out.println("----------------------------------\n");
    }
    
    /**
     * Procesa el archivo: carga el grafo, muestra y detecta ciclos
     * @param nombreArchivo nombre del archivo a leer
     */
    private static void procesarArchivo(String nombreArchivo) {
        GrafoDependencias grafo = new GrafoDependencias();
        
        try {
            System.out.println("\n📂 Leyendo archivo: " + nombreArchivo);
            LectorArchivo.cargar(nombreArchivo, grafo);
            System.out.println("✅ Archivo cargado correctamente.\n");
            
            // Mostrar el grafo completo en consola
            System.out.println("=== GRAFO DE DEPENDENCIAS ===");
            for (Map.Entry<String, List<String>> entrada : grafo.getGrafo().entrySet()) {
                String tarea = entrada.getKey();
                List<String> dependencias = entrada.getValue();
                System.out.print(tarea + " -> [");
                for (int i = 0; i < dependencias.size(); i++) {
                    System.out.print(dependencias.get(i));
                    if (i < dependencias.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println("]");
            }
            System.out.println("=============================\n");
            
            // Detección de ciclos utilizando DFS
            DetectorCiclos detector = new DetectorCiclos(grafo);
            if (detector.hayCiclo()) {
                System.out.println("⚠️ ¡Ciclo detectado! No es posible calcular el orden topológico.");
            } else {
                System.out.println("✅ No se encontraron ciclos. El grafo es un DAG.");
                System.out.println("📌 Próximamente: orden topológico con el algoritmo de Kahn.");
            }
            
        } catch (IOException e) {
            System.err.println("❌ Error: No se pudo leer el archivo '" + nombreArchivo + "'");
            System.err.println("   Asegúrate de que el archivo existe y tiene permisos de lectura.");
            System.err.println("   Detalle técnico: " + e.getMessage());
        }
    }
}