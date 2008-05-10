package fa;
import fa.legacy.State;
import java.util.Set;

public interface NFiniteAutomaton<T> extends FiniteAutomaton<T> {
    public void addTransition(State from, State to);
    public Set<State> reachableStates(State from);
}
