package main.util;

import java.util.Arrays;

public class Node {
    public final String data;
    public double pathCost;
    public Edge[] adjacencies;
    public Node parent;

    public Node(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder edgeData = new StringBuilder("[");
        if (adjacencies != null) {
            for (int i = 0; i < adjacencies.length; i++) {
                edgeData.append(adjacencies[i].target.data).append(" (cost: ").append(adjacencies[i].cost).append(")");
                if (i < adjacencies.length - 1) {
                    edgeData.append(", ");
                }
            }
        }
        edgeData.append("]");

        String parentName = (parent != null) ? parent.data : "null";

        return """
           Data: %s
           CosteCamino: %f
           Edges: %s
           Parent: %s
        """.formatted(data, pathCost, edgeData.toString(), parentName);
    }
}
