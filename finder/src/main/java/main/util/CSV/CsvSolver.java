package main.util.CSV;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class CsvSolver {
    private static String[][] readCols(Path ruta) throws IOException {
        String firstLine = Files.lines(ruta).findFirst().orElse("");
        char delimitador = firstLine.contains(";") ? ';' : ',';

        CSVFormat format = CSVFormat.DEFAULT.builder().setDelimiter(delimitador).build();

        try (FileReader reader = new FileReader(ruta.toFile());
             CSVParser csvParser = new CSVParser(reader, format)) {

            List<CSVRecord> filas = csvParser.getRecords();
            if (filas.isEmpty()) return new String[0][0];

            int totalColumnas = filas.get(0).size();
            int totalFilas = filas.size();
            
            String[][] columnData = new String[totalColumnas][totalFilas];

            for (int i = 0; i < totalFilas; i++) {
                CSVRecord fila = filas.get(i);
                for (int j = 0; j < totalColumnas; j++) {
                    columnData[j][i] = (j < fila.size()) ? fila.get(j) : "";
                }
            }

            return columnData;
        }
    }

    public static int[][] getMazeData(Path path) throws IOException {
        // Formatear a ","
        String firstLine = Files.lines(path).findFirst().orElse("");
        char delimitador = firstLine.contains(";") ? ';' : ',';

        CSVFormat format = CSVFormat.DEFAULT.builder().setDelimiter(delimitador).build();

        try (FileReader reader = new FileReader(path.toFile()); CSVParser csvParser = new CSVParser(reader, format)) {

            List<CSVRecord> filas = csvParser.getRecords();
            if (filas.isEmpty()) return new int[0][0];

            int totalFilas = filas.size();
            int totalColumnas = filas.get(0).size();
            
            int[][] graphData = new int[totalFilas][totalColumnas];

            for (int i = 0; i < totalFilas; i++) {
                CSVRecord fila = filas.get(i);
                for (int j = 0; j < totalColumnas; j++) {
                    // Si la celda existe, la convierte a entero; si no, por defecto pone 0 (muro o vacío)
                    if (j < fila.size()) {
                        String celdaLimpia = fila.get(j).replaceAll("[^\\d-]", "");
                        graphData[i][j] = Integer.parseInt(celdaLimpia);
                    } else {
                        graphData[i][j] = 0; 
                    }
                }
            }

            return graphData;
        }
    }

    public static String[][] getGraphData(Path path) throws IOException {
        String firstLine = Files.lines(path).findFirst().orElse("");
        char delimitador = firstLine.contains(";") ? ';' : ',';

        CSVFormat format = CSVFormat.DEFAULT.builder().setDelimiter(delimitador).build();

        try (FileReader reader = new FileReader(path.toFile()); CSVParser csvParser = new CSVParser(reader, format)) {

            List<CSVRecord> filas = csvParser.getRecords();
            if (filas.isEmpty()) return new String[0][0];

            int totalFilas = filas.size();
            int totalColumnas = filas.get(0).size();
            
            String[][] graphData = new String[totalFilas][totalColumnas];

            for (int i = 0; i < totalFilas; i++) {
                CSVRecord fila = filas.get(i);
                for (int j = 0; j < totalColumnas; j++) {
                    // Si la celda existe, la convierte a entero; si no, por defecto pone 0 (muro o vacío)
                    if (j < fila.size()) {
                        graphData[i][j] = fila.get(j);
                    } else {
                        graphData[i][j] = "";
                    }
                }
            }

            return graphData;
        }
    }
}