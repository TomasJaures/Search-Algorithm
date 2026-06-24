```java
package main.util.swing;

import javax.swing.*;
import main.Solvers.MazeSolver;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MazeGrid extends JFrame {
    private int rows;
    private int cols;
    private JPanel[][] guiGrid;
    private int[][] originalMaze;
    
    private int[] startAux = {-1, -1};
    private int[] endAux = {-1, -1};
    
    private JComboBox<String> algorithmSelector;

    public MazeGrid(int[][] maze){
        this.originalMaze = maze;
        this.rows = maze.length;
        this.cols = maze[0].length;
        this.guiGrid = new JPanel[rows][cols];
        
        this.setLayout(new BorderLayout());
        
        setTopPanel();
        setMaze();
    }

    private void setTopPanel() {
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(Color.LIGHT_GRAY);
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- ROW 0: Start Position ---
        gbc.gridx = 0; gbc.gridy = 0;
        topPanel.add(new JLabel("Set start position:"), gbc);

        gbc.gridx = 1;
        JTextField txtStart = new JTextField(6);
        txtStart.setToolTipText("Example: 1,1");
        topPanel.add(txtStart, gbc);

        gbc.gridx = 2;
        JButton btnSetStart = new JButton("Assign");
        btnSetStart.addActionListener(e -> {
            try {
                String[] parts = txtStart.getText().split(",");
                int x = Integer.parseInt(parts[0].trim());
                int y = Integer.parseInt(parts[1].trim());
                
                if(originalMaze[y][x] == 1) {
                    JOptionPane.showMessageDialog(null, "No puedes empezar sobre un muro (Casilla Negra).");
                    return;
                }
                
                MazeSolver.startX = x;
                MazeSolver.startY = y;
                
                if (startAux[0] != -1) {
                    Color originalColor = (originalMaze[startAux[1]][startAux[0]] == 0) ? Color.DARK_GRAY : Color.BLACK;
                    addColor(startAux[0], startAux[1], originalColor);
                }
                
                startAux[0] = x; startAux[1] = y;
                addColor(x, y, Color.GREEN);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Format error. Use: x,y (e.g. 0,2)");
            }
        });
        topPanel.add(btnSetStart, gbc);

        // --- ROW 1: End Position ---
        gbc.gridx = 0; gbc.gridy = 1;
        topPanel.add(new JLabel("Set end position:"), gbc);

        gbc.gridx = 1;
        JTextField txtEnd = new JTextField(6);
        txtEnd.setToolTipText("Example: 10,12");
        topPanel.add(txtEnd, gbc);

        gbc.gridx = 2;
        JButton btnSetEnd = new JButton("Assign");
        btnSetEnd.addActionListener(e -> {
            try {
                String[] parts = txtEnd.getText().split(",");
                int x = Integer.parseInt(parts[0].trim());
                int y = Integer.parseInt(parts[1].trim());
                
                if(originalMaze[y][x] == 1) {
                    JOptionPane.showMessageDialog(null, "El destino no puede ser un muro (Casilla Negra).");
                    return;
                }
                
                MazeSolver.endX = x;
                MazeSolver.endY = y;
                
                if (endAux[0] != -1) {
                    Color originalColor = (originalMaze[endAux[1]][endAux[0]] == 0) ? Color.DARK_GRAY : Color.BLACK;
                    addColor(endAux[0], endAux[1], originalColor);
                }
                
                endAux[0] = x; endAux[1] = y;
                addColor(x, y, Color.RED);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Format error. Use: x,y (e.g. 6,0)");
            }
        });
        topPanel.add(btnSetEnd, gbc);

        // --- ROW 2: Selector & Action Buttons ---
        gbc.gridx = 0; gbc.gridy = 2;
        String[] algorithmOptions = { "BFS (Breadth-First)", "DFS (Depth-First)" };
        algorithmSelector = new JComboBox<>(algorithmOptions);
        topPanel.add(algorithmSelector, gbc);

        gbc.gridx = 1;
        JButton btnStart = new JButton("Start");
        btnStart.addActionListener(e -> {
            // EJECUCIÓN EN HILO SEPARADO: Vital para evitar que la UI se congele
            new Thread(() -> {
                String selected = (String) algorithmSelector.getSelectedItem();
                if (selected.contains("BFS")) {
                    MazeSolver.bfs(this);
                } else if (selected.contains("DFS")) {
                    MazeSolver.dfs(this);
                }
            }).start();
        });
        topPanel.add(btnStart, gbc);

        gbc.gridx = 2;
        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(e -> resetGridPaint());
        topPanel.add(btnClear, gbc);

        // --- ROW 3: Hello Button ---
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 3;
        JButton btnHello = new JButton("Hello");
        btnHello.addActionListener(e -> System.out.println("Hello"));
        topPanel.add(btnHello, gbc);

        this.add(topPanel, BorderLayout.NORTH);
    }

    private void setMaze(){
        JPanel panelMaze = new JPanel(new GridLayout(rows, cols));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                guiGrid[i][j] = new JPanel();
                guiGrid[i][j].setBorder(BorderFactory.createLineBorder(Color.GRAY));
                panelMaze.add(guiGrid[i][j]);
            }
        }
        this.add(panelMaze, BorderLayout.CENTER);
        resetGridPaint();
    }

    /**
     * Limpia el laberinto regresándolo a su estado original (muros y caminos limpios)
     */
    public void resetGridPaint() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (originalMaze[i][j] == 0) {
                    guiGrid[i][j].setBackground(Color.DARK_GRAY);
                } else {
                    guiGrid[i][j].setBackground(Color.BLACK);
                }
            }
        }
        // Conservar visualmente los puntos iniciales/finales si ya se habían asignado
        if (startAux[0] != -1) addColor(startAux[0], startAux[1], Color.GREEN);
        if (endAux[0] != -1) addColor(endAux[0], endAux[1], Color.RED);
    }

    public void addColor(int x, int y, Color color) {
        if (y >= 0 && y < rows && x >= 0 && x < cols) {
            guiGrid[y][x].setBackground(color);
            guiGrid[y][x].repaint();
            guiGrid[y][x].revalidate();
        }
    }

    public void run(){
        setTitle("Maze Solver %dx%d - Control Panel".formatted(cols, rows));
        setSize(650, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
```