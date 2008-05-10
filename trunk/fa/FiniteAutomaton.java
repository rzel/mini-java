package fa;
import fa.legacy.State;
import java.util.Set;

public interface FiniteAutomaton<T> {
    public void addTransition(State from, State to, T input);
    //public void addAcceptedState(State state);
    public Set<State> reachableStates(State from, T input);
    public Set<T> possibleInputs(State from);
}
