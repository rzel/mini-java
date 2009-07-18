package mini.java.fa.v1;

/**
 * Represents a state in a Finite Automaton
 */
public class State {
    private static Integer ID = 0;
    private final Integer _id;
    
    public State() {
        synchronized(ID) {
            _id = ID++;
        }
    }

    @Override
    public String toString() {
        return "State(" + _id + ")";
    }
}
