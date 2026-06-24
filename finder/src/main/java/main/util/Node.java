package main.util;

public class Node {
    public final String value;
    public double pathCost;
    public Edge[] adjacencies;
    public Node parent;

    public Node(String val) {
        value = val;
    }

    @Override
    public String toString() {
        return value;
    }
}
