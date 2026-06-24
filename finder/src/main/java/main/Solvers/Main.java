package main.Solvers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static Scanner sc = new Scanner(System.in);
    public static String graphFilesPath = "finder\\src\\main\\resources\\CSVs\\Graphs";
    public static String mazeFilesPath = "finder\\src\\main\\resources\\CSVs\\Mazes";
    

    public static void main(String[] args) {

        boolean tmp = true;
        while (tmp) {
            System.out.println(
                """
                ========================================
                Manual de uso en README.md
                
                ¿Que tipo de estructura desea indagar?
                [0] : Grafo
                [1] : Laberinto
                ========================================
                """
            );
            System.out.print("> ");
            int r = sc.nextInt();
            sc.nextLine(); //Limbiar buffer

            if (r == 0) graph();
            if (r == 1) maze();
            if (r > 1 || r < 0) System.out.println("Seleccione un dato valido");
        }
        
    }

    public static void maze() {
        Path path = selectMazeFile();
        MazeSolver.run(path);
    }

    public static void graph() {
        Path path = selectGraphFile();
        GraphSolver.run(path);
        
    }

    public static Path selectGraphFile() {
        List<Path> files = getFiles(graphFilesPath);
        String content = "";
        for (int i = 0; i < files.size(); i++) {
            content += "\n[" + i + "] " + files.get(i).getFileName();
        }

        System.out.print(
        """
        ========================================
        Seleccione el archivo que desea aplicale el algoritmo: 
        %s
        ========================================
        > 
        """.formatted(content)
        );

        return files.get(sc.nextInt());
    }

    public static Path selectMazeFile() {
        List<Path> files = getFiles(mazeFilesPath);
        String content = "";
        for (int i = 0; i < files.size(); i++) {
            content += "\n[" + i + "] " + files.get(i).getFileName();
        }

        System.out.print(
        """
        ========================================
        Seleccione el archivo que desea aplicale el algoritmo: 
        %s
        ========================================
        > 
        """.formatted(content)
        );
        
        return files.get(sc.nextInt());
    }

    public static List<Path> getFiles(String path) {
        List<Path> files = null;
        try {
           files = Files.list(Path.of(path)).filter(Files::isRegularFile).toList();
        } catch (IOException e) {
            System.err.println("Error al leer la carpeta: " + e.getMessage());
        }

        return files;
    }

}
