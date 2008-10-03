package mini.java.fa;

import java.util.Set;

/**
 * DFA is an interface for all state machine like data structure. Note this
 * interface doesn't include methods like addTransition() and other such methods that
 * can alter the data structure. So the interface itself is immutable.
 * 
 * @author Alex
 */

public interface DFA {
    /**
     * Returns the target state from the source state through the given input.
     * There should be no more than one such target state. Null input should
     * be seen as invalid input, and no states should be returned.
     * 
     * @return The target state. Null if there's no such state.
     */
    public State getState(State from, Object input);
    
    /**
     * Returns the initial state in this DFA.
     */
    public InitialState getInitialState();

    /**
     * Returns all inputs that the specified state can accept.
     */
    public Set<Object> getInputs(State from);
}
