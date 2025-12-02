import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GuiTaquin extends JFrame {
    private static final int TILE_SIZE = 80;
    private  JTextArea statsArea;
    private JComboBox<String> algorithmComboBox;
    private final char[][] initialGrid;
    private final char[][] finalGrid;

    public GuiTaquin(char[][] initialGrid, char[][] finalGrid) {
        if (initialGrid == null || finalGrid == null) {
            throw new IllegalArgumentException("Les grilles ne peuvent pas être null");
        }
        if (initialGrid.length == 0 || finalGrid.length == 0) {
            throw new IllegalArgumentException("Les grilles doivent avoir au moins une ligne");
        }

        this.initialGrid = deepCopy(initialGrid);
        this.finalGrid = deepCopy(finalGrid);

        setTitle("Résolution du Taquin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel gridsPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        gridsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gridsPanel.add(createGridPanel(this.initialGrid, "Grille Initiale"));
        gridsPanel.add(createGridPanel(this.finalGrid, "Grille Finale"));

        JPanel controlPanel = createControlPanel();

        add(gridsPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("Contrôle"));

        JPanel algorithmPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        algorithmPanel.add(new JLabel("Algorithme: "));
        algorithmComboBox = new JComboBox<>(new String[]{"BFS", "DFS", "Meilleur d'abord"});
        algorithmPanel.add(algorithmComboBox);

        JButton executeButton = new JButton("Exécuter");
        executeButton.addActionListener(e -> executeAlgorithm());
        algorithmPanel.add(executeButton);

        statsArea = new JTextArea(5, 30);
        statsArea.setEditable(false);
        statsArea.setText("Noeuds en ouverte: 0\nNoeuds en fermee: 0\nProfondeur: 0\nTemps: 0ms");
        JScrollPane statsScrollPane = new JScrollPane(statsArea);

        panel.add(algorithmPanel, BorderLayout.NORTH);
        panel.add(statsScrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void executeAlgorithm() {
        String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();

        try {
            String[][] initialStr = convertCharToString(initialGrid);
            String[][] goalStr = convertCharToString(finalGrid);

            int[] emptyPos = findEmptyPosition(initialGrid);
            int xVide = emptyPos[0];
            int yVide = emptyPos[1];

            AlgoResult<String> result;

            switch (selectedAlgorithm) {
                case "DFS":
                    result = Dfs.dfsSolve(initialStr, goalStr, xVide, yVide);
                    break;
                case "BFS":
                    result = Bfs.bfsSolve(initialStr, goalStr, xVide, yVide);
                    break;
                case "Meilleur d'abord":
                    result = BestFirst.bestFirstSolve(initialStr, goalStr, xVide, yVide);
                    break;
                default:
                    throw new IllegalStateException("Algorithme inconnu : " + selectedAlgorithm);
            }

            if (result.solution == null) {
                statsArea.setText(String.format(
                        "Pas de solution trouvée.\n\nNoeuds en ouverte: %d\nNoeuds en fermée: %d\nTemps: %dms",
                        result.openSetSize,
                        result.closedSetSize,
                        result.durationMs));
            } else {
                statsArea.setText(String.format(
                        "Noeuds en ouverte: %d\nNoeuds en ferme: %d\nProfondeur: %d\nTemps: %dms",
                        result.openSetSize,
                        result.closedSetSize,
                        result.solution.size() - 1,
                        result.durationMs
                ));

                for (State<String> s : result.solution) {
                    s.printGrid();
                    System.out.println();
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'exécution: " + ex.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
    private JPanel createGridPanel(char[][] grid, String title) {
        JPanel panel = new JPanel(new GridLayout(grid.length, grid[0].length));
        panel.setBorder(BorderFactory.createTitledBorder(title));

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                JLabel label = new JLabel(grid[i][j] == ' ' ? " " : String.valueOf(grid[i][j]), SwingConstants.CENTER);
                label.setPreferredSize(new Dimension(TILE_SIZE, TILE_SIZE));
                label.setFont(new Font("Arial", Font.BOLD, 24));
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                if (grid[i][j] == ' ') {
                    label.setBackground(Color.LIGHT_GRAY);
                    label.setOpaque(true);
                }

                panel.add(label);
            }
        }
        return panel;
    }

    private static String[][] convertCharToString(char[][] grid) {
        String[][] strGrid = new String[grid.length][grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                strGrid[i][j] = (grid[i][j] == ' ') ? null : String.valueOf(grid[i][j]);
            }
        }
        return strGrid;
    }

    private static int[] findEmptyPosition(char[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (grid[i][j] == ' ') {
                    return new int[]{i, j};
                }
            }
        }
        throw new IllegalArgumentException("La grille ne contient pas de case vide");
    }

    private static char[][] deepCopy(char[][] original) {
        char[][] copy = new char[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

    public static void main(String[] args) {
        String filename = "C:\\Users\\zaiba\\IdeaProjects\\tpiasymbolique\\src\\taquin_3x4.grid.txt";
        try {
            List<char[][]> grids = MatrixReader.readTaquinInstance(filename);
            new GuiTaquin(grids.get(0), grids.get(1));
        } catch (IOException e) {
            System.err.println("Erreur de lecture du fichier: " + e.getMessage());
        }
    }
}