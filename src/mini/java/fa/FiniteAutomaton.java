package mini.java.fa;

import java.util.Set;

/**
 * FiniteAutomaton represents a finite state machine.
 * 
 * @author Alex
 */

public interface FiniteAutomaton {
    /**
     * Add a transition to this finite automaton.
     */
    public void addTransition(State from, State to, Object input);

    /**
     * Get all reachable states from the source state through the given input.
     * Note only states reachable within one step should be returned.
     * 
     * @return Reachable states. Empty set for null.
     */
    public Set<State> getStates(State from, Object input);

    /**
     * Get all reachable states regardless input. Note only states reachable
     * within a single step will be returned.
     */
    public Set<State> getStates(State from);

    /**
     * Return all states in this finite automaton
     */
    public Set<State> getStates();
    
    /**
     * Return all acceptable states in this finite automaton
     */
    public Set<AcceptableState> getAcceptableState();

    /**
     * Get all possible inputs the specified state can accept
     */
    public Set<Object> getInputs(State from);

    /**
     * Get all posible inputs that any state in this Automaton can accept
     */
    public Set<Object> getInputs();
}
