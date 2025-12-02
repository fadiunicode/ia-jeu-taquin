import java.util.List;

public class AlgoResult<T> {
    public final List<State<T>> solution;
    public final int openSetSize;
    public final int closedSetSize;
    public final long durationMs;

    public AlgoResult(List<State<T>> solution, int openSetSize, int closedSetSize, long durationMs) {
        this.solution = solution;
        this.openSetSize = openSetSize;
        this.closedSetSize = closedSetSize;
        this.durationMs = durationMs;
    }
}