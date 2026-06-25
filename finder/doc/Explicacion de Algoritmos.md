# Explicación de los algoritmos

> Disclaimer: El texto generado a continuacion ha sido transportado desde las notas generadas sobre una tableta digital, por lo tanto pueden haber detalles que no cuadren del todo al faltar una referencia visual.

### Algoritmos de búsqueda en grafos, árboles y laberintos

Los algoritmos de búsqueda (*Search Algorithms*) son técnicas utilizadas para recorrer estructuras de datos con el objetivo de encontrar un elemento, una ruta o una solución a un problema determinado.

Aunque existen aplicaciones simples sobre arreglos o bases de datos, estos algoritmos adquieren especial relevancia al trabajar con estructuras más complejas como grafos, árboles y laberintos, donde existen múltiples caminos posibles entre un punto de inicio y un objetivo.

La capacidad de recorrer estas estructuras de manera eficiente es fundamental en diversas áreas de la computación, entre ellas:

* Inteligencia Artificial.
* Sistemas de navegación y rutas.
* Videojuegos.
* Redes computacionales.
* Optimización de procesos.

Para este proyecto se estudiaron e implementaron cuatro algoritmos clásicos de búsqueda:

| Algoritmo | Nombre               |
| --------- | -------------------- |
| BFS       | Breadth-First Search |
| DFS       | Depth-First Search   |
| UCS       | Uniform Cost Search  |
| A*        | A-Star Search        |

---

## Breadth-First Search (BFS)

El algoritmo **Breadth-First Search (Búsqueda en Anchura)** tiene como objetivo explorar un grafo nivel por nivel.

Su estrategia consiste en visitar primero todos los nodos vecinos del nodo actual antes de avanzar hacia niveles más profundos. Para ello utiliza una estructura de datos de tipo **cola (FIFO)**.

En un laberinto, BFS puede imaginarse como una onda que se expande desde la posición inicial hacia todas las direcciones posibles hasta alcanzar el objetivo.

### Funcionamiento general

1. Marcar el nodo inicial como visitado.
2. Insertarlo en una cola.
3. Mientras la cola no esté vacía:

   * Extraer el primer nodo.
   * Procesarlo.
   * Agregar a la cola todos sus vecinos no visitados.

### Ventajas

* Garantiza encontrar el camino con menor cantidad de pasos en grafos no ponderados.
* Fácil de implementar.

### Desventajas

* Consume bastante memoria en grafos grandes.
* Puede explorar muchos nodos innecesarios antes de llegar al objetivo.

### Complejidad

* Tiempo: **O(V + E)**
* Memoria: **O(V)**

Donde:

* **V** representa la cantidad de vértices.
* **E** representa la cantidad de aristas.

---

## Depth-First Search (DFS)

El algoritmo **Depth-First Search (Búsqueda en Profundidad)** explora un camino completo antes de retroceder y probar rutas alternativas.

A diferencia de BFS, DFS prioriza avanzar hacia los niveles más profundos posibles del grafo antes de considerar otros caminos.

Para ello utiliza una **pila (Stack)** o una implementación recursiva.

### Funcionamiento general

1. Marcar el nodo actual como visitado.
2. Seleccionar un vecino no visitado.
3. Repetir el proceso de forma recursiva.
4. Si no existen más vecinos disponibles, retroceder al nodo anterior.

### Ventajas

* Requiere menos memoria que BFS.
* Resulta útil para exploraciones completas del grafo.

### Desventajas

* No garantiza encontrar el camino más corto.
* Puede recorrer caminos muy largos antes de encontrar una solución.

### Complejidad

* Tiempo: **O(V + E)**
* Memoria: **O(V)**

---

## Uniform Cost Search (UCS)

El algoritmo **Uniform Cost Search (Búsqueda de Costo Uniforme)** está diseñado para trabajar con grafos ponderados, es decir, grafos donde cada arista posee un costo asociado.

Mientras BFS minimiza la cantidad de pasos, UCS busca minimizar el costo total acumulado del recorrido.

Para lograrlo utiliza una **cola de prioridad**, expandiendo siempre el nodo cuyo costo acumulado desde el origen sea menor.

### Funcionamiento general

1. Insertar el nodo inicial con costo 0.
2. Seleccionar el nodo con menor costo acumulado.
3. Expandir sus vecinos.
4. Actualizar los costos cuando se encuentre un camino más económico.
5. Repetir hasta alcanzar el objetivo.

### Ventajas

* Garantiza encontrar el camino de menor costo.
* Funciona correctamente con pesos distintos en las aristas.

### Desventajas

* Puede explorar una gran cantidad de nodos.
* Es más costoso computacionalmente que BFS o DFS.

### Complejidad

En el peor caso:

* Tiempo: **O(E log V)**
* Memoria: **O(V)**

---

## A* (A-Star Search)

El algoritmo **A*** es una extensión de UCS que incorpora información adicional para dirigir la búsqueda hacia el objetivo de forma más inteligente.

Además del costo recorrido hasta el momento, A* utiliza una **heurística**, es decir, una estimación del costo restante hasta el nodo objetivo.

La función principal utilizada por A* es:

```text
f(n) = g(n) + h(n)
```

Donde:

* **g(n)**: costo real desde el nodo inicial hasta el nodo actual.
* **h(n)**: estimación del costo desde el nodo actual hasta el objetivo.
* **f(n)**: costo total estimado del camino.

La búsqueda expande siempre el nodo con menor valor de **f(n)**.

### Intuición

Si UCS se pregunta:

> "¿Cuál es el camino más barato que he recorrido hasta ahora?"

A* se pregunta:

> "¿Cuál parece ser el camino más prometedor considerando lo que ya recorrí y lo que falta por recorrer?"

Gracias a esto suele encontrar soluciones óptimas explorando muchos menos nodos que UCS.

### Ventajas

* Encuentra caminos óptimos cuando la heurística es admisible.
* Generalmente explora menos nodos que UCS.
* Es uno de los algoritmos más utilizados para búsqueda de rutas.

### Desventajas

* Requiere definir una heurística adecuada.
* Una mala heurística puede reducir significativamente su rendimiento.

### Complejidad

Depende de la calidad de la heurística utilizada.

En el peor caso:

* Tiempo: **O(E log V)**
* Memoria: **O(V)**

Si la heurística es igual a cero:

```text
h(n) = 0
```

entonces A* se comporta exactamente igual que UCS.

---

## Comparación general

| Algoritmo | Usa pesos | Garantiza mejor camino             | Estructura principal |
| --------- | --------- | ---------------------------------- | -------------------- |
| BFS       | No        | Sí (menor cantidad de pasos)       | Cola                 |
| DFS       | No        | No                                 | Pila                 |
| UCS       | Sí        | Sí (menor costo)                   | Cola de prioridad    |
| A*        | Sí        | Sí (si la heurística es admisible) | Cola de prioridad    |

Cada algoritmo presenta ventajas particulares dependiendo del problema a resolver. Mientras BFS y DFS son adecuados para búsquedas generales, UCS y A* resultan más apropiados cuando los costos de desplazamiento son relevantes y se busca una solución óptima.
