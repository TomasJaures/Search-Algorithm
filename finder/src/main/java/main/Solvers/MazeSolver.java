package main.Solvers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class MazeSolver {

    public static final int[][] maze = {

    //   0  1  2  3  4  5  6  7
        {0, 0, 1, 0, 0, 0, 0, 0}, // 0  
        {0, 0, 1, 0, 0, 0, 0, 0}, // 1 
        {0, 0, 0, 0, 0, 1, 0, 0}, // 2
        {0, 0, 1, 0, 0, 1, 0, 0}, // 3
        {0, 0, 1, 0, 0, 1, 0, 0}, // 4
        {0, 0, 1, 0, 0, 1, 0, 0}, // 5
        {0, 0, 1, 0, 0, 1, 0, 0}, // 6
        {0, 0, 1, 0, 0, 1, 0, 0}  // 7
    };

    static class Pos {
        public int x, y;
        Pos(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    
    public static final Pos startPoint = new Pos(0, 0);
    public static final Pos finalPoint = new Pos(7, 7);

    public static void run(int flag){
        //asd
        if (flag == 1) bfs();
        if (flag == 2) dfs();
    }

    public static void bfs(){
        int rowsLen = maze.length;
        int colsLen = maze[0].length;

        Queue<Pos> q = new LinkedList<>();

        boolean[][] confirmed = new boolean[rowsLen][colsLen];
        Pos[][] parent = new Pos[rowsLen][colsLen];

        Pos current = startPoint;
        q.offer(current);
        confirmed[startPoint.x][startPoint.y] = true;

        while (!q.isEmpty()) {
            current = q.poll();
            

            if (current.x == finalPoint.x && current.y == finalPoint.y) {
                break; //Pos final
            }


            //Para verificar Arriba, Abajo, Izq y Der...
            int[] dX = {-1, 1, 0, 0}; //fila
            int[] dY = {0, 0, -1, 1}; //columna

            for (int i = 0; i < 4; i++) {
                int newX = current.x + dX[i];
                int newY = current.y + dY[i];

                if (newX >= 0 && newX < rowsLen &&
                    newY >= 0 && newY < colsLen &&
                    maze[newX][newY] == 0 &&
                    !confirmed[newX][newY]) {

                    confirmed[newX][newY] = true;
                    parent[newX][newY] = current;
                    q.offer(new Pos(newX, newY));
                }
            }
            
        }

        List<Pos> path = new ArrayList<>();

        for (Pos p = new Pos(finalPoint.x, finalPoint.y);
            p != null;
            p = parent[p.x][p.y]) {

            path.add(p);
        }

        Collections.reverse(path);

        System.out.println("Camino:");
        for (Pos p : path) {
            System.out.println("(" + p.y + ", " + p.x + ")"); //Se voltean las coords para cuadrar con la matriz visual de arriba
        }

    }

    public static void dfs() {
        int rowsLen = maze.length;
        int colsLen = maze[0].length;

        Stack<Pos> stack = new Stack<>();

        boolean[][] confirmed = new boolean[rowsLen][colsLen];
        Pos[][] parent = new Pos[rowsLen][colsLen];

        stack.push(startPoint);
        confirmed[startPoint.x][startPoint.y] = true;

        int[] dX = {-1, 1, 0, 0};
        int[] dY = {0, 0, -1, 1};

        while (!stack.isEmpty()) {

            Pos current = stack.pop();

            if (current.x == finalPoint.x &&
                current.y == finalPoint.y) {
                break;
            }

            for (int i = 3; i >= 0; i--) {

                int newX = current.x + dX[i];
                int newY = current.y + dY[i];

                if (newX >= 0 && newX < rowsLen &&
                    newY >= 0 && newY < colsLen &&
                    maze[newX][newY] == 0 &&
                    !confirmed[newX][newY]) {

                    confirmed[newX][newY] = true;
                    parent[newX][newY] = current;

                    stack.push(new Pos(newX, newY));
                }
            }
        }

        if (!confirmed[finalPoint.x][finalPoint.y]) {
            System.out.println("No existe camino.");
            return;
        }

        List<Pos> path = new ArrayList<>();

        for (Pos p = new Pos(finalPoint.x, finalPoint.y);
            p != null;
            p = parent[p.x][p.y]) {

            path.add(p);
        }

        Collections.reverse(path);

        System.out.println("Camino:");
        for (Pos p : path) {
            System.out.println("(" + p.y + ", " + p.x + ")");
        }
    }
}
