package mini.java.fa;

/**
 * A DFASimulator is used to "run" a DFA. It can accept a serious of input
 * objects and keep track of the state of that DFA after the corresponding
 * transitions.
 * 
 * The DFASimulator can have two states: "running" and "stopped". The
 * DFASimulator enters the "stopped" state right after taking in an invalid
 * input object, which doesn't trigger any transitions.
 * 
 * @author Alex
 */
public interface DFASimulator {
    /**
     * Takes in a single input object and triggers the corresponding transition.
     * It also changes current DFA state to the new one. If there's no
     * transition for the input, the DFASimulator enters the "stopped" state and
     * further inputs will be ignored.
     */
    public void step(Object input_);

    /**
     * Resets the DFASimulator by setting the InitialState of the underlying DFA
     * as current DFA state.
     */
    public void reset();

    /**
     * Returns the current state of the underlying DFA.
     */
    public State getDFAState();
    
    /**
     * Sets the current state of the underlying DFA to the given state.
     * NOTE: This method will also reset the DFASimulator state to "running".
     */
    public void setDFAState(State state_);

    /**
     * Returns current state of the DFASimulator. Not to be confused with
     * getDFAState(), which returns the underlying DFA state.
     * 
     * @return True for "running" state; False for "stopped" state.
     */
    public boolean isRunning();
}
