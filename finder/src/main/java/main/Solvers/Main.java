package main.Solvers;

import java.util.Scanner;

public class Main {

    public static Scanner sc = new Scanner(System.in);

    

    public static void main(String[] args) {
        //GraphSolver.run(1);
        //GraphSolver.run(2);
        //GraphSolver.run(2);
        MazeSolver.run(2);

        /*
        while (true) {    
            System.out.println("¿Que operacion desea realizar? ");
            System.out.println("[1] Laberinto");
            System.out.println("[2] Grafo");
            System.out.print("> ");
            int inp = sc.nextInt();
            sc.nextLine();

            if(inp==1) maze();
            if(inp==2) graph();
            System.out.println("Input no valido");
        }
        */
        
    }

    public static void maze() {
        System.out.println("Haz seleccionado el Laberinto");
        printOptions();
        int inp = sc.nextInt();
        MazeSolver.run(inp);
    }

    public static void graph() {
        System.out.println("Haz seleccionado el grafo");
        printOptions();
        int inp = sc.nextInt();
        GraphSolver.run(inp);
    }

    public static void printOptions(){
        System.out.println("[1] BFS");
        System.out.println("[2] DFS");
        System.out.println("[3] BFS");
        System.out.println("[4] BFS");
        System.out.println("[5] BFS");

        System.out.print("> ");
    }

}
