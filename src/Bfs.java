import java.util.*;

public class Bfs {

        public static <T> AlgoResult<T> bfsSolve(T[][] initial, T[][] goal, int xVide, int yVide) {
            Queue<State<T>> ouverte = new LinkedList<>();
            Set<State<T>> fermee = new HashSet<>();

            State<T> start = new State<>(initial, xVide, yVide, null);
            ouverte.add(start);

            long startTime = System.currentTimeMillis();

            while (!ouverte.isEmpty()) {
                State<T> current = ouverte.poll();
                fermee.add(current);
                System.out.println("Exploration :");
                current.printGrid();

                if (current.isGoal(goal)) {
                    long duration = System.currentTimeMillis() - startTime;
                    return new AlgoResult<>(
                            reconstruireChemin(current),
                            ouverte.size(),
                            fermee.size(),
                            duration
                    );
                }

                for (State<T> voisin : current.getVoisins()) {
                    if (!fermee.contains(voisin) && !ouverte.contains(voisin)) {
                        ouverte.add(voisin);
                    }
                }
            }

            long duration = System.currentTimeMillis() - startTime;
            return new AlgoResult<>(null, ouverte.size(), fermee.size(), duration);
        }
    private static <T> List<State<T>> reconstruireChemin(State<T> state) {
        List<State<T>> chemin = new ArrayList<>();
        while (state != null) {
            chemin.add(state);
            state = state.parent;
        }
        Collections.reverse(chemin);
        return chemin;
    }

    public static <T> void printSolution(List<State<T>> solution) {
        if (solution == null) {

            System.out.println("Pas de solution trouvée.");
            return;
        }
        for (State<T> state : solution) {
            System.out.println("Étape:");
            state.printGrid();
            System.out.println();
        }
    }

    public static void main(String[] args) {
        String[][] initialStr = {
                {"i","s","f","n"},
                {"l",null,"u","e"}
        };
        String[][] goalStr = {
                {"e","l","u","s"},
                {"f","i","n", null}
        };

        AlgoResult<String> result = Bfs.bfsSolve(initialStr, goalStr, 1, 1);
        Bfs.printSolution(result.solution);
        if (result.solution != null) {
            System.out.println("Nœuds explorés : " + result.closedSetSize);
            System.out.println("Durée : " + result.durationMs + " ms");
    }}}