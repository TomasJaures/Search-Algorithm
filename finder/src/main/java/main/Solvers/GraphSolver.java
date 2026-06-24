package main.Solvers;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;



import main.util.Graphs;
import main.util.Node;
import main.util.Edge;

public class GraphSolver {
 

    public static final int startedNode = 0; //A
    public static final int finalNode = 5; //F

    public static void run(Path path, int flag){
        //[0] BFS
        //[1] DFS
        //[2] UCS
        //[3] A*

        
        
        System.out.println("Algorithm: ");
        /*
        if (flag == 1) bfs();
        if (flag == 2) dfs();
        if (flag == 3) ucs();
        if (flag == 4) A(); //A*
         */
    }

    public static void bfs() {
        int[][] graph = Graphs.graph1; //Grafo sin adyacencias

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
        int[][] graph = Graphs.graph1; //Grafo sin adyacencias

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

    public static void ucs() {
        Node[] graph = Graphs.getGraph1WithAdjacencies(); //Grafo CON adyacencias
        //{a, b, c, d, e, f, g}
        Node source = graph[0]; //A
        Node goal = graph[6]; //B
        
        source.pathCost = 0;
        
        PriorityQueue<Node> queue = new PriorityQueue<>(20, new Comparator<Node>() {
            @Override
            public int compare(Node i, Node j) {
                return Double.compare(i.pathCost, j.pathCost);
            }
        });

        queue.add(source);
        Set<Node> explored = new HashSet<>();

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            System.out.println("Current: " + current.value);
            explored.add(current);

            if (current.value.equals(goal.value)) {
                break; 
            }

            for (Edge e : current.adjacencies) {
                Node child = e.target;
                double cost = e.cost;
                double newPathCost = current.pathCost + cost;
                System.out.println("newPathCost: " + newPathCost);

                
                if (!explored.contains(child) && !queue.contains(child)) {
                    child.pathCost = newPathCost;
                    child.parent = current;
                    queue.add(child);
                } 
                
                else if (queue.contains(child) && newPathCost < child.pathCost) {
                    child.parent = current;
                    child.pathCost = newPathCost;

                    // Reodernamiento
                    queue.remove(child);
                    queue.add(child);
                }
            }
        }

        
        List<Node> path = new ArrayList<>();
        for (Node node = goal; node != null; node = node.parent) {
            path.add(node);
        }
        Collections.reverse(path);
        
        System.out.println("Path: " + path);
    }

    public static void A(){

    }

}
