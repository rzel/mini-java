package mini.java.fa;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;


public class NFAState {
    // unique id assigned to each NFAState
    private static Integer ID = 0;
    
    private Integer               _id; // unique id
    private Set<NFAState>         _epsilons;
    private Map<Object, NFAState> _transitions;

    public NFAState() {
        synchronized(ID) {
          _id = ID++;
        }
        
        _epsilons = new TreeSet<NFAState>(new Comparator<NFAState>() {
            public int compare(NFAState s1, NFAState s2) {
                return s1._id.compareTo(s2._id);
            }
        });
        _transitions = new TreeMap<Object, NFAState>(new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                return ("" + o1).compareTo("" + o2); //null
            }
        });
    }

    /**
     * Creates a new transition from this NFAState to the target NFAState.
     * Target state and input object cannot be null. <b>NOTE:</b> There will be
     * only one target NFAState for a given input; new transitions will override
     * existing one.
     */
    public void addTransition(NFAState state_, Object input_) {
        assert (state_ != null);
        assert (input_ != null);

        _transitions.put(input_, state_);
    }

    /**
     * Creates a new epsilon transition from this NFAState to the target
     * NFAState. Target state cannot be null.
     */
    public void addTransition(NFAState state_) {
        assert (state_ != null);

        _epsilons.add(state_);
    }

    /**
     * Returns all target states of the epsilon transitions from this NFAState.
     */
    public Set<NFAState> getStates() {
        return Collections.unmodifiableSet(_epsilons);
    }

    /**
     * Returns the target state associated with the given input object. Null
     * will be returned if no such target state.
     */
    public NFAState getState(Object input_) {
        return _transitions.get(input_);
    }

    /**
     * Returns all input objects this NFAState can accept.
     */
    public Set<Object> getInputs() {
        return _transitions.keySet();
    }
    
    /**
     * Returns the closure of this NFAState.
     */
    public NFAClosure getClosure() {
        return new NFAClosure(this);
    }
    
    
    
    
    /**
     * Callback interface contains event handlers to be called in the visitor.
     */
    public interface Callback {
        /**
         * Called for epsilon transitions. Ret value determines whether
         * should follow this transition or not.
         */
        public boolean on(NFAState src_, NFAState target_);
        
        /**
         * Called for normal transitions.
         */
        public boolean on(NFAState src_, NFAState target_, Object input_);
    }
    
    
    /**
     * Helper method used to visit NFA. Unlike the previous implementation,
     * this method visits each of the transitions rather than nodes.
     */
    public static void visit(NFAState root_, Callback cb_) {
        Set<NFAState> checked = new HashSet<NFAState>();
        Queue<NFAState> todo = new LinkedList<NFAState>();        
        todo.add(root_);
        
        while (!todo.isEmpty()) {
            NFAState state = todo.remove();
            checked.add(state);
            // first the epsilons, according to the order of target states
            for (NFAState target : state.getStates()) {
                if (!checked.contains(target)
                        && cb_.on(state, target))
                {
                    todo.add(target);
                }
            }
            
            // the normal transitions, according to the order of the inputs
            for (Object input : state.getInputs()) {
                NFAState target = state.getState(input);
                if (!checked.contains(target)
                        && cb_.on(state, target, input))
                {
                    todo.add(target);
                }
            }
        }
    }
    
    /**
     * Helper method used to get the string rep of the given NFA.
     */
    public static String dump(NFAState root_) {
        final StringBuilder sb = new StringBuilder();
        final Map<NFAState, Integer> ids = new HashMap<NFAState, Integer>();
        ids.put(root_, 0);
        
        visit(root_, new Callback() {
            public boolean on(NFAState src_, NFAState target_) {
//                if (!ids.containsKey(src_)) {
//                    ids.put(src_, ids.size());
//                }
                // NOTE - guaranteed to have source state in the map
                if (!ids.containsKey(target_)) {
                    ids.put(target_, ids.size());
                }
                sb.append(String.format("%s => %s%n",
                        ids.get(src_), ids.get(target_)));
                return true;
            }
            
            public boolean on(NFAState src_, NFAState target_, Object input_) {
                if (!ids.containsKey(target_)) {
                    ids.put(target_, ids.size());
                }
                sb.append(String.format("%s =>(%s) %s%n",
                        ids.get(src_), input_, ids.get(target_)));
                return true;
            }
        });
        
        return sb.toString();
    }
}
