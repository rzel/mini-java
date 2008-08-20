package mini.java.fa.legacy;

public interface DFA {
	
	/**
	 * Give this DFA a input character
	 * @param c the input character
	 * @return the resulting DFAState
	 */
	public DFAState feed(char c);
	
	/**
	 * 
	 * Resets the state of this DFA
	 *
	 */
	public void reset();
	
	/**
	 * Add a specified Transition to this DFA
	 * @param t the specified Transition
	 */
	public void addTransition(Transition t);
	
	/**
	 * Add a specified Accepted Node to this DFA
	 * @param node the specified accepted node
	 */
	public void addAcceptedNode(DFANode node);
}
