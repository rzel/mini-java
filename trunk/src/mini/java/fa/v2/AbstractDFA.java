package mini.java.fa.v2;
import java.util.*;

public abstract class AbstractDFA implements DFA {
	protected DFAState dfaState;
	protected DFANode curNode;
	protected Collection<Transition> transitions;
	protected Collection<DFANode> acctNodes;
	/*
	 * The constructor of AbstractDFA
	 */
	public AbstractDFA() {
		reset();
		curNode = DFANode.getStartNode();
		transitions = new ArrayList<Transition>();
		acctNodes = new HashSet<DFANode>();
	}
	/*
	 * Reset the DFA
	 * @see model.DFA#reset()
	 */
	public void reset() {
		dfaState = DFAState.INIT;
		curNode = DFANode.getStartNode();
	}
	/*
	 * Add the specified transition to the DFA
	 * @param t specified transition 
	 * @see model.DFA#addTransition(model.Transition)
	 */
	public void addTransition(Transition t) {
		transitions.add(t);
	}
	/*
	 * Add the specified AcceptedNode to the DFA
	 * @param node specified AcceptedNode 
	 * @see model.DFA#addAcceptedNode(model.DFANode)
	 */
	public void addAcceptedNode(DFANode node) {
		acctNodes.add(node);
	}
}
