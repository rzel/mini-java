package mini.java.fa.my;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public final class Helper {
    /**
     * Helper function used to find all nodes in a collection. Each node will be
     * included in the return set exactly once. The equivalence of the nodes
     * is based on the identity rather than the return value of equals(). The
     * initial node is provided as the parameter.
     */
    public static <T> Queue<T> findAll(T node_, Finder<T> finder_) {
        assert(node_   != null);
        assert(finder_ != null);
        
        Set<T> checkedNodes = new HashSet<T>();
        
        Queue<T> nodes = new LinkedList<T>();
        Queue<T> uncheckedNodes = new LinkedList<T>(Collections.singleton(node_));
        
        while (!uncheckedNodes.isEmpty()) {
            T node = uncheckedNodes.remove();
            checkedNodes.add(node); // mark the node as checked
            nodes.add(node);
            
            for (T next : finder_.findNext(node)) {
                if (!checkedNodes.contains(next)) {
                    uncheckedNodes.add(next);
                }
            }
        }
        
        return nodes;
    }
    
    
    /**
     * Helper function used to find the closure for the given NFAState.
     */
    public static Set<NFAState> findClosure(NFAState state_) {
        assert (state_ != null);
        return new HashSet<NFAState>(Helper.findAll(state_,
                new Finder<NFAState>() {
                    public Queue<NFAState> findNext(NFAState state_) {
                        return new LinkedList<NFAState>(state_.getStates());
                    }
                }));
    }
}
