package main.util.swing;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;
import main.util.Edge;
import main.util.Node;

public class GraphUI extends JPanel {
    private final List<UINode> uiNodes = new ArrayList<>();
    private final Map<Node, UINode> nodeMap = new HashMap<>();
    private final int NODE_RADIUS = 40;
    
    private final double REPULSION = 4500.0;
    private final double ATTRACTION = 0.02;
    private final double DAMPING = 0.85;
    private final int MAX_REPULSION_DIST = 400;

    private Set<Node> visitedNodes = new LinkedHashSet<>();
    private List<Node> finalPath = new ArrayList<>();
    private Set<Node> currentlyAnimatedVisited = new HashSet<>();
    private List<Node> currentlyAnimatedPath = new ArrayList<>();
    private Timer animationTimer;

    public GraphUI(List<Node> graphNodes) {
        Random r = new Random();
        for (Node n : graphNodes) {
            UINode uiN = new UINode(n, 200 + r.nextInt(150), 200 + r.nextInt(150));
            uiNodes.add(uiN);
            nodeMap.put(n, uiN);
        }

        Timer physicsTimer = new Timer(16, e -> updatePhysics());
        physicsTimer.start();
    }

    public void animatePath(Set<Node> visited, List<Node> path) {
        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }
        
        this.visitedNodes = visited;
        this.finalPath = path;
        this.currentlyAnimatedVisited.clear();
        this.currentlyAnimatedPath.clear();

        List<Node> visitedOrder = new ArrayList<>(visitedNodes);
        final int[] index = {0};

        animationTimer = new Timer(300, e -> {
            if (index[0] < visitedOrder.size()) {
                currentlyAnimatedVisited.add(visitedOrder.get(index[0]));
                index[0]++;
            } else {
                currentlyAnimatedPath = finalPath;
                animationTimer.stop();
            }
            repaint();
        });
        animationTimer.start();
    }

    private void updatePhysics() {
        if (uiNodes.isEmpty()) return;

        for (int i = 0; i < uiNodes.size(); i++) {
            UINode n1 = uiNodes.get(i);
            for (int j = i + 1; j < uiNodes.size(); j++) {
                UINode n2 = uiNodes.get(j);
                double dx = n1.x - n2.x;
                double dy = n1.y - n2.y;
                double dist = Math.hypot(dx, dy) + 0.1;

                if (dist < MAX_REPULSION_DIST) {
                    double force = REPULSION / (dist * dist);
                    double fx = (dx / dist) * force;
                    double fy = (dy / dist) * force;

                    n1.vx += fx; n1.vy += fy;
                    n2.vx -= fx; n2.vy -= fy;
                }
            }
        }

        for (UINode sourceUI : uiNodes) {
            Node raw = sourceUI.rawNode;
            if (raw.adjacencies == null) continue;

            for (Edge edge : raw.adjacencies) {
                UINode targetUI = nodeMap.get(edge.target);
                if (targetUI == null) continue;

                double dx = targetUI.x - sourceUI.x;
                double dy = targetUI.y - sourceUI.y;

                double fx = dx * ATTRACTION;
                double fy = dy * ATTRACTION;

                sourceUI.vx += fx; sourceUI.vy += fy;
                targetUI.vx -= fx; targetUI.vy -= fy;
            }
        }

        for (UINode n : uiNodes) {
            n.vx *= DAMPING;
            n.vy *= DAMPING;

            n.x += n.vx;
            n.y += n.vy;

            n.x = Math.max(NODE_RADIUS, Math.min(getWidth() - NODE_RADIUS, n.x));
            n.y = Math.max(NODE_RADIUS, Math.min(getHeight() - NODE_RADIUS, n.y));
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setStroke(new BasicStroke(2));
        for (UINode sourceUI : uiNodes) {
            Node raw = sourceUI.rawNode;
            if (raw.adjacencies == null) continue;

            for (Edge edge : raw.adjacencies) {
                UINode targetUI = nodeMap.get(edge.target);
                if (targetUI == null) continue;

                int x1 = (int) sourceUI.x;
                int y1 = (int) sourceUI.y;
                int x2 = (int) targetUI.x;
                int y2 = (int) targetUI.y;

                if (currentlyAnimatedPath.contains(raw) && currentlyAnimatedPath.contains(edge.target) 
                    && Math.abs(currentlyAnimatedPath.indexOf(raw) - currentlyAnimatedPath.indexOf(edge.target)) == 1) {
                    g2.setColor(Color.ORANGE);
                    g2.setStroke(new BasicStroke(4));
                } else {
                    g2.setColor(Color.GRAY);
                    g2.setStroke(new BasicStroke(2));
                }

                g2.drawLine(x1, y1, x2, y2);

                int midX = (x1 + x2) / 2;
                int midY = (y1 + y2) / 2;

                String weightText = String.valueOf((int) edge.cost);
                g2.setFont(new Font("Arial", Font.PLAIN, 11));
                FontMetrics fmWeight = g2.getFontMetrics();
                int tw = fmWeight.stringWidth(weightText);
                int th = fmWeight.getAscent();

                g2.setColor(new Color(255, 255, 255, 220));
                g2.fillRoundRect(midX - tw/2 - 4, midY - th/2 - 2, tw + 8, th + 4, 6, 6);
                g2.setColor(Color.RED);
                g2.drawRoundRect(midX - tw/2 - 4, midY - th/2 - 2, tw + 8, th + 4, 6, 6);

                g2.setColor(Color.BLACK);
                g2.drawString(weightText, midX - tw / 2, midY + th / 2 - 2);
            }
        }

        for (UINode n : uiNodes) {
            g2.setColor(Color.WHITE);
            g2.fillOval((int)n.x - NODE_RADIUS/2, (int)n.y - NODE_RADIUS/2, NODE_RADIUS, NODE_RADIUS);
            
            Color nodeColor = new Color(46, 204, 113);
            if (currentlyAnimatedPath.contains(n.rawNode)) {
                nodeColor = Color.ORANGE;
            } else if (currentlyAnimatedVisited.contains(n.rawNode)) {
                nodeColor = Color.CYAN;
            }

            g2.setColor(nodeColor); 
            g2.setStroke(new BasicStroke(3));
            g2.drawOval((int)n.x - NODE_RADIUS/2, (int)n.y - NODE_RADIUS/2, NODE_RADIUS, NODE_RADIUS);

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            FontMetrics fm = g2.getFontMetrics();
            int tx = (int)n.x - fm.stringWidth(n.rawNode.data) / 2;
            int ty = (int)n.y + fm.getAscent() / 2 - 2;
            g2.drawString(n.rawNode.data, tx, ty);
        }
    }
}