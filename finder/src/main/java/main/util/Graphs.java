package main.util;

public class Graphs {
    public static final int[][] graph1 = {
    //   A  B  C  D  E  F  G
        {0, 1, 1, 1, 0, 0, 0}, //A (0)
        {1, 0, 0, 0, 0, 0, 1}, //B (1)
        {1, 0, 0, 1, 1, 0, 1}, //C (2)
        {1, 0, 1, 0, 1, 0, 0}, //D (3)
        {0, 0, 1, 1, 0, 0, 0}, //E (4)
        {0, 0, 0, 0, 0, 0, 1}, //F (5)
        {0, 1, 1, 0, 0, 1, 0}, //G (6)
    };

    public static final int[][] graph2 = {
    //   A  B  C  D  E  F  G
        {0, 1, 1, 1, 0, 0, 0}, //A (0)
        {1, 0, 0, 0, 0, 0, 1}, //B (1)
        {1, 0, 0, 1, 1, 0, 1}, //C (2)
        {1, 0, 1, 0, 1, 0, 0}, //D (3)
        {0, 0, 1, 1, 0, 0, 0}, //E (4)
        {0, 0, 0, 0, 0, 0, 1}, //F (5)
        {0, 1, 1, 0, 0, 1, 0}, //G (6)
    };

    public static final int[][] graph3 = {
    //   A  B  C  D  E  F  G
        {0, 1, 1, 1, 0, 0, 0}, //A (0)
        {1, 0, 0, 0, 0, 0, 1}, //B (1)
        {1, 0, 0, 1, 1, 0, 1}, //C (2)
        {1, 0, 1, 0, 1, 0, 0}, //D (3)
        {0, 0, 1, 1, 0, 0, 0}, //E (4)
        {0, 0, 0, 0, 0, 0, 1}, //F (5)
        {0, 1, 1, 0, 0, 1, 0}, //G (6)
    };

    public static int[][] getGraph1() {
        return graph1;
    }

    public static Node[] getGraph1WithAdjacencies() {

        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");
        Node d = new Node("D");
        Node e = new Node("E");
        Node f = new Node("F");
        Node g = new Node("G");

        a.adjacencies = new Edge[]{
            new Edge(b, 2),
            new Edge(c, 5),
            new Edge(d, 2)
        };

        b.adjacencies = new Edge[]{
            new Edge(g, 3)
        };

        c.adjacencies = new Edge[]{
            new Edge(d, 3),
            new Edge(e, 4),
            new Edge(g, 2)
        };

        d.adjacencies = new Edge[]{
            new Edge(e, 4)
        };

        g.adjacencies = new Edge[]{
            new Edge(f, 8)
        };

        return new Node[]{a, b, c, d, e, f, g};
    }
}