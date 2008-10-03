package mini.java.fa;

import java.util.Set;

/**
 * Represents a non-deterministic finite automaton. NFA can contain epsilon
 * transitions, transitions without input and can be triggered implicitly.
 * This interface is an immutable one, similar as DFA, which is up to the
 * implemetations to decide whether to use a builder or some other mechanism
 * for adding transitions.
 * <br>
 * <br> 
 * Theoretically speaking, states in an NFA can have multiple successive states
 * of the same inputs. But such behaviour is not implemented in our NFA class
 * since the same behaviour can be achieved easily by using an epsilon transition.
 * <br>
 * <br>
 * <b>NOTE:</b> NFA is not intended to be run by simulators. One could call buildDFA()
 * method to get an equivalent DFA representation of the NFA and run that DFA
 * instead.
 * 
 * @author Alex
 */
public interface NFA {
    /**
     * Get the "closure" of the target states. A target state "closure" is all
     * the target states from the source state's closure and the target states'
     * closures . An empty set will be returned if no such states.
     * <br>
     * <br>
     * <b>NOTE:</b> the source state itself will not be included in this kind of
     * closure.
     */
    public Set<State> closure(State from, Object input);
    
    /**
     * Get the "closure" of the source state. A source state "closure" is all
     * states reachable from the source state through an epsilon transition.
     * Empty set will be returned if no such states.
     * 
     * NOTE: the source state will be included in its own closure.
     */
    public Set<State> closure(State from);
    
    /**
     * Get the initial state in this NFA. Each NFA can have one and only one such
     * initial state.
     */
    public InitialState getInitialState();

    /**
     * Get valid inputs of the given source state and its closure
     */
    public Set<Object> getInputs(State from);

//    /**
//     * Convert the NFA to a DFA.
//     * @return an equivalent DFA representation
//     */
//    public DFA buildDFA();
}
