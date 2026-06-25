# INFORME

## Explicacion general del codigo

### ``Main.java``

Clase "principal" del codigo, no hay mucho que destacar en general debido a que la clase no realiza ninguna logica demasiado compleja, aqui se maneja los menus de inicio, y se llaman a las diversas clases del codigo.

Ademas cuenta con las carpetas donde van los CSV

```java
public final static String graphFilesPath = "src\\main\\resources\\CSVs\\Graphs";
public final static String mazeFilesPath = "src\\main\\resources\\CSVs\\Mazes";
```

Lo que mas se podria destacar de la clase es la lectura de los archivos, pero no demasiado mas.

### ``util/CSV/CsvSolver.java``

Clase "final" dedicada a la lectura y adaptacion a formato de los archivos CSV.

Es gracias a esta clase que los archivos CSV tienen la posibilidad de leerse.

cuenta con dos metodos:

**getGraphData()**: Retorna una matriz de enteros Strings al pasarle un archivo `.csv`, siendo la primera fila los headers y posteriores los datos como tal, es usado para poder procesar y representar un grafo.
```java
public static String[][] getGraphData(Path path) throws IOException {...}
```


**getMazeData()**: Retorna una matriz de enteros de 1's y 0's al pasarle un archivo `.csv`, es usado para poder procesar y representar un laberinto.
```java
public static int[][] getMazeData(Path path) throws IOException {...}
```

Es usada por `MazeSolver` y `GraphSolver.

### ``GraphSolver.java``

Clase "final" usada para realizar todo el proceso de cargar una Matriz de String pasada por ``util/CSV/CsvSolver.java`` y realizar los algoritmos BFS, DFS, UCS y A* para que las clases ``swing/UINode``, ``swing/GraphUI`` y ``swing/GraphVisual`` puedan realizar las representaciones visuales.

Esta clase se centra en acomodar los algoritmos especificamente para los grafos.

**BFS**
```java
public static List<Node> bfs(List<Node> graph, Node start, Node end, Set<Node> visited) {
    Queue<Node> queue = new LinkedList<>(); //Cola
    queue.add(start);
    visited.add(start); //Conjunto de visitados

    boolean found = false;
    while (!queue.isEmpty()) {
        Node current = queue.poll(); //Se saca el nodo actual de la cola
        if (current.equals(end)) {
            found = true; //Comprobacion de si nodo actual es el objetivo
            break;
        }
        if (current.adjacencies == null) continue; //Comprobacion de que el nodo actual tiene vecinos
        //Añadiendo vecinos a la cola si no estan ya previamente visitados
        for (Edge e : current.adjacencies) {
            if (!visited.contains(e.target)) {
                visited.add(e.target);
                e.target.parent = current;
                queue.add(e.target);
            }
        }
    }
    return reconstructPath(end, found, "BFS");
}
```

Lo que hace la clase es usar al maximo la estructura de "Cola" y un conjunto que llamaremos "visited"

La cola se encarga de ir guardando los nodos vecinos del nodo actual, mientras que el conjunto se encarga de verificar que no se agregue un nodo ya visitado a la cola nuevamente.

Para este caso, se esta usando una estructura personalizada llamada "Node" y "Edge" que representan a los nodos y las aristas.

`Node.java`
```java
public class Node {
    public final String data;
    public double pathCost; //Costo acumulado (Usado en UCS)
    public Edge[] adjacencies; //Vecinos del nodo
    public Node parent; //Padre del nodo (Es usado para mostrar el camino)

    public Node(String data) {
        this.data = data;
    }

    //...
}
```

`Edge.java`

```java
public class Edge {
    public final double cost; //Costo del camino
    public final Node target; //Nodo que esta apuntando

    public Edge(Node targetNode, double costVal) {
        cost = costVal;
        target = targetNode;
    }
}
```

Cada nodo tiene un arreglo de Edge's, que representan los vecinos del nodo.

Pero lo realmente intersante ocurre con la siguiente estructura, pues en esta seccion, se indaga en los Nodos vecinos del nodo actual, confirmando que estos mismo no se encuentren visitados previamente, y si NO lo estan, entonces se agregar al conjunto de visitados y se agregaran a la cola, de esta manera llenandola de nuevo y ejecutando nuevamente el ciclo, hasta que se encuentre el objetivo o se llene todo el grafo
```java

while (!queue.isEmpty()){
    //...
    for (Edge e : current.adjacencies) {
        if (!visited.contains(e.target)) {
            visited.add(e.target);
            e.target.parent = current;
            queue.add(e.target);
        }
    }
}

```

**DFS**
```java
public static List<Node> dfs(List<Node> graph, Node start, Node end, Set<Node> visited) {
        Stack<Node> stack = new Stack<>(); //Pila
        stack.push(start);

        boolean found = false;
        while (!stack.isEmpty()) {
            Node current = stack.pop(); //Se saca el nodo actual de la pila
            if (!visited.contains(current)) {
                visited.add(current);
                if (current.equals(end)) {
                    found = true;
                    break;
                }
                if (current.adjacencies == null) continue;
                //Añadiendo vecinos a la pila si no estan ya previamente visitados
                for (int i = current.adjacencies.length - 1; i >= 0; i--) {
                    Edge e = current.adjacencies[i];
                    if (!visited.contains(e.target)) {
                        e.target.parent = current;
                        stack.push(e.target);
                    }
                }
            }
        }
        return reconstructPath(end, found, "DFS");
    }
```

El DFS como tal es bastante parecido al BFS, no tanto en su comportamiento si no mas bien por su estructura.

Funciona relativamente parecido a BFS, con la diferencia fundamental que con este algoritmo se hace uso de un **Stack**.

Es gracias a este Stack que el algoritmo procede con su caracteristico comportamiento, pues a diferencia de BFS que se dedica a "ampliarse", este se dedica a profundizar hasta chocarse con algo.

El Stack permite que los datos que se vayan agregando tengan un comportamiento LIFO, lo que permite que cuando se agreguen al stack, estos no sean revisados hasta que nuevos vecinos dejen de ir apareciendo, quedandose por lo general bastante "abajo" de esta pila, y permitiendo al algoritmo "volver sobre sus pasos".

**UCS**
```java
public static List<Node> ucs(List<Node> graph, Node start, Node end, Set<Node> visited) {
        //La cola de prioridad tomara en cuenta el coste minimo
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(n -> n.pathCost));
        start.pathCost = 0; //Variable para costo acumulado
        queue.add(start);

        boolean found = false;
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (visited.contains(current)) continue;
            visited.add(current);

            if (current.equals(end)) {
                found = true; //Comprobacion de si nodo actual es el objetivo
                break;
            }

            if (current.adjacencies == null) continue;
            //Se hacen las comprobaciones de los vecinos:
            for (Edge e : current.adjacencies) {
                double newCost = current.pathCost + e.cost;
                if (newCost < e.target.pathCost) {
                    e.target.pathCost = newCost; //Se guarda el coste acumulado
                    e.target.parent = current;
                    queue.add(e.target);
                }
            }
        }
        return reconstructPath(end, found, "UCS");
    }
```

El UCS usa como tal una PriorityQueue (PQ) con tal de poder definir el menor coste posible, esta PQ tiene como objetivo definir que aristas se deberian de comprobar primero (Las menos costozas), pero NO descarta posibles nodos con aristas mas costosas.

Esto se hace en caso de que si la arista que se creia menos costoza en primer lugar, termina teniendo un costo **acumulado** MAYOR a la arista que en primer lugar se consideraba como "mas costoza".

Lo bueno de este algoritmo, es que considera estos casos y trata de encontrar no solo el camino mas corto como tal, si no el menos costozo, una diferencia clave con los algoritmos BFS y DFS que ni siquiera tienen en cuenta este dato en primer lugar.

**A***
```java
public static List<Node> aStar(List<Node> graph, Node start, Node end, Set<Node> visited) {
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(n -> n.pathCost));
        start.pathCost = 0;
        queue.add(start);

        boolean found = false;
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (visited.contains(current)) continue;
            visited.add(current);

            if (current.equals(end)) {
                found = true;
                break;
            }

            if (current.adjacencies == null) continue;
            for (Edge e : current.adjacencies) {
                double gScore = current.pathCost + e.cost;
                double hScore = 0.0; 
                double fScore = gScore + hScore;

                if (fScore < e.target.pathCost) {
                    e.target.pathCost = fScore;
                    e.target.parent = current;
                    queue.add(e.target);
                }
            }
        }
        return reconstructPath(end, found, "ASTAR");
    }
```

A* (A-Star) es una extensión de UCS. Ambos utilizan una PriorityQueue y consideran el costo acumulado del camino, pero A* agrega un segundo factor: una heurística, es decir, una estimación de qué tan lejos está un nodo del objetivo.

En general y a diferencia de UCS, A* se pregunta ademas por:

> "¿Cuál es el camino más prometedor considerando lo que ya recorrí y lo que creo que falta por recorrer?"

Para ello, es usado una funcion heurisitica implementado bajo el contexto de A*

Siendo esta misma la siguiente:

$$
f(n)=g(n)+h(n)
$$

Donde:
- g(n): costo acumulado desde el inicio hasta el nodo actual.
- h(n): estimación del costo restante hasta el objetivo.
- f(n): prioridad del nodo.

Podemos ver a esta misma, como una especie de campo inclinado al punto final, como una especie de gravedad a este mismo.

### ``MazeSolver.java``

Clase "final" usada para realizar todo el proceso de cargar una Matriz de String pasada por ``util/CSV/CsvSolver.java`` y realizar los algoritmos BFS, DFS, UCS y A* para que la clase ``swing/MazeGrid`` pueda realizar las representaciones visuales.

Esta clase se centra en acomodar los algoritmos especificamente para los laberintos.

Los algoritmos de esta clase tienen la misma logica que de GraphSolver, con la diferencia que estan adaptados especificamente para laberitnos

Por ejemplo, en vez de trabajar con NODOS, se trabaja con Posiciones, siendo esta la estructura:

```java
static class Pos {
        public int x, y;
        Pos(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
```

Esto en principio no afecta especialmente a la mayoria de algoritmos, a excepcion de "UCS". Pues debido a que este algoritmo toma en cuenta el costo de las aristas, pierde todo su significado al usarlo en un laberinto, pues cada paso que se da en este mismo, tiene un coste uniforme.

Sin embargo, debido a su naturaleza, cuando todos los "nodos", tienen una arista con un nodo uniforme, este curiosamente se comporta como un algoritmo "BFS", es facil darse cuenta de esto debido a que este usa una PriorityQueue que al no tener nada con que comparar se comporta como una cola comun y corriente.

Es en base a este motivo, que se decidio por ignorar el algoritmo y simplemente usar el BFS de toda la vida.

```java
public static void ucs(MazeGrid gui){
    bfs(gui); //UCS en grafos con adyacencias de igual nivel, es igual a BFS
}
```

Como extra, esta clase tambien es mas compatible con las clases especializadas en la GUI, por lo tanto no es necesario realizar demasiadas estructuras.

### ``swing/..``

Para finalizar, tenemos las clases relacionadas a JAVA SWING, estas clases son las encargadas de representar visualmente los algoritmos ya vistos, ya sea en grafos o en laberintos.

> **Clase GraphUI**

Hereda de JFrame. Funciona como la ventana principal y el panel de control para la simulación de grafos de nodos interconectados.

Responsabilidades:

Construir la barra de herramientas superior (controlPanel) para capturar los nodos de inicio/fin y el algoritmo seleccionado.

Validar la existencia de los nodos ingresados por el usuario dentro de la lista de nodos activos (currentNodes).

Resetear los metadatos de los nodos (parent, pathCost) antes de cada ejecución para evitar contaminación de búsquedas previas.

Delegar el cálculo del camino a la clase estática GraphSolver e iniciar la animación del resultado.

> **Clase GraphVisual**

Hereda de JPanel. Es el lienzo encargado del renderizado y cálculo físico/dinámico del grafo en tiempo real.

Motor de Física Integrado: Implementa un modelo de fuerzas (Force-Directed Graph) mediante un Timer (ejecutado cada 16ms):

Repulsión: Los nodos se repelen inversamente al cuadrado de su distancia para evitar solapamientos.

Atracción: Los nodos conectados por aristas se atraen de forma lineal (Ley de Hooke).

Amortiguación (DAMPING): Reduce gradualmente la velocidad para estabilizar el sistema visual.

Sistema de Animación: Utiliza un segundo temporizador secuencial (300ms) para pintar de manera progresiva los nodos explorados (CYAN) y, finalmente, destacar el camino óptimo hallado (ORANGE).

> **Clase MazeGrid**

Hereda de JFrame. Proporciona una interfaz cuadriculada (GridLayout) para la resolución visual de laberintos matriciales.

Componentes Principales:

Entrada Matricial: Representa caminos libres en color gris oscuro (0) y obstáculos/paredes en negro (1).

Asignación de Coordenadas: Permite configurar dinámicamente los puntos de origen (verde) y destino (rojo) mediante parsing de texto (x, y).

Concurrencia: Ejecuta las llamadas a MazeSolver dentro de un hilo secundario (Thread). Esto evita que el bucle de renderizado de la GUI (Event Dispatch Thread) se congele durante el procesamiento o los retrasos simulados del algoritmo.

Métodos de Pintura: Expone addColor para que el motor de búsqueda actualice el estado de las casillas en tiempo real.

> **Clase UINode**

Clase utilitaria de tipo Wrapper (contenedor).

Responsabilidades:

Asociar una instancia del nodo lógico de la estructura de datos (Node rawNode) con propiedades físicas bidimensionales necesarias para la GUI.

Almacenar las coordenadas de posición actuales (x, y) y los vectores de velocidad (vx, vy) calculados continuamente por el motor de física en GraphVisual.