package main.util.swing;

import javax.swing.*;
import main.util.Node;
import main.Solvers.GraphSolver;
import java.awt.*;
import java.util.*;
import java.util.List;

public class UI extends JFrame {

    private JTextField startNodeField;
    private JTextField endNodeField;
    private JComboBox<String> algorithmComboBox;
    private JButton actionButton;
    private GraphUI graphUI;
    private List<Node> currentNodes;

    public UI(String title) {
        setTitle(title);
        setSize(750, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        controlPanel.setBackground(new Color(230, 235, 240));

        JLabel startLabel = new JLabel("Start Node:");
        startNodeField = new JTextField(5);
        
        JLabel endLabel = new JLabel("End Node:");
        endNodeField = new JTextField(5);

        JLabel algoLabel = new JLabel("Algorithm:");
        String[] algorithms = {"BFS", "DFS", "UCS", "A*"};
        algorithmComboBox = new JComboBox<>(algorithms);

        actionButton = new JButton("Find Path");
        actionButton.addActionListener(e -> executeSelectedAlgorithm());

        controlPanel.add(startLabel);
        controlPanel.add(startNodeField);
        controlPanel.add(endLabel);
        controlPanel.add(endNodeField);
        controlPanel.add(algoLabel);
        controlPanel.add(algorithmComboBox);
        controlPanel.add(actionButton);

        add(controlPanel, BorderLayout.NORTH);
    }

    private void executeSelectedAlgorithm() {
        String startName = startNodeField.getText().trim();
        String endName = endNodeField.getText().trim();
        String algo = (String) algorithmComboBox.getSelectedItem();

        Node startNode = null;
        Node endNode = null;

        for (Node n : currentNodes) {
            n.parent = null;
            n.pathCost = Double.MAX_VALUE;
            if (n.data.equalsIgnoreCase(startName)) startNode = n;
            if (n.data.equalsIgnoreCase(endName)) endNode = n;
        }

        if (startNode == null || endNode == null) {
            JOptionPane.showMessageDialog(this, "Invalid start or end node name.");
            return;
        }

        Set<Node> visited = new LinkedHashSet<>();
        List<Node> path = new ArrayList<>();

        if ("BFS".equals(algo)) {
            path = GraphSolver.bfs(currentNodes, startNode, endNode, visited);
        } else if ("DFS".equals(algo)) {
            path = GraphSolver.dfs(currentNodes, startNode, endNode, visited);
        } else if ("UCS".equals(algo)) {
            path = GraphSolver.ucs(currentNodes, startNode, endNode, visited);
        } else if ("A*".equals(algo)) {
            path = GraphSolver.aStar(currentNodes, startNode, endNode, visited);
        }

        graphUI.animatePath(visited, path);
    }


    

    public void run(List<Node> nodes) {
        this.currentNodes = nodes;
        graphUI = new GraphUI(nodes);
        add(graphUI, BorderLayout.CENTER);
        setVisible(true);
    }
}