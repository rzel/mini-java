package mini.java.fa;

public interface NFABuilder {
    /**
     * Add a transition to the DFA being built. A transition consists a source
     * state, a target state and an input which will trigger the transition.
     * <b>Note:</b> a source/target states pair can only be associated with a
     * single input; new input will override previous ones.
     * <br>
     * <br>
     * InitialState and AcceptableState can be set be passing corresponding
     * states as parameters. Note that a DFA can have no more than one
     * InitialState. Invalid transitions will be ignored.
     */
    public void addTransition(State from_, State to_, Object input_);
    
    /**
     * Add an epsilon transition to the NFA being built. Epsilon transitions are
     * transitions without input and can be triggered implicitly.
     */
    public void addTransition(State from_, State to_);
    
    /**
     * Factory method which creates an NFA instance based on the transitions
     * added to this NFABuilder so far.
     */
    public NFA buildNFA();
}
