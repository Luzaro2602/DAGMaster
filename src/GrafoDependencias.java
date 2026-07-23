import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Esta clase representa el grafo de dependencias usando listas de adyacencia.
 * La clave es el nombre de la tarea, y el valor es la lista de tareas de las que depende.
 * Elegimos HashMap porque el acceso es O(1) promedio, lo cual es eficiente para 
 * consultar dependencias y para los algoritmos DFS y Kahn.
 * 
 * @author Luigi Zamora, Rodolfo Castro, Angel Tomasini
 */
public class GrafoDependencias {
    private Map<String, List<String>> listaAdyacencia;

    public GrafoDependencias() {
        listaAdyacencia = new HashMap<>();
    }

    /**
     * Agrega una tarea si no existe.
     * Si ya existe, no hace nada (evita duplicados).
     * @param tarea nombre de la tarea
     */
    public void agregarTarea(String tarea) {
        if (!listaAdyacencia.containsKey(tarea)) {
            listaAdyacencia.put(tarea, new ArrayList<>());
        }
    }

    /**
     * Agrega una dependencia: 'tarea' depende de 'dependencia'.
     * Si alguna de las dos no existe, se crea automáticamente.
     * También evitamos duplicados en la lista de dependencias.
     * 
     * @param tarea tarea principal
     * @param dependencia tarea de la que depende
     */
    public void agregarDependencia(String tarea, String dependencia) {
        agregarTarea(tarea);
        agregarTarea(dependencia);
        List<String> deps = listaAdyacencia.get(tarea);
        if (!deps.contains(dependencia)) {
            deps.add(dependencia);
        }
    }

    /**
     * Devuelve la lista de dependencias de una tarea.
     * Si la tarea no existe, retorna una lista vacía para evitar NullPointerException.
     * @param tarea nombre de la tarea
     * @return Lista de dependencias (puede estar vacía)
     */
    public List<String> obtenerDependencias(String tarea) {
        if (listaAdyacencia.containsKey(tarea)) {
            return listaAdyacencia.get(tarea);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Obtiene el mapa completo del grafo.
     * Lo usamos cuando necesitamos recorrer todas las tareas.
     * @return el mapa original (no copia), ojo con modificarlo desde fuera.
     */
    public Map<String, List<String>> getGrafo() {
        return listaAdyacencia;
    }

    /**
     * Retorna la cantidad de tareas (nodos) en el grafo.
     * @return número de tareas distintas
     */
    public int tamaño() {
        return listaAdyacencia.size();
    }
}