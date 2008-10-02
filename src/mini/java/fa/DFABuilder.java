package mini.java.fa;

public interface DFABuilder {

    /**
     * Add a transition to the DFA being built. A transition consists a source
     * state, a target state and an input which will trigger the transition.
     * <b>Note:</b> a source/target states pair can only be associated with a
     * single input; new input will override previous ones.
     * 
     * InitialState and AcceptableState can be set be passing corresponding
     * states as parameters. Note that a DFA can have no more than one
     * InitialState. Invalid transitions will be ignored.
     */
    public void addTransition(State from, State to, Object input);

    /**
     * Factory method which returns an DFA instance based on the transitions
     * added to this DFABuilder so far.
     */
    public DFA buildDFA();
}
