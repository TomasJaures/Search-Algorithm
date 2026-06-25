# Ejercicio de Algoritmos...

Proyecto en Java que implementa algoritmos de búsqueda (BFS, DFS, UCS y A*) aplicados a grafos y laberintos, con visualización en **Swing** y carga de datos desde archivos **CSV**.

## Requisitos:

### Preinstalaciones ya hechas:

Para ejecutar el programa, es necesario tener Maven previamente instalado, de esta manera solo bastara con ejecutar lo siguiente desde la carpeta raiz del proyecto (finder):

- Java 17+
- Maven 3.8+

### Ejecucion

Ejecutar la primera vez:
```bash
mvn compile
mvn exec:java "-Dexec.mainClass=main.Solvers.Main"
```

Segunda vez
```bash
mvn exec:java "-Dexec.mainClass=main.Solvers.Main"
```

## ¿Como usar el programa? (Manual de uso)
### Navegar menu inico

Al dar inicio al programa, el menu principal sera unicamente texto en terminal...

En este mini menu es necesario indicar el tipo de accion que queramos realizar. Ej:

```bash
========================================
Manual de uso en README.md

¿Que tipo de estructura desea indagar?
[0] : Grafo
[1] : Laberinto
========================================
```

Con este en mente... Podemos realizar la operacion escribiendo en la terminal, en este caso:

- Escribir `0` para analizar los algoritmos sobre **grafos**
- Escribir `1` para analizar los algoritmos sobre **laberintos**

Y de esta manera el codigo nos llevara a la siguiente seccion

--- 

### Cargar CSV personalizados

Una vez pasado el menu anterior, el programa preguntara sobre que tipo de archivo `.csv` se desea realizar la operacion.

```bash
========================================
Seleccione el archivo que desea aplicale el algoritmo:

[0] Graph1.csv
[1] Graph2.csv
[2] Graph3.csv
========================================
```

Lo importante de este menu, es el hecho de que nosotros podemos cargar nuestros propios CSV personalizados.

Para ello, en la carpeta `finder\src\main\resources\CSVs`, habra dos subcarpetas:

* ``Graphs\``
* ``Mazes\``

En cada carpeta hay archivos de prueba ya creados dependiendo del tipo de estructura, si hay la necesidad de insertar un archivo nuevo propio, se debe guardar en la carpeta debida con el formato adecuado.

Ejemplo: **Grafo**
```csv
Source;Target;Weight
A;B;2
A;C;5
A;D;2
B;G;3
C;D;3
C;E;4
C;G;2
D;E;4
G;F;8
```

Ejemplo: **Laberinto**
```csv
0;0;0;1;0;0;0
0;0;0;1;0;0;0
0;0;0;1;0;0;0
0;0;0;0;0;0;0
0;0;0;1;0;0;0
0;0;0;1;0;0;0
0;0;0;1;0;0;0
0;0;0;1;0;0;0
0;0;0;1;0;0;0
0;0;0;1;0;0;0
0;0;0;1;0;0;0
0;0;0;1;0;0;0
0;0;0;1;0;0;0
0;0;0;1;0;0;0
0;0;0;1;0;0;0
```

NO se recomienda crear alguno de estos archivos de manera manual, debido a que los archivos `.csv` suelen dejar caracteres invisibles que son necesarios para el funcionamiento del codigo.

Lo mas recomendable es exportar un archivo `.csv` desde Excel y guardarlo de manera directa.

Y en caso de que todo salga de manea correcta, el codigo deberia dar la opcion de elegir el nuevo archivo agregado a la carpeta indicada

### **GUI grafo**

Si hemos elegido el tipo de estructura "grafo" y elegido un archivo, entonces el codigo deberia de abrir una nueva ventana donde se pueda visualizar el grafo creado.

<img src="Imgs/GraphExample.png" width=500px>

Dentro de esta Interfaz de usuario, podemos realizar diversas acciones:

* Si queremos definir un nodo inicial y un nodo final, podemos seleccionarlo en el campo "Start Node" y "End Node".
* Si queremos definir el algoritmo de busqueda, podemos dar click en el campo "Algorithm" donde se nos desplegara las diversas opciones de algoritmos, siendo estos:
    * BFS
    * DFS
    * UCS
    * A*

Por defecto, el codigo carga las aristas con peso, sin embargo si se realiza un algoritmo donde esto no se toma en cuenta, el algoritmo hara su recorrido como si este elemento no existiera.

* Finalmente, si queremos ver la visualizacion en accion, debemos dar click en "FindPath" y nos mostrara una pequeña animacion sobre como el algoritmo va recorriendo los diversos NODOS.

Si queremos probar otra combinacion, simplemente cambiamos los campos y damos click en "Find Path" nuevamente.

**IMPORTANTEs:**
```
En la terminal saldran alguna informacion extra, siendo estos:
- El coste.
- Cantidad de nodos del camino encontrado.

En caso de ejecutar un algoritmo donde o no se considere la longitud de las aristas o el coste del grafo, no se mostrara la informacion.

Se entiende ademas que con la informacion entregada de manera visual, tambien se satisfacen requerimientos como "visualización gráfica", "camino encontrado", Etc.
```

Por utlimo, en el caso de que se quiera cambiar de grafo, sera necesario terminar la ejecucion y correr nuevamente el programa.

### **GUI Laberinto**

Si hemos elegido el tipo de estructura "laberinto" y elegido su respectivo archivo, entonces el codigo deberia de abrir una nueva ventana donde se pueda visualizar el laberinto creado.


<img src="Imgs/MazeExample.png" width=500px>

Dentro de esta Interfaz de usuario, podemos realizar las siguientes acciones:

- Tanto en el campo "Posicion Inicial" y "Posicion Final", podemos definir mediante una coordenada en que posicion se encontrar el inicio y el final (Siendo `0,0` la esquina superior izquierda). Este sigue el siguiente formato:

    - Posicion inicial: `0,20`
    - Posicion final: `20,0`

Y asignandolos dandole al boton de asignar.

- A su vez, tambien podemos definir el tipo de algoritmo que recorrera el grafo:
    - BFS
    - DFS
    - UCS
    - A*

- Finalmente, podemos iniciar la busqueda dandole al boton "Iniciar", y si queremos cambiar de campos, podemos darle al boton "Clear" para limpiar el camino creado.

```
Adicionalmente, en la terminal saldra informacion como:
- Si se ha encontrado el camino
- El tamaño del camino encontado
```