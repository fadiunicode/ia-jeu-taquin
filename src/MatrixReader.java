import java.io.*;
import java.util.*;

public class MatrixReader {
    public static List<char[][]> readTaquinInstance(String filename) throws IOException {
        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
        }

        if (lines.size() < 2) {
            throw new IOException("Fichier trop court");
        }

        int n;
        try {
            n = Integer.parseInt(lines.get(0));
        } catch (NumberFormatException e) {
            throw new IOException("La première ligne doit être un nombre (nombre de lignes)", e);
        }

        if (lines.size() < 2 * n + 1) {
            throw new IOException("Nombre de lignes insuffisant. Attendu: " + (2 * n + 1)
                    + ", trouvé: " + lines.size());
        }

        int cols = 0;
        for (int i = 1; i <= 2 * n; i++) {
            if (lines.get(i).length() > cols) {
                cols = lines.get(i).length();
            }
        }

        char[][] initialGrid = new char[n][cols];
        char[][] finalGrid = new char[n][cols];

        // Remplissage initialGrid (lignes 1 à n)
        for (int i = 0; i < n; i++) {
            String currentLine = lines.get(i + 1);
            // Complète avec des espaces si nécessaire
            if (currentLine.length() < cols) {
                currentLine = String.format("%-" + cols + "s", currentLine);
            }
            for (int j = 0; j < cols; j++) {
                initialGrid[i][j] = currentLine.charAt(j);
            }
        }

        for (int i = 0; i < n; i++) {
            String currentLine = lines.get(i + 1 + n);
            if (currentLine.length() < cols) {
                currentLine = String.format("%-" + cols + "s", currentLine);
            }
            for (int j = 0; j < cols; j++) {
                finalGrid[i][j] = currentLine.charAt(j);
            }
        }

        return Arrays.asList(initialGrid, finalGrid);
    }
}