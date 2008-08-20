package mini.java.fa;

import java.util.Set;

import mini.java.fa.legacy.State;

/**
 * FiniteAutomaton represents a finite state machine. The exact behavior (either
 * it's a DFA or an NFA) depends on the underlying implementation.
 * 
 * @author Alex
 * 
 */
public interface FiniteAutomaton {

	/**
	 * Add a transition in this finite automaton
	 * 
	 * @param from
	 * @param to
	 * @param input
	 */
	public void addTransition(State from, State to, Object input);

	// public void addAcceptedState(State state);
	public Set<State> reachableStates(State from, Object input);

	public Set<Object> possibleInputs(State from);
}
