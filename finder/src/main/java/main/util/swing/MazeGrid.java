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
    private int[][] csvMaze;
    
    //Auxiliares para StartPos y EndPos
    private int[] startAux = {-1, -1};
    private int[] endAux = {-1, -1};

    public MazeGrid(int[][] maze){
        this.csvMaze = maze;
        this.rows = maze.length;
        this.cols = maze[0].length;
        this.guiGrid = new JPanel[rows][cols];
        
        this.setLayout(new BorderLayout());
        
        setTopPanel(); //Panel superior
        setMaze();
    }

    /**
     * Creates the top bar with position inputs, dropdown list, and buttons.
     */
    private void setTopPanel() {
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(Color.LIGHT_GRAY);
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        /* ===========================
        Campos de posicion inicial
        ===========================*/
        gbc.gridx = 0; gbc.gridy = 0;
        topPanel.add(new JLabel("Posicion inicial:"), gbc);
        
        //Input de texto para colocar la posicion de la casilla inicial
        gbc.gridx = 1;
        JTextField txtStart = new JTextField(6);
        txtStart.setToolTipText("1,1");
        topPanel.add(txtStart, gbc);

        //Boton para aplicar los cambios de la posicion inicial
        gbc.gridx = 2;
        JButton btnSetStart = new JButton("Assign");
        btnSetStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    
                    String[] parts = txtStart.getText().split(",");
                    int x = Integer.parseInt(parts[0].trim());
                    int y = Integer.parseInt(parts[1].trim());
                    
                    //Cambios en MazeSolver
                    MazeSolver.startX = x;
                    MazeSolver.startY = y;
                    
                    //Remplazar posicion inicial anterior por la nueva
                    if (startAux[0] != -1) {
                        Color originalColor = (csvMaze[startAux[1]][startAux[0]] == 0) ? Color.DARK_GRAY : Color.BLACK;
                        addColor(startAux[0], startAux[1], originalColor);
                    }
                    startAux[0] = x;
                    startAux[1] = y;
                    addColor(x, y, Color.GREEN);
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Formato de posicion inicial invalido");
                }
            }
        });
        topPanel.add(btnSetStart, gbc);

        /* ===========================
        Campos de posicion final
        ===========================*/
        gbc.gridx = 0; gbc.gridy = 1;
        topPanel.add(new JLabel("Posicion final:"), gbc);

        //Input de texto para colocar la posicion de la casilla final
        gbc.gridx = 1;
        JTextField txtEnd = new JTextField(6);
        txtEnd.setToolTipText("10,12");
        topPanel.add(txtEnd, gbc);

        //Boton para aplicar los cambios de la posicion inicial
        gbc.gridx = 2;
        JButton btnSetEnd = new JButton("Assign");
        btnSetEnd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String[] parts = txtEnd.getText().split(",");
                    int x = Integer.parseInt(parts[0].trim());
                    int y = Integer.parseInt(parts[1].trim());
                    
                    //Cambios en MazeSolver
                    MazeSolver.endX = x;
                    MazeSolver.endY = y;
                    
                    //Remplazar posicion inicial anterior por la nueva
                    if (endAux[0] != -1) {
                        Color originalColor = (csvMaze[endAux[1]][endAux[0]] == 0) ? Color.DARK_GRAY : Color.BLACK;
                        addColor(endAux[0], endAux[1], originalColor);
                    }
                    endAux[0] = x;
                    endAux[1] = y;
                    addColor(x, y, Color.RED);
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Formato de posicion inicial invalido");
                }
            }
        });
        topPanel.add(btnSetEnd, gbc);

        //ComboBox con BFS, DFS, UCS y A*
        gbc.gridx = 0; gbc.gridy = 2;
        String[] algorithmOptions = { "BFS", "DFS", "UCS", "A*" };
        JComboBox<String> algorithmSelector = new JComboBox<>(algorithmOptions);
        topPanel.add(algorithmSelector, gbc);

        gbc.gridx = 1;
        JButton btnStart = new JButton("Iniciar");
        btnStart.addActionListener(e -> {
            //Hilo cargando el algoritmo elegido en el ComboBox
            new Thread(() -> {
                String selected = (String) algorithmSelector.getSelectedItem();
                if (selected.contains("BFS")) {
                    MazeSolver.bfs(this);  
                } else if (selected.contains("DFS")) {
                    MazeSolver.dfs(this);
                } else if (selected.contains("UCS")) {
                    MazeSolver.ucs(this);
                } else if(selected.contains("A*")){
                    MazeSolver.aStart(this);
                }
            }).start();
        });
        topPanel.add(btnStart, gbc);

        //Boton de limpiar
        gbc.gridx = 2;
        JButton btnClear = new JButton("Clear");
        btnClear.addActionListener(e -> resetGridPaint());
        topPanel.add(btnClear, gbc);

        this.add(topPanel, BorderLayout.NORTH);
    }

    private void setMaze(){
        //Lenar cuadricula visual con la cuadricula dada por el CSV (csvMaze)
        JPanel panelMaze = new JPanel(new GridLayout(rows, cols));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                guiGrid[i][j] = new JPanel();
                guiGrid[i][j].setBorder(BorderFactory.createLineBorder(Color.GRAY));

                if (csvMaze[i][j] == 0) {
                    //Casilla libre
                    guiGrid[i][j].setBackground(Color.DARK_GRAY);
                } else {
                    //Casilla con obstaculo
                    guiGrid[i][j].setBackground(Color.BLACK);
                }
                
                panelMaze.add(guiGrid[i][j]);
            }
        }
        
        this.add(panelMaze, BorderLayout.CENTER);
    }

    public void addColor(int x, int y, Color color) {
        if (y >= 0 && y < rows && x >= 0 && x < cols) {
            guiGrid[y][x].setBackground(color);
            guiGrid[y][x].repaint();
            guiGrid[y][x].revalidate();
        }
    }

    public void addStart(int x, int y){
        addColor(x, y, Color.YELLOW);
    }

    public void addEnd(int x, int y){
        addColor(x, y, Color.RED);
    }

    public void resetGridPaint() {
        //Lenar de color GRIS OSCURO o NEGRO si es 0 o 1
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (csvMaze[i][j] == 0) {
                    guiGrid[i][j].setBackground(Color.DARK_GRAY);
                } else {
                    guiGrid[i][j].setBackground(Color.BLACK);
                }
            }
        }
        // Conservar inicio y final
        if (startAux[0] != -1) addColor(startAux[0], startAux[1], Color.GREEN);
        if (endAux[0] != -1) addColor(endAux[0], endAux[1], Color.RED);
    }

    public void run(){
        setTitle("Maze Solver %dx%d - Control Panel".formatted(cols, rows));
        setSize(650, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}