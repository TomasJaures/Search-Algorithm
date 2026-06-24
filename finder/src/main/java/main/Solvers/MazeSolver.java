package main.Solvers;
import java.awt.Color;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import main.util.CSV.CsvSolver;
import main.util.swing.MazeGrid;

public class MazeSolver {

    static class Pos {
        public int x, y;
        Pos(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    static class AStarNode implements Comparable<AStarNode> {
        int x, y;
        int g; // Costo desde el inicio
        int f; // Costo total estimado (g + h)

        AStarNode(int x, int y, int g, int f) {
            this.x = x;
            this.y = y;
            this.g = g;
            this.f = f;
        }

        @Override
        public int compareTo(AStarNode other) {
            return Integer.compare(this.f, other.f);
        }
    }

    private static int[][] csvMaze; // Laberinto del CSV
    public static int startX;
    public static int startY;
    public static int endX;
    public static int endY;

    public static void run(Path path){
        MazeGrid gui = null;
        try {
            csvMaze = CsvSolver.getMazeData(path);
            gui = new MazeGrid(csvMaze);
        } catch (Exception e) {
            //Problema con el CSV
            System.out.println("Hay un problema con el CSV");
            e.printStackTrace();
        }
        
        gui.run();
    }

    public static void bfs(MazeGrid gui) {
        if (!validateEndPoints()) return;

        int rowsLen = csvMaze.length;
        int colsLen = csvMaze[0].length;

        Queue<Pos> q = new LinkedList<>();
        boolean[][] confirmed = new boolean[rowsLen][colsLen];

        Pos[][] parent = new Pos[rowsLen][colsLen];

        Pos start = new Pos(startY, startX); 
        Pos finish = new Pos(endY, endX);

        q.offer(start);
        confirmed[start.x][start.y] = true;

        while (!q.isEmpty()) {
            Pos current = q.poll();

            //Verificar no pintar el punto inicial o el punto final
            if (!(current.x == start.x && current.y == start.y) && !(current.x == finish.x && current.y == finish.y)) {
                gui.addColor(current.y, current.x, new Color(100, 150, 255)); 
                sleep(40); 
            }

            //Terminar operacion al llegar al punto final
            if (current.x == finish.x && current.y == finish.y) {
                break;
            }

            //Verificar vecinos
            int[] dX = {-1, 1, 0, 0}; 
            int[] dY = {0, 0, -1, 1}; 
            for (int i = 0; i < 4; i++) {
                int newX = current.x + dX[i];
                int newY = current.y + dY[i];

                if (newX >= 0 && newX < rowsLen && //X no se sale de la cuadricula
                    newY >= 0 && newY < colsLen && //Y no se sale de la cuadricula
                    csvMaze[newX][newY] == 0 && //Si NO es obstaculo
                    !confirmed[newX][newY]) { //Si no esta explorado

                    confirmed[newX][newY] = true;
                    parent[newX][newY] = current;
                    q.offer(new Pos(newX, newY));
                }
            }
        }

        drawFinalPath(parent, start, finish, gui);
    }

    public static void dfs(MazeGrid gui) {
        if (!validateEndPoints()) return;

        int rowsLen = csvMaze.length;
        int colsLen = csvMaze[0].length;

        //A diferencia de BFS se necesita un Stack para tener la capacidad de "volver".
        Stack<Pos> stack = new Stack<>();
        boolean[][] confirmed = new boolean[rowsLen][colsLen];
        Pos[][] parent = new Pos[rowsLen][colsLen];

        Pos start = new Pos(startY, startX);
        Pos finish = new Pos(endY, endX);

        stack.push(start);
        confirmed[start.x][start.y] = true;

        int[] dX = {-1, 1, 0, 0};
        int[] dY = {0, 0, -1, 1};
        while (!stack.isEmpty()) {
            Pos current = stack.pop();

            if (!(current.x == start.x && current.y == start.y) && !(current.x == finish.x && current.y == finish.y)) {
                gui.addColor(current.y, current.x, new Color(180, 100, 255)); 
                sleep(40);
            }

            if (current.x == finish.x && current.y == finish.y) {
                break;
            }

            for (int i = 3; i >= 0; i--) {
                int newX = current.x + dX[i];
                int newY = current.y + dY[i];

                if (newX >= 0 && newX < rowsLen &&
                    newY >= 0 && newY < colsLen &&
                    csvMaze[newX][newY] == 0 &&
                    !confirmed[newX][newY]) {

                    confirmed[newX][newY] = true;
                    parent[newX][newY] = current;
                    stack.push(new Pos(newX, newY));
                }
            }
        }

        drawFinalPath(parent, start, finish, gui);
    }

    public static void ucs(MazeGrid gui){
        bfs(gui); //UCS en grafos con adyacencias de igual nivel, es igual a BFS
    }

    public static void aStart(MazeGrid gui) {
        if (!validateEndPoints()) return;

        int rowsLen = csvMaze.length;
        int colsLen = csvMaze[0].length;

        // Cola de prioridad que ordena automáticamente de menor a mayor 'f'
        PriorityQueue<AStarNode> pq = new PriorityQueue<>();
        
        // Matriz para guardar el camino de retorno (igual que en tu BFS)
        Pos[][] parent = new Pos[rowsLen][colsLen];
        
        // Matriz para registrar el menor costo 'g' encontrado para cada celda
        int[][] gScore = new int[rowsLen][colsLen];
        for (int[] row : gScore) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }

        Pos start = new Pos(startY, startX); 
        Pos finish = new Pos(endY, endX);

        // Inicialización del nodo de salida
        gScore[start.x][start.y] = 0;
        int initialH = Math.abs(start.x - finish.x) + Math.abs(start.y - finish.y);
        pq.offer(new AStarNode(start.x, start.y, 0, initialH));

        while (!pq.isEmpty()) {
            AStarNode current = pq.poll();

            // Si encontramos un camino más largo hacia esta misma celda que ya procesamos, lo ignoramos
            if (current.g > gScore[current.x][current.y]) {
                continue;
            }

                // Verificar no pintar el punto inicial o el punto final
            if (!(current.x == start.x && current.y == start.y) && !(current.x == finish.x && current.y == finish.y)) {
                gui.addColor(current.y, current.x, new Color(100, 150, 255)); 
                sleep(40); 
            }

            // Terminar operación al llegar al punto final
            if (current.x == finish.x && current.y == finish.y) {
                break;
            }

            // Verificar vecinos (4 direcciones)
            int[] dX = {-1, 1, 0, 0}; 
            int[] dY = {0, 0, -1, 1}; 
            for (int i = 0; i < 4; i++) {
                int newX = current.x + dX[i];
                int newY = current.y + dY[i];

                // Validar límites de la cuadrícula y que no sea un obstáculo
                if (newX >= 0 && newX < rowsLen && 
                    newY >= 0 && newY < colsLen && 
                    csvMaze[newX][newY] == 0) {
                    
                    // El costo para moverse a un vecino siempre es +1 en cuadriculas uniformes
                    int tentativeG = current.g + 1;

                    // Si encontramos un camino más corto hacia este vecino
                    if (tentativeG < gScore[newX][newY]) {
                        gScore[newX][newY] = tentativeG;
                        parent[newX][newY] = new Pos(current.x, current.y);
                        
                        // Distancia de Manhattan (Heurística)
                        int h = Math.abs(newX - finish.x) + Math.abs(newY - finish.y);
                        int f = tentativeG + h;

                        pq.offer(new AStarNode(newX, newY, tentativeG, f));
                    }
                }
            }
        }

        // Dibuja el camino final utilizando la matriz 'parent' idéntica a la de tu BFS
        drawFinalPath(parent, start, finish, gui);
    }

    private static boolean validateEndPoints() {
        if (startX == -1 || startY == -1 || endX == -1 || endY == -1) {
            System.out.println("Asigna las posiciones de inicio y final");
            return false;
        }
        return true;
    }

    private static void drawFinalPath(Pos[][] parent, Pos start, Pos finish, MazeGrid gui) {
        if (parent[finish.x][finish.y] == null) {
            System.out.println("No existe un camino");
            return;
        }

        List<Pos> path = new ArrayList<>();
        //Similar a una lista ligada por punteros
        for (Pos p = finish; p != null; p = parent[p.x][p.y]) {
            path.add(p);
        }
        Collections.reverse(path);

        System.out.println("Existe un camino");
        System.out.println("Recorrido: " + path.size());
        for (Pos p : path) {
            //Mostrarlo en la GUI
            if (!(p.x == start.x && p.y == start.y) && !(p.x == finish.x && p.y == finish.y)) {
                gui.addColor(p.y, p.x, Color.YELLOW);
                sleep(30);
            }
        }
    }

    private static void sleep(int ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}
