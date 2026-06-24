package main.Solvers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class GraphSolver {

    public static final int[][] graph = {
    //   A  B  C  D  E  F  G
        {0, 1, 1, 1, 0, 0, 0}, //A (0)
        {1, 0, 0, 0, 0, 0, 1}, //B (1)
        {1, 0, 0, 1, 1, 0, 1}, //C (2)
        {1, 0, 1, 0, 1, 0, 0}, //D (3)
        {0, 0, 1, 1, 0, 0, 0}, //E (4)
        {0, 0, 0, 0, 0, 0, 1}, //F (5)
        {0, 1, 1, 0, 0, 1, 0}, //G (6)
    };
 

    public static final int startedNode = 0; //A
    public static final int finalNode = 5; //F

    public static void run(int flag){
        System.out.println("Algorithm: ");
        if (flag == 1) bfs();
        if (flag == 2) dfs();
    }

    public static void bfs() {
        boolean[] confirmed = new boolean[graph.length];
        int[] parent = new int[graph.length];
        Arrays.fill(parent, -1);

        Queue<Integer> q = new LinkedList<>();
        StringBuilder sb = new StringBuilder();

        q.offer(startedNode);
        confirmed[startedNode] = true;

        while (!q.isEmpty()) {
            int c = q.poll();
            sb.append((char) (c + 97)).append(" ");

            if (c == finalNode) {
                break; // destino encontrdo
            }

            int[] residents = graph[c];
            for (int i = 0; i < residents.length; i++) {
                if (residents[i] == 1 && !confirmed[i]) {
                    q.offer(i);
                    confirmed[i] = true;
                    parent[i] = c;
                }
            }
        }

        System.out.println("Nodos visitados: " + sb);
        List<Integer> path = new ArrayList<>();
        int current = finalNode;

        while (current != -1) {
            path.add(current);
            current = parent[current];
        }

        Collections.reverse(path);

        System.out.print("camino: ");
        for (int node : path) {
            System.out.print((char)(node + 97) + " ");
        }
        System.out.println();
    }

    public static void dfs() {
        boolean[] visited = new boolean[graph.length];
        int[] parent = new int[graph.length];

        Arrays.fill(parent, -1);

        Stack<Integer> s = new Stack<>();

        s.push(startedNode);
        visited[startedNode] = true;

        while (!s.isEmpty()) {
            int current = s.pop();

            if (current == finalNode) {
                break;
            }

            //Al revez por ser una pila
            for (int i = graph[current].length - 1; i >= 0; i--) {
                if (graph[current][i] == 1 && !visited[i]) {
                    visited[i] = true;
                    parent[i] = current;
                    s.push(i);
                }
            }
        }

        List<Integer> path = new ArrayList<>();

        for (int current = finalNode;
            current != -1;
            current = parent[current]) {

            path.add(current);
        }

        Collections.reverse(path);

        System.out.println("Camino:");
        for (int node : path) {
            System.out.print((char)(node + 65) + " ");
        }
    }



}
