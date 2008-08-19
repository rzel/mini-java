package fa.legacy;
import java.util.*;

public class SimpleDFA extends AbstractDFA {

	
	public DFAState feed(char c) {
		/**
		 *  Iterates over the transition container
		 */
		for(Iterator<Transition> iter = transitions.iterator();
			iter.hasNext();) {
			Transition t = iter.next();
			// curNode found
			if(curNode.equals(t.getCurNode())) {
				// Iterates over the input list
				for(Iterator<Character> inputs = t.getInputs();
					inputs.hasNext();) {
					char in = inputs.next();
					// If input valid -> switch state
					if(c == in) {						
						curNode = t.getNxtNode();		// go to next node
						// check if accepted node
						for(Iterator<DFANode> acctIter = acctNodes.iterator();
							acctIter.hasNext();) {
							if(curNode.equals(acctIter.next())) {
								dfaState = DFAState.ACCEPTED;
								return dfaState;
							}
						}
						
						dfaState = DFAState.RUNNING;
						return dfaState;
					}
				}
			}
		}
		if(dfaState == DFAState.ACCEPTED) {
			dfaState = DFAState.ACCT_DEAD;
		} else {
			dfaState = DFAState.DEAD;
		}
		return dfaState;
	}
}

