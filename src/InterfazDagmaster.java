import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/**
 * Esta es la interfaz gráfica del proyecto, hecha con JavaFX.
 * Reemplaza la consola por una ventana más amigable y profesional.
 * 
 * Incluye:
 * - Botones para cargar archivo, detectar ciclos, calcular orden, exportar.
 * - Barra de estado y tiempo de ejecución.
 * - Colores en la salida para distinguir mensajes (éxito, error, advertencia, info).
 * - Tooltips que explican qué hace cada botón.
 * - Exportación a formato DOT para visualizar el grafo con Graphviz.
 * 
 * @author Luigi Zamora, Rodolfo Castro, Angel Tomasini
 */
public class InterfazDagmaster extends Application {

    // Componentes de la interfaz
    private TextFlow areaSalida;          // Área con colores
    private Label lblArchivo;
    private Label lblEstado;
    private Label lblTiempo;
    private GrafoDependencias grafoActual;
    private String nombreArchivoActual;

    // Botones, los declaramos como atributos para poder habilitar/deshabilitar
    private Button btnDetectar;
    private Button btnOrden;
    private Button btnExportar;
    private Button btnExportarGrafo;
    private Button btnLimpiar;

    @Override
    public void start(Stage stage) {
        stage.setTitle("DAGmaster - Analizador de Dependencias");
        stage.setMinWidth(850);
        stage.setMinHeight(650);

        //  Panel superior 
        Label lblTitulo = new Label("DAGmaster");
        lblTitulo.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        lblTitulo.setStyle("-fx-text-fill: white;");

        Button btnSeleccionar = new Button("📂 Seleccionar archivo");
        btnSeleccionar.setTooltip(new Tooltip("Selecciona un archivo .txt con las dependencias"));
        btnSeleccionar.setOnAction(e -> seleccionarArchivo(stage));

        lblArchivo = new Label("Ningún archivo cargado");
        lblArchivo.setStyle("-fx-font-style: italic; -fx-text-fill: #ecf0f1;");

        HBox topBar = new HBox(15, lblTitulo, btnSeleccionar, lblArchivo);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(15, 20, 15, 20));
        topBar.setStyle("-fx-background-color: #2c3e50;");

        //  Área de salida con colores 
        areaSalida = new TextFlow();
        areaSalida.setPadding(new Insets(10));
        areaSalida.setStyle("-fx-background-color: #1e272e;");
        ScrollPane scroll = new ScrollPane(areaSalida);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(true);
        scroll.setStyle("-fx-background-color: #1e272e;");

        //  Barra de herramientas 
        btnDetectar = new Button("🔍 Detectar ciclos");
        btnDetectar.setTooltip(new Tooltip("Analiza el grafo en busca de dependencias circulares"));
        btnDetectar.setOnAction(e -> detectarCiclos());
        btnDetectar.setDisable(true);

        btnOrden = new Button("📋 Calcular orden");
        btnOrden.setTooltip(new Tooltip("Calcula el orden topológico de ejecución de las tareas"));
        btnOrden.setOnAction(e -> calcularOrden());
        btnOrden.setDisable(true);

        btnExportar = new Button("💾 Exportar orden");
        btnExportar.setTooltip(new Tooltip("Guarda el orden topológico en un archivo .txt"));
        btnExportar.setOnAction(e -> exportarOrden());
        btnExportar.setDisable(true);

        btnExportarGrafo = new Button("📊 Exportar grafo");
        btnExportarGrafo.setTooltip(new Tooltip("Exporta el grafo a formato DOT (para Graphviz)"));
        btnExportarGrafo.setOnAction(e -> exportarGrafoDot());
        btnExportarGrafo.setDisable(true);

        btnLimpiar = new Button("🗑️ Limpiar");
        btnLimpiar.setTooltip(new Tooltip("Limpia el área de resultados"));
        btnLimpiar.setOnAction(e -> {
            areaSalida.getChildren().clear();
            lblEstado.setText("Listo");
            lblTiempo.setText("");
        });

        // Estilo de los botones
        String estiloBoton = "-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-border-radius: 5; -fx-background-radius: 5;";
        String estiloBotonPeligro = "-fx-background-color: #e67e22; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 15; -fx-border-radius: 5; -fx-background-radius: 5;";

        btnDetectar.setStyle(estiloBoton);
        btnOrden.setStyle(estiloBoton);
        btnExportar.setStyle(estiloBoton);
        btnExportarGrafo.setStyle(estiloBoton);
        btnLimpiar.setStyle(estiloBotonPeligro);

        HBox toolbar = new HBox(15, btnDetectar, btnOrden, btnExportar, btnExportarGrafo, btnLimpiar);
        toolbar.setAlignment(Pos.CENTER);
        toolbar.setPadding(new Insets(10, 20, 10, 20));
        toolbar.setStyle("-fx-background-color: #34495e;");

        //  Barra de estado 
        lblEstado = new Label("Listo");
        lblEstado.setStyle("-fx-text-fill: #bdc3c7; -fx-padding: 5 10;");

        lblTiempo = new Label("");
        lblTiempo.setStyle("-fx-text-fill: #bdc3c7; -fx-padding: 5 10;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox statusBar = new HBox(10, lblEstado, spacer, lblTiempo);
        statusBar.setPadding(new Insets(5, 15, 5, 15));
        statusBar.setStyle("-fx-background-color: #2c3e50; -fx-border-color: #34495e; -fx-border-width: 1 0 0 0;");
        statusBar.setAlignment(Pos.CENTER_LEFT);

        //  Layout principal 
        VBox centro = new VBox(toolbar, scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setTop(topBar);
        root.setCenter(centro);
        root.setBottom(statusBar);

        Scene scene = new Scene(root, 850, 650);
        stage.setScene(scene);
        stage.show();

        // Mensaje de bienvenida
        agregarTexto("[INFO] ¡Bienvenido a DAGmaster!\n", Color.CORNFLOWERBLUE);
        agregarTexto("[INFO] Selecciona un archivo .txt para comenzar.\n", Color.CORNFLOWERBLUE);
        agregarTexto("[INFO] Formato esperado: tarea: dep1 dep2 ...\n", Color.CORNFLOWERBLUE);
        agregarTexto("[INFO] Las líneas con # son comentarios.\n\n", Color.CORNFLOWERBLUE);
    }

    //  MÉTODOS AUXILIARES PARA TEXTO CON COLOR 

    private void agregarTexto(String texto, Color color) {
        Text t = new Text(texto);
        t.setFill(color);
        t.setFont(Font.font("Consolas", 14));
        areaSalida.getChildren().add(t);
    }


    //  HABILITAR/DESHABILITAR BOTONES 

    private void habilitarBotones(boolean habilitado) {
        btnDetectar.setDisable(!habilitado);
        btnOrden.setDisable(!habilitado);
        btnExportar.setDisable(!habilitado);
        btnExportarGrafo.setDisable(!habilitado);
    }

    //  SELECCIONAR ARCHIVO 

    private void seleccionarArchivo(Stage stage) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Seleccionar archivo de dependencias");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de texto", "*.txt"));
        File archivo = fc.showOpenDialog(stage);
        if (archivo != null) {
            nombreArchivoActual = archivo.getAbsolutePath();
            lblArchivo.setText("📄 " + archivo.getName());
            grafoActual = new GrafoDependencias();

            try {
                long startTime = System.currentTimeMillis();
                LectorArchivo.cargar(nombreArchivoActual, grafoActual);
                long endTime = System.currentTimeMillis();

                areaSalida.getChildren().clear();
                agregarTexto("[OK] Archivo cargado: " + archivo.getName() + "\n", Color.LIMEGREEN);
                agregarTexto("[INFO] Tareas: " + grafoActual.tamaño() + "\n", Color.CORNFLOWERBLUE);
                agregarTexto("[INFO] Tiempo de carga: " + (endTime - startTime) + " ms\n\n", Color.CORNFLOWERBLUE);

                mostrarGrafo();
                lblEstado.setText("Archivo cargado: " + archivo.getName());
                lblTiempo.setText("");

                habilitarBotones(true);

            } catch (IOException e) {
                agregarTexto("[ERROR] No se pudo leer el archivo: " + e.getMessage() + "\n", Color.RED);
                grafoActual = null;
                lblArchivo.setText("Error al cargar");
                lblEstado.setText("Error");
                habilitarBotones(false);
            }
        }
    }

    //  MOSTRAR GRAFO 

    private void mostrarGrafo() {
        if (grafoActual == null) return;
        agregarTexto("=== GRAFO DE DEPENDENCIAS ===\n", Color.CORNFLOWERBLUE);
        for (Map.Entry<String, List<String>> entrada : grafoActual.getGrafo().entrySet()) {
            String tarea = entrada.getKey();
            List<String> deps = entrada.getValue();
            StringBuilder sb = new StringBuilder();
            sb.append(tarea).append(" -> [");
            for (int i = 0; i < deps.size(); i++) {
                sb.append(deps.get(i));
                if (i < deps.size() - 1) sb.append(", ");
            }
            sb.append("]\n");
            if (deps.isEmpty()) {
                agregarTexto(sb.toString(), Color.GRAY);
            } else {
                agregarTexto(sb.toString(), Color.WHITE);
            }
        }
        agregarTexto("================================\n\n", Color.CORNFLOWERBLUE);
    }

    //  DETECTAR CICLOS 

    private void detectarCiclos() {
        if (grafoActual == null) {
            agregarTexto("[ERROR] Primero carga un archivo.\n", Color.RED);
            return;
        }

        lblEstado.setText("Detectando ciclos...");
        long startTime = System.currentTimeMillis();

        DetectorCiclos detector = new DetectorCiclos(grafoActual);
        List<String> ciclo = detector.obtenerCiclo();

        long endTime = System.currentTimeMillis();
        lblTiempo.setText("Tiempo: " + (endTime - startTime) + " ms");

        if (ciclo != null && !ciclo.isEmpty()) {
            agregarTexto("[CICLO] ¡Ciclo detectado!\n", Color.RED);
            agregarTexto("[CICLO] Ruta del ciclo: ", Color.ORANGE);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ciclo.size(); i++) {
                sb.append(ciclo.get(i));
                if (i < ciclo.size() - 1) sb.append(" → ");
            }
            agregarTexto(sb.toString() + "\n", Color.ORANGE);
            agregarTexto("[INFO] No se puede calcular el orden topológico.\n\n", Color.CORNFLOWERBLUE);
            lblEstado.setText("Ciclo detectado");
            btnOrden.setDisable(true);
            btnExportar.setDisable(true);
        } else {
            agregarTexto("[OK] No se encontraron ciclos. El grafo es un DAG.\n\n", Color.LIMEGREEN);
            lblEstado.setText("No hay ciclos");
            btnOrden.setDisable(false);
        }
    }

    // CALCULAR ORDEN TOPOLÓGICO 

    private void calcularOrden() {
        if (grafoActual == null) {
            agregarTexto("[ERROR] Primero carga un archivo.\n", Color.RED);
            return;
        }

        DetectorCiclos detector = new DetectorCiclos(grafoActual);
        if (detector.hayCiclo()) {
            agregarTexto("[ERROR] No se puede calcular orden porque hay ciclo.\n", Color.RED);
            return;
        }

        lblEstado.setText("Calculando orden topológico...");
        long startTime = System.currentTimeMillis();

        OrdenTopologico ordenador = new OrdenTopologico(grafoActual);
        List<String> orden = ordenador.obtenerOrden();

        long endTime = System.currentTimeMillis();
        lblTiempo.setText("Tiempo: " + (endTime - startTime) + " ms");

        if (orden == null) {
            agregarTexto("[ERROR] Kahn detectó un ciclo (inconsistencia).\n", Color.RED);
            lblEstado.setText("Error");
            return;
        }

        agregarTexto("\n[ORDEN] ORDEN TOPOLÓGICO DE EJECUCIÓN:\n", Color.CORNFLOWERBLUE);
        for (int i = 0; i < orden.size(); i++) {
            agregarTexto((i + 1) + ". " + orden.get(i) + "\n", Color.WHITE);
        }
        agregarTexto("[OK] Orden calculado correctamente.\n", Color.LIMEGREEN);
        lblEstado.setText("Orden calculado");

        areaSalida.setUserData(orden);
        btnExportar.setDisable(false);
    }

    //  EXPORTAR ORDEN 

    private void exportarOrden() {
        Object data = areaSalida.getUserData();
        if (!(data instanceof List)) {
            agregarTexto("[ERROR] No hay orden calculado. Ejecuta 'Calcular orden' primero.\n", Color.RED);
            return;
        }
        List<String> orden = (List<String>) data;
        FileChooser fc = new FileChooser();
        fc.setTitle("Guardar orden topológico");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos de texto", "*.txt"));
        fc.setInitialFileName("orden_topologico.txt");
        File archivo = fc.showSaveDialog(null);
        if (archivo == null) return;

        try (PrintWriter writer = new PrintWriter(archivo)) {
            writer.println("=== DAGMASTER - ORDEN TOPOLÓGICO ===");
            writer.println("Archivo fuente: " + nombreArchivoActual);
            writer.println("Fecha: " + new java.util.Date());
            writer.println("Total de tareas: " + orden.size());
            writer.println("-----------------------------------");
            for (int i = 0; i < orden.size(); i++) {
                writer.println((i + 1) + ". " + orden.get(i));
            }
            writer.println("-----------------------------------");
            writer.println("Fin del orden.");
            agregarTexto("[GUARDADO] Orden exportado a: " + archivo.getName() + "\n", Color.LIMEGREEN);
        } catch (IOException e) {
            agregarTexto("[ERROR] No se pudo guardar: " + e.getMessage() + "\n", Color.RED);
        }
    }

    //  EXPORTAR GRAFO A DOT 

    private void exportarGrafoDot() {
        if (grafoActual == null) {
            agregarTexto("[ERROR] Primero carga un archivo.\n", Color.RED);
            return;
        }
        FileChooser fc = new FileChooser();
        fc.setTitle("Exportar grafo a DOT");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos DOT", "*.dot"));
        fc.setInitialFileName("grafo.dot");
        File archivo = fc.showSaveDialog(null);
        if (archivo == null) return;

        try (PrintWriter writer = new PrintWriter(archivo)) {
            writer.println("digraph Dependencias {");
            writer.println("    // Grafo generado por DAGmaster");
            writer.println("    rankdir=LR;");
            for (Map.Entry<String, List<String>> entrada : grafoActual.getGrafo().entrySet()) {
                for (String dep : entrada.getValue()) {
                    writer.println("    \"" + entrada.getKey() + "\" -> \"" + dep + "\";");
                }
            }
            writer.println("}");
            agregarTexto("[GUARDADO] Grafo exportado a: " + archivo.getName() + "\n", Color.LIMEGREEN);
        } catch (IOException e) {
            agregarTexto("[ERROR] No se pudo exportar: " + e.getMessage() + "\n", Color.RED);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}