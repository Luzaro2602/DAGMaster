import java.util.*;

/**
 * Implementa el algoritmo de Kahn para obtener el orden topológico de un grafo dirigido.
 * 
 * El algoritmo funciona de la siguiente manera:
 *   1. Calculamos el grado de entrada de cada tarea (cuántas dependencias tiene).
 *   2. Metemos en una cola las tareas con grado 0 (no dependen de nadie).
 *   3. Vamos sacando una tarea de la cola, la agregamos al orden, y reducimos 
 *      el grado de las tareas que dependían de ella.
 *   4. Si una tarea queda con grado 0, la agregamos a la cola.
 *   5. Al final, si el orden no contiene todas las tareas, hay un ciclo.
 * 
 * Elegimos este algoritmo porque es eficiente (O(V+E)) y fácil de entender.
 * 
 * @author Luigi Zamora, Rodolfo Castro, Angel Tomasini
 */
public class OrdenTopologico {
    private GrafoDependencias grafo;

    public OrdenTopologico(GrafoDependencias grafo) {
        this.grafo = grafo;
    }

    /**
     * Calcula el orden topológico usando el algoritmo de Kahn.
     * @return Lista con el orden de ejecución, o null si hay un ciclo.
     */
    public List<String> obtenerOrden() {
        // Primero, calcular grado de entrada = número de dependencias
        Map<String, Integer> gradoEntrada = new HashMap<>();
        for (String tarea : grafo.getGrafo().keySet()) {
            gradoEntrada.put(tarea, grafo.obtenerDependencias(tarea).size());
        }

        // Construir la lista de dependientes: qué tareas dependen de cada una
        // Esto nos permite reducir el grado eficientemente cuando una tarea se ejecuta.
        Map<String, List<String>> dependientes = new HashMap<>();
        for (String tarea : grafo.getGrafo().keySet()) {
            dependientes.put(tarea, new ArrayList<>());
        }
        for (String tarea : grafo.getGrafo().keySet()) {
            for (String dep : grafo.obtenerDependencias(tarea)) {
                dependientes.get(dep).add(tarea);
            }
        }

        // Inicializar cola con tareas que ya pueden ejecutarse (grado 0)
        Queue<String> cola = new LinkedList<>();
        for (String tarea : gradoEntrada.keySet()) {
            if (gradoEntrada.get(tarea) == 0) {
                cola.add(tarea);
            }
        }

        // Procesar la cola
        List<String> orden = new ArrayList<>();
        while (!cola.isEmpty()) {
            String actual = cola.poll();
            orden.add(actual);

            // Reducir el grado de las tareas que dependían de 'actual'
            for (String dependiente : dependientes.get(actual)) {
                int nuevoGrado = gradoEntrada.get(dependiente) - 1;
                gradoEntrada.put(dependiente, nuevoGrado);
                if (nuevoGrado == 0) {
                    cola.add(dependiente);
                }
            }
        }

        // Verificar si procesamos todos los nodos
        if (orden.size() != grafo.tamaño()) {
            return null; // ciclo detectado
        }
        return orden;
    }
}