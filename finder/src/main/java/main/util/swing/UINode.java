package main.util.swing;

import main.util.Node;

public class UINode {
    Node rawNode;
    double x, y;
    double vx, vy;

    UINode(Node rawNode, double x, double y) {
        this.rawNode = rawNode;
        this.x = x;
        this.y = y;
    }
}
