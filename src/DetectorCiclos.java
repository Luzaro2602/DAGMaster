import java.util.*;

/**
 * Implementa la detección de ciclos en un grafo dirigido usando DFS con colores.
 * Estados:
 *   0 = nodo no visitado
 *   1 = nodo en proceso de visita (en la pila recursiva actual)
 *   2 = nodo completamente procesado (no forma parte de un ciclo)
 * 
 * Si durante el DFS encontramos un vecino con estado 1, hay un ciclo.
 */
public class DetectorCiclos {

    private GrafoDependencias grafo;
    private Map<String, Integer> estado;   // Almacena el estado de cada tarea

    public DetectorCiclos(GrafoDependencias grafo) {
        this.grafo = grafo;
        this.estado = new HashMap<>();
    }

    /**
     * Verifica si el grafo contiene al menos un ciclo.
     * @return true si hay ciclo, false en caso contrario
     */
    public boolean hayCiclo() {
        // Inicializar todos los nodos como no visitados (0)
        for (String tarea : grafo.getGrafo().keySet()) {
            estado.put(tarea, 0);
        }

        // Recorrer cada nodo por si el grafo es disconexo
        for (String tarea : grafo.getGrafo().keySet()) {
            if (estado.get(tarea) == 0) {
                if (dfs(tarea)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Recorrido DFS recursivo.
     * @param nodo nodo actual
     * @return true si se encuentra un ciclo a partir de este nodo
     */
    private boolean dfs(String nodo) {
        estado.put(nodo, 1);   // Marcamos como "en visita"

        for (String vecino : grafo.obtenerDependencias(nodo)) {
            if (estado.get(vecino) == 1) {
                // Vecino está en la misma pila -> ciclo detectado
                return true;
            }
            if (estado.get(vecino) == 0) {
                if (dfs(vecino)) {
                    return true;
                }
            }
        }
        estado.put(nodo, 2);   // Marcamos como "completado"
        return false;
    }
}