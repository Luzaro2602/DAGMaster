import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representa un grafo dirigido mediante listas de adyacencia.
 * La clave es el nombre de la tarea, el valor es la lista de tareas de las que depende.
 * Esta estructura permite acceder a las dependencias en tiempo constante O(1) promedio.
 * 
 * @author Luigi Zamora, Rodolfo Castro, Angel Tomasini
 */
public class GrafoDependencias {
    private Map<String, List<String>> listaAdyacencia;

    public GrafoDependencias() {
        listaAdyacencia = new HashMap<>();
    }

    /**
     * Agrega una tarea al grafo si no existe.
     * Si ya existe, no hace nada.
     * @param tarea nombre de la tarea a agregar
     */
    public void agregarTarea(String tarea) {
        if (!listaAdyacencia.containsKey(tarea)) {
            listaAdyacencia.put(tarea, new ArrayList<>());
        }
    }

    /**
     * Establece que una tarea depende de otra.
     * Si alguna de las dos tareas no existe, se crea automáticamente.
     * Evita dependencias duplicadas (aunque el archivo de entrada no debería tenerlas).
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
     * Si la tarea no existe, retorna una lista vacía (evita NullPointerException).
     * @param tarea nombre de la tarea
     * @return Lista de dependencias (posiblemente vacía)
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
     * Útil para recorridos externos (por ejemplo, para mostrar el grafo o calcular grados).
     * @return Mapa original (no copia) - se debe usar con cuidado de no modificarlo externamente.
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