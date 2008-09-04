package mini.java.fa;


/**
 * Represents a non-deterministic finite automaton. NFA can contain epsilon
 * transition, which doesn't need an input and can happen automatically.
 * 
 * Theoretically speaking, states in an NFA can have multiple successive states
 * of the same inputs. But such behaviour is not implemented in our NFA class.
 * Since that can be achieved easily by using the epsilon transitions.
 * 
 * NOTE: NFA is not intended to be run by simulators. One could call buildDFA()
 * method to get an equivalent DFA representation of the NFA and run that DFA
 * instead. So NFA can be treated as a DFA builder, which just has a different
 * algorithm compared to the default DFA builder.
 * 
 * @author Alex
 */
public interface NFA {
    public void addTransition(State from, State to);

    public void addTransition(State from, State to, Object input);

    public DFA buildDFA();
}
