import java.util.*;

class State<T> {
    T[][] grid;
    int xVide, yVide;
    State<T> parent;

    public State(T[][] grid, int xVide, int yVide, State<T> parent) {
        this.grid = deepCopy(grid);
        this.xVide = xVide;
        this.yVide = yVide;
        this.parent = parent;
    }

    public boolean isGoal(T[][] goal) {
        return Arrays.deepEquals(this.grid, goal);
    }

    public List<State<T>> getVoisins() {
        List<State<T>> voisins = new ArrayList<>();
        int[] dx = {0, 0, -1, 1};  // verticale
        int[] dy = {-1, 1, 0, 0};  // horizontale

        for (int i = 0; i < 4; i++) {
            int newX = xVide + dx[i];
            int newY = yVide + dy[i];

            if (newX >= 0 && newX < grid.length && newY >= 0 && newY < grid[0].length) {

                T[][] newGrid = deepCopy(grid);
                newGrid[xVide][yVide] = newGrid[newX][newY];
                newGrid[newX][newY] = null;
                State<T> newState = new State<>(newGrid, newX, newY, this);

                if (parent == null || !Arrays.deepEquals(newState.grid, parent.grid)) {
                    voisins.add(newState);
                }
            }
        }
        return voisins;
    }


    private T[][] deepCopy(T[][] original) {
        T[][] copy = Arrays.copyOf(original, original.length);
        for (int i = 0; i < original.length; i++) {
            copy[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return copy;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof State) {
            State<?> other = (State<?>) obj;
            return Arrays.deepEquals(this.grid, other.grid);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(grid);
    }

    public void printGrid() {
        for (T[] row : grid) {
            System.out.println(Arrays.toString(row));
        }
    }
}