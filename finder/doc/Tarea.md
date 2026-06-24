# Tarea:

Implementación y Visualización de Algoritmos de Búsqueda

## Objetivo
Implementar y comparar los algoritmos BFS, DFS, UCS y A* sobre dos escenarios: grafos/árboles y laberintos representados mediante matrices.

## Descripción general
El programa deberá permitir seleccionar el tipo de escenario (grafo o laberinto), el algoritmo de búsqueda, el punto inicial y el punto objetivo. Se deberá mostrar el orden de visita, el camino final y una visualización del recorrido.

### Parte 1: Búsqueda en grafos o árboles
El grafo podrá definirse mediante una estructura predefinida en el código o ser leído desde un archivo CSV usando lista o matriz de adyacencia.

### Parte 2: Búsqueda en laberintos
El laberinto será una matriz donde 0 representa una celda libre y 1 una barrera. El usuario definirá una posición inicial y una posición objetivo.

### Requisitos funcionales
Implementar BFS, DFS, UCS y A*. Permitir selección del algoritmo, nodo inicial y final, mostrar visitados, camino encontrado, costo (cuando corresponda) y visualización gráfica.

### Visualización
Mostrar el recorrido mediante una representación textual o gráfica. Se recomienda utilizar matplotlib para resaltar barreras, nodos visitados y camino final.

### Consideraciones
BFS debe explorar por niveles; DFS en profundidad; UCS usando cola de prioridad y costo acumulado; A* usando f(n)=g(n)+h(n), recomendando distancia Manhattan para laberintos.

### Entregables
Código fuente, archivos de prueba, capturas de ejecución e informe breve explicando las decisiones de diseño y la comparación de algoritmos.

### Resultado esperado
Comprender y comparar el comportamiento de BFS, DFS, UCS y A* en términos de costo, cantidad de nodos visitados y calidad de las soluciones encontradas.