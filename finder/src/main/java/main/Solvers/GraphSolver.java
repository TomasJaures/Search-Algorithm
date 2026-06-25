package main.Solvers;

import java.nio.file.Path;
import java.util.*;
import main.util.Node;
import main.util.CSV.CsvSolver;
import main.util.swing.GraphUI;
import main.util.Edge;

public class GraphSolver {
 
    public static String[][] csvGraph;

    public static void run(Path path){
        csvGraph = null;
        try {
            csvGraph = CsvSolver.getGraphData(path);
        } catch (Exception e) {
            System.out.println("Hubo un problema con el CSV (Asegurse que este en el formato correcto)");
            e.printStackTrace();
            return;
        }

        List<Node> nodes = getNodes(csvGraph);
        GraphUI ui = new GraphUI("Grafo");
        ui.run(nodes);
    }

    public static List<Node> getNodes(String[][] csvGraph) {
        Map<String, Node> nodeMap = new HashMap<>();
        Map<String, List<Edge>> adjacencyMap = new HashMap<>();

        for (int i = 1; i < csvGraph.length; i++) {
            String[] row = csvGraph[i];
            String sourceName = row[0];
            String targetName = row[1];
            double cost = Double.parseDouble(row[2]);

            nodeMap.putIfAbsent(sourceName, new Node(sourceName));
            nodeMap.putIfAbsent(targetName, new Node(targetName));

            Node sourceNode = nodeMap.get(sourceName);
            Node targetNode = nodeMap.get(targetName);

            //Arias de ida
            Edge forwardEdge = new Edge(targetNode, cost);
            adjacencyMap.putIfAbsent(sourceName, new ArrayList<>());
            adjacencyMap.get(sourceName).add(forwardEdge);

            //Arista de vuelta
            Edge backwardEdge = new Edge(sourceNode, cost);
            adjacencyMap.putIfAbsent(targetName, new ArrayList<>());
            adjacencyMap.get(targetName).add(backwardEdge);
        }

        for (Map.Entry<String, List<Edge>> entry : adjacencyMap.entrySet()) {
            Node node = nodeMap.get(entry.getKey());
            List<Edge> edges = entry.getValue();
            node.adjacencies = edges.toArray(new Edge[0]);
        }

        return new ArrayList<>(nodeMap.values());
    }
    
    public static List<Node> bfs(List<Node> graph, Node start, Node end, Set<Node> visited) {
        Queue<Node> queue = new LinkedList<>();
        queue.add(start);
        visited.add(start);

        boolean found = false;
        while (!queue.isEmpty()) {
            Node current = queue.poll();
            if (current.equals(end)) {
                found = true;
                break;
            }
            if (current.adjacencies == null) continue;
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

    public static List<Node> dfs(List<Node> graph, Node start, Node end, Set<Node> visited) {
        Stack<Node> stack = new Stack<>();
        stack.push(start);

        boolean found = false;
        while (!stack.isEmpty()) {
            Node current = stack.pop();
            if (!visited.contains(current)) {
                visited.add(current);
                if (current.equals(end)) {
                    found = true;
                    break;
                }
                if (current.adjacencies == null) continue;
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

    public static List<Node> ucs(List<Node> graph, Node start, Node end, Set<Node> visited) {
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
                double newCost = current.pathCost + e.cost;
                if (newCost < e.target.pathCost) {
                    e.target.pathCost = newCost;
                    e.target.parent = current;
                    queue.add(e.target);
                }
            }
        }
        return reconstructPath(end, found, "UCS");
    }

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

    private static List<Node> reconstructPath(Node end, boolean found, String algorithm) {
        List<Node> path = new ArrayList<>();
        if (!found){
            System.out.println("No se encontro el camino");
            return path;
        } else {
            //Muestra el coste solo si es UCS o ASTAR el algoritmo lo pide
            String result = (algorithm.equals("UCS") || algorithm.equals("ASTAR")) ? 
            """
            =====================
            Coste: %s        
            =====================
            """.formatted(end.pathCost) : "";
            System.out.println(result);
        }
        
        for (Node n = end; n != null; n = n.parent) {
            path.add(n);
        }
        Collections.reverse(path);
        return path;
    }
}