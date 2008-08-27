package mini.java.fa;

import java.util.Set;


/**
 * Represents a non-deterministic finite automaton. NFA can contain epsilon
 * transitions, which don't need an input and can happen automatically.
 * 
 * @author Alex
 */
public interface NFA extends DFA {
    // TODO: add "convert to DFA" functionality. And think about the way to test it
    /**
     * Add an epsilon transition to the finite automaton
     * 
     * @param from
     * @param to
     */
    public void addTransition(State from, State to);

//    /**
//     * Return all states reachable through an epsilon transition
//     * 
//     * @param from
//     * @return reachable states. Empty set for null.
//     */
//    public Set<State> reachableStates(State from);
}
