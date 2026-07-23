import java.util.*;

/**
 * Clase que se encarga de detectar ciclos en un grafo de dependencias.
 * Usamos DFS (búsqueda en profundidad) con colores para marcar el estado de cada nodo.
 * 
 * Estados:
 *   0 = no visitado
 *   1 = visitando (está en la pila de recursión actual)
 *   2 = ya procesado (no forma parte de ningún ciclo)
 * 
 * Si al recorrer encontramos un vecino que está en estado 1, significa que hay un ciclo.
 * Además, guardamos el padre de cada nodo para poder reconstruir la ruta exacta del ciclo.
 * 
 * Esta funcionalidad nos pareció útil para darle al usuario más información que solo 
 * "hay ciclo" o "no hay ciclo".
 * 
 * @author Luigi Zamora, Rodolfo Castro, Angel Tomasini
 */
public class DetectorCiclos {

    private GrafoDependencias grafo;
    private Map<String, Integer> estado;
    private Map<String, String> padre;   // Para reconstruir la ruta del ciclo
    private String nodoInicioCiclo;      // Nodo donde se detectó el ciclo

    public DetectorCiclos(GrafoDependencias grafo) {
        this.grafo = grafo;
        this.estado = new HashMap<>();
        this.padre = new HashMap<>();
        this.nodoInicioCiclo = null;
    }

    /**
     * Método público para saber si hay al menos un ciclo.
     * Simplemente llama a obtenerCiclo() y verifica si devuelve algo.
     * @return true si hay ciclo, false si no.
     */
    public boolean hayCiclo() {
        return obtenerCiclo() != null;
    }

    /**
     * Detecta un ciclo y devuelve la ruta como lista de tareas en orden.
     * Si no hay ciclo, retorna null.
     * 
     * @return Lista con las tareas que forman el ciclo, o null si no hay.
     */
    public List<String> obtenerCiclo() {
        // Inicializar todos los nodos como no visitados y sin padre
        for (String tarea : grafo.getGrafo().keySet()) {
            estado.put(tarea, 0);
            padre.put(tarea, null);
        }
        nodoInicioCiclo = null;

        // Recorremos cada nodo por si el grafo es disconexo (varios componentes)
        for (String tarea : grafo.getGrafo().keySet()) {
            if (estado.get(tarea) == 0) {
                if (dfsConRuta(tarea)) {
                    // Encontramos un ciclo, reconstruimos la ruta
                    return reconstruirCiclo();
                }
            }
        }
        return null;
    }

    /**
     * DFS recursivo que además guarda el padre de cada nodo.
     * Esto nos permite reconstruir la ruta cuando se encuentra un ciclo.
     */
    private boolean dfsConRuta(String nodo) {
        estado.put(nodo, 1); // marcamos como "en visita"
        for (String vecino : grafo.obtenerDependencias(nodo)) {
            if (estado.get(vecino) == 1) {
                // Vecino ya está en la pila actual → ciclo detectado
                nodoInicioCiclo = vecino;
                padre.put(vecino, nodo);
                return true;
            }
            if (estado.get(vecino) == 0) {
                padre.put(vecino, nodo);
                if (dfsConRuta(vecino)) {
                    return true;
                }
            }
        }
        estado.put(nodo, 2); // ya terminamos con este nodo
        return false;
    }

    /**
     * Reconstruye la ruta del ciclo desde el nodo donde se detectó.
     * Usamos el mapa 'padre' para ir hacia atrás hasta cerrar el ciclo.
     */
    private List<String> reconstruirCiclo() {
        List<String> ciclo = new ArrayList<>();
        String actual = nodoInicioCiclo;
        do {
            ciclo.add(actual);
            actual = padre.get(actual);
        } while (!actual.equals(nodoInicioCiclo));
        ciclo.add(nodoInicioCiclo); // cerramos el ciclo
        Collections.reverse(ciclo); // para que quede en orden de ejecución
        return ciclo;
    }
}

  