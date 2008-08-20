package mini.java.fa.legacy;
import java.util.*;

/**
 * 
 * A typical representation of a Transition in a DFA
 *
 */
public class Transition {
	private DFANode curNode;
	private DFANode nxtNode;
	private Set<Character> inputs;
	
	public Transition(DFANode cur, DFANode nxt) {
		curNode = cur;
		nxtNode = nxt;
		inputs = new HashSet<Character>();
	}
	
	public DFANode getCurNode() {
		return curNode;
	}
	
	public DFANode getNxtNode() {
		return nxtNode;
	}
	
	public boolean addInput(char c) {
		return inputs.add(c);
	}
	
	public Iterator<Character> getInputs() {
		return inputs.iterator();
	}
}
