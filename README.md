#  DAGmaster – Analizador de Dependencias

**DAGmaster** es una herramienta de software desarrollada en Java que permite analizar dependencias entre tareas a partir de un archivo de texto. Su nombre combina **DAG** (Directed Acyclic Graph – Grafo Acíclico Dirigido) y **master**, reflejando su capacidad para dominar el análisis de dependencias.

---

## 👥 Integrantes del equipo

- **Luigi Zamora** – C.I: 27.640.831  
- **Rodolfo Castro** – C.I: 31.381.747  
- **Angel Tomasini** – C.I: 32.078.058  

**Materia:** Programación No Numérica I  
**Profesor:** William Jiménez  
**Institución:** I.U.P "Santiago Mariño" – Sede Maracay, Edo. Aragua  
**Fecha:** Junio 2026

---

##  Misión

Proveer una herramienta didáctica en Java que permita modelar dependencias entre tareas a partir de un archivo de texto, facilitando el aprendizaje de estructuras de datos y algoritmos de grafos (DFS y Kahn) en el contexto de planificación de procesos.

##  Visión

Convertirnos en un referente académico para el análisis de dependencias, ofreciendo un código claro y bien documentado que sirva de base para proyectos más complejos de automatización y gestión de flujos de trabajo.

##  Objetivos

1. Procesar un archivo de texto con formato `tarea: dependencia1 dependencia2 ...` y construir un grafo dirigido usando `HashMap<String, List<String>>`.
2. Detectar ciclos mediante recorrido DFS (profundidad) con estados de visita (0=no visitado, 1=visitando, 2=visitado).
3. Mostrar la ruta exacta del ciclo detectado.
4. Calcular el orden topológico de ejecución de tareas utilizando el algoritmo de Kahn (cola de nodos con grado de entrada cero).
5. Ofrecer una interfaz gráfica moderna con JavaFX para facilitar la interacción del usuario.
6. Exportar el orden topológico a un archivo de texto y el grafo a formato DOT (para visualización con Graphviz).
7. Manejar errores de forma robusta: archivo no encontrado, formato incorrecto, líneas vacías, comentarios, auto-dependencias.

---

## ✨ Características principales

| Característica | Descripción |
|----------------|-------------|
|  **Carga de archivos** | Selecciona archivos `.txt` mediante `FileChooser`. Soporta rutas absolutas y relativas. |
|  **Grafo con HashMap** | Representación eficiente mediante listas de adyacencia. |
|  **Detección de ciclos (DFS)** | Algoritmo DFS con colores (0,1,2). **Muestra la ruta exacta del ciclo** (ej. `A → B → A`). |
|  **Orden topológico (Kahn)** | Calcula el orden de ejecución de tareas. Solo si el grafo es un DAG. |
|  **Colores en la salida** | Usa `TextFlow` con colores: verde (OK), rojo (error), naranja (advertencia), azul (info). |
|  **Tiempo de ejecución** | Muestra el tiempo de carga y de cada algoritmo en milisegundos. |
|  **Exportar orden** | Guarda el orden topológico en un archivo `.txt` con metadatos. |
|  **Exportar a DOT** | Genera un archivo `.dot` para visualizar el grafo con Graphviz. |
|  **Soporte para comentarios** | Líneas que empiezan con `#` se ignoran. |
|  **Auto-dependencia** | Detecta y advierte si una tarea depende de sí misma (ej. `A: A`). |
|  **Manejo de errores** | Archivo no encontrado, formato incorrecto, líneas vacías, etc. |
|  **Tooltips** | Ayuda visual en todos los botones de la interfaz. |
|  **Barra de estado** | Muestra el estado actual (cargando, detectando, calculando, etc.). |

---

##  Requisitos técnicos

### Software necesario

|    Herramienta   |        Versión       |                   Nota                        |
| ---------------- |----------------------|-----------------------------------------------|
|      **JDK**     |      17 o mayor      | [Descargar de Adoptium](https://adoptium.net/) |
|  **JavaFX SDK**  | 21.0.12 o compatible |[Descargar de Gluon](https://gluonhq.com/products/javafx/) |
|   **VS Code**    |    Última versión    | Con Extension Pack for Java instalado |
|**Git (opcional)**|   Cualquier versión  |       Para control de versiones       |

### Estructura de carpetas del proyecto

DAGMaster/
├── .vscode/                          # Configuración de VS Code
│   ├── launch.json                   # Configuración para ejecutar con JavaFX
│   └── settings.json                 # Configuración del classpath (librerías)
│
├── src/                              # Código fuente
│   ├── DetectorCiclos.class          # (generado por compilación)
│   ├── DetectorCiclos.java           # Algoritmo DFS con ruta de ciclo
│   ├── GrafoDependencias.class       # (generado por compilación)
│   ├── GrafoDependencias.java        # Modelo del grafo (HashMap)
│   ├── InterfazDagmaster.class       # (generado por compilación)
│   ├── InterfazDagmaster.java        # Interfaz gráfica con JavaFX
│   ├── LectorArchivo.class           # (generado por compilación)
│   ├── LectorArchivo.java            # Lectura de archivos con comentarios
│   ├── OrdenTopologico.class         # (generado por compilación)
│   └── OrdenTopologico.java          # Algoritmo de Kahn
│
├── archivo_vacio.txt                 # Archivo vacío (solo comentarios)
├── auto_dependencia.txt              # Prueba de auto-dependencia (A: A)
├── cadena_larga.txt                  # Cadena larga de dependencias (A→B→...→F)
├── ciclo_largo.txt                   # Ciclo de 3 nodos (X→Y→Z→X)
├── ciclo_simple.txt                  # Ciclo simple (A→B→A)
├── dependencias_sin_ciclo.txt        # Grafo sin ciclos (DAG)
├── dependencias.txt                  # Grafo con ciclo (compilar→guardar→compilar)
├── grafo_con_comentarios.txt         # Archivo con comentarios (#) y líneas vacías
├── multiples_dependencias.txt        # Tareas con muchas dependencias
├── tareas_con_espacios.txt           # Nombres de tareas con espacios
└── README.md                         # Este archivo


---

## 🚀 Instalación y configuración

### 1. Clonar o descargar el proyecto

```bash
git clone https://github.com/tu-usuario/DAGMaster.git
# O descarga el ZIP y extrae en tu computadora

### 2. Configurar JavaFX en VS Code
2.1. Descargar JavaFX SDK

Descarga el SDK desde Gluon y descomprímelo en una ubicación permanente. Por ejemplo:

C:\Users\House\Desktop\java\javafx-sdk-21.0.12

2.2. Configurar launch.json

Crea (o edita) el archivo .vscode/launch.json con el siguiente contenido, ajustando la ruta a tu SDK de JavaFX:

{
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "Launch InterfazDagmaster",
            "request": "launch",
            "mainClass": "InterfazDagmaster",
            "vmArgs": "--module-path \"C:\\Users\\House\\Desktop\\java\\javafx-sdk-21.0.12\\lib\" --add-modules javafx.controls,javafx.fxml"
        }
    ]
}

2.3. Agregar librerías al classpath (opcional, para eliminar errores en el editor)

Crea o edita .vscode/settings.json:

{
    "java.project.referencedLibraries": [
        "C:/Users/House/Desktop/java/javafx-sdk-21.0.12/lib/**/*.jar"
    ]
}

### 3. Compilar y ejecutar
Desde la terminal (PowerShell o CMD)

Navega a la carpeta raíz del proyecto y ejecuta:

# Compilar
javac --module-path "C:\Users\House\Desktop\java\javafx-sdk-21.0.12\lib" --add-modules javafx.controls,javafx.fxml src/*.java

# Ejecutar (desde la carpeta src)
cd src
java --module-path "C:\Users\House\Desktop\java\javafx-sdk-21.0.12\lib" --add-modules javafx.controls,javafx.fxml InterfazDagmaster

Desde VS Code (con un clic)

 1. Abre InterfazDagmaster.java.

 2. Presiona F5 (o haz clic en el botón verde "Run" en la barra superior).

 3. La interfaz gráfica de DAGmaster debería abrirse

Guía de uso
1. Cargar un archivo

    Haz clic en "Seleccionar archivo".

    Navega y selecciona un archivo .txt con el formato adecuado.

    El grafo se cargará automáticamente y se mostrará en el área de texto.

2. Detectar ciclos

    Haz clic en "Detectar ciclos".

    Si hay ciclo, se mostrará la ruta exacta (ej. compilar → guardar → compilar).

    Si no hay ciclo, se mostrará un mensaje verde: "No se encontraron ciclos".

3. Calcular orden topológico

    Solo si no hay ciclos, haz clic en "Calcular orden".

    Se mostrará la lista numerada de tareas en orden de ejecución.

    El orden se guarda en memoria para poder exportarlo.

4. Exportar orden

    Haz clic en "Exportar orden".

    Selecciona la ubicación y el nombre del archivo.

    El archivo contendrá el orden con metadatos (fecha, archivo origen, total de tareas).

5. Exportar grafo a DOT

    Haz clic en "Exportar grafo".

    Guarda el archivo .dot.

    Puedes visualizarlo con Graphviz o en visualizadores en línea.

6. Limpiar

    Haz clic en "Limpiar" para borrar el área de resultados.

Formato del archivo de entrada

Formato básico

tarea: dependencia1 dependencia2 dependencia3 ...

    Separador: : (dos puntos) entre la tarea y sus dependencias.

    Separador de dependencias: espacios (uno o más).

    Comentarios: líneas que empiezan con # se ignoran.

    Líneas vacías: se ignoran automáticamente.

Ejemplos de archivos de prueba

dependencias_sin_ciclo.txt (sin ciclo)

# Este es un comentario
compilar: editar guardar
ejecutar: compilar
editar:
guardar:           # Sin dependencias
documentar: compilar
limpiar:

Salida esperada:

[OK] No se encontraron ciclos. El grafo es un DAG.

[ORDEN] ORDEN TOPOLÓGICO DE EJECUCIÓN:
1. editar
2. guardar
3. compilar
4. documentar
5. limpiar
6. ejecutar

Estructura del código

Archivo	                Descripción
GrafoDependencias.java	Modelo del grafo: HashMap<String, List<String>> con métodos para           agregar tareas y dependencias.
LectorArchivo.java	    Lee el archivo .txt, soporta comentarios # y auto-dependencia.
DetectorCiclos.java	    Algoritmo DFS con colores (0,1,2). Devuelve la ruta del ciclo si existe.
OrdenTopologico.java	Algoritmo de Kahn para calcular el orden topológico.
InterfazDagmaster.java	Interfaz gráfica con JavaFX: botones, colores, barra de estado, exportaciones.

Tecnologías utilizadas
Tecnología	Uso
Java 17+	Lenguaje principal. Orientación a objetos, colecciones (HashMap, List), manejo de archivos (java.io).
JavaFX 21	Interfaz gráfica moderna con CSS integrado. Componentes: TextFlow, ScrollPane, FileChooser, Tooltip.
VS          Code Entorno de desarrollo con Extension Pack for Java.
Git	        Control de versiones y colaboración en equipo.

Licencia

Este proyecto fue desarrollado con fines académicos para la materia Programación No Numérica I en el I.U.P "Santiago Mariño". Su uso es libre para fines educativos.

Contacto

    Luigi Zamora: luigizamora0@gmail.com

    Rodolfo Castro: rodolfocastro1106@gmail.com

    Angel Tomasini: toangel.08@gmail.com

¡DAGmaster – Dependencias resueltas, orden garantizado! 