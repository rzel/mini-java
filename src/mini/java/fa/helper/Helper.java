package mini.java.fa.helper;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import mini.java.ComponentFactory;
import mini.java.fa.AcceptableNFAState;
import mini.java.fa.NFAClosure;
import mini.java.fa.NFAState;
import mini.java.fa.adapter.V3Adapter;
import mini.java.fa.v3.AcceptableInitialState;
import mini.java.fa.v3.AcceptableState;
import mini.java.fa.v3.DFA;
import mini.java.fa.v3.DFABuilder;
import mini.java.fa.v3.InitialState;
import mini.java.fa.v3.NFA;
import mini.java.fa.v3.State;


public final class Helper {
    
    // Comparison based on object's string representation
    protected final static Comparator<Object> STR_CMP = new Comparator<Object>() {
        public int compare(Object s1, Object s2) {
            return ("" + s1).compareTo("" + s2);
        }
    };
    
    /**
     * Graph traversal algorithm implementation. Each node will be visited once.
     */
    public static <T> void visit(T node_, IFinder<T> finder_) {
        Set<T> checkedNodes = new HashSet<T>();
        Queue<T> uncheckedNodes = new LinkedList<T>(Collections.singleton(node_));
        
        while (!uncheckedNodes.isEmpty()) {
            T node = uncheckedNodes.remove();
            checkedNodes.add(node); // mark the node as checked
            
            for (T next : finder_.findNext(node)) {
                if (!checkedNodes.contains(next)) {
                    uncheckedNodes.add(next);
                }
            }
        }
    }
    
   
    /**
     * dumpString() for V3 interface
     */
    public static String dumpString(DFA dfa_) {
        final DFA dfa = dfa_;
        final StringBuilder sb = new StringBuilder();
        final Map<State, Integer> ids = new HashMap<State, Integer>();
        
        State init = dfa_.getInitialState();
        if (init == null) {
            throw new IllegalArgumentException("Invalid DFA: initial state is null!");
        }
        ids.put(init, 0);
        
        visit(init, new IFinder<State>() {
            public Queue<State> findNext(State node_) {
                if (node_ == null) {
                    throw new IllegalArgumentException("State cannot be null!");
                }
                Queue<State> ret = new LinkedList<State>();
                
                Set<Object> inputs = new TreeSet<Object>(new Comparator<Object>() {
                    public int compare(Object o1, Object o2) {
                        return ("" + o1).compareTo("" + o2); //null
                    }
                });
                inputs.addAll(dfa.getInputs(node_));
                
                for (Object input : inputs) {
                    State target = dfa.getState(node_, input);
                    if (target == null) {
                        throw new IllegalArgumentException("Unable to find the corresponding target state for ("
                                + node_ + ", " + input + ")");
                    }
                    if (!ids.containsKey(target)) {
                        ids.put(target, ids.size());
                    }
                    ret.add(target);
                    // XXX - is this method guaranteed to be called the same order
                    // as the states in the final queue?
                    sb.append(String.format("%s =>(%s) %s%n",
                            ids.get(node_), input, ids.get(target)));
                }
                return ret;
            }
        });
        return sb.toString();
    }
    
    
    /**
     * Yet another implementation for dump().
     */
    public static String dump(NFAState root_) {
        final StringBuilder sb = new StringBuilder();
        final Map<NFAState, Integer> ids = new HashMap<NFAState, Integer>();
        ids.put(root_, 0);
        
        visit(root_, new NFAStateFinder(
                new IFinderCallback<NFAState>() {
                    public boolean onNext(NFAState src_, NFAState dest_, Object input_) {
                        if (input_ == null) {
                            // for epsilons, we don't have a deterministic order
                            throw new UnsupportedOperationException("dump() for NFA is not supported");
                        }
                        if (!ids.containsKey(dest_)) {
                            ids.put(dest_, ids.size());
                        }
                        sb.append(String.format("%s =>(%s) %s%n",
                                ids.get(src_), input_, ids.get(dest_)));
                        return true;
                    }
                }));
        return sb.toString();
    }
    
    /**
     * Yet another implementation for findClosure().
     */
    public static Set<NFAState> findClosure(NFAState state_) {
        final Set<NFAState> ret = new HashSet<NFAState>();
        ret.add(state_); // closure includes itself
        
        visit(state_, new NFAStateFinder(
                new IFinderCallback<NFAState>() {
                    public boolean onNext(NFAState src_, NFAState dest_, Object input_) {
                        if (input_ == null) {
                            ret.add(dest_);
                            return true;
                        }
                        return false; // skip normal transitions
                    }
                }));
        return ret;
    }
    
    /**
     * Collapses the epsilons in the give NFA to create a DFA.
     */
    public static NFAState collapse(NFAState root_) {
        // mapping from closures to new DFA states
        final Map<NFAClosure, NFAState> mapping = new HashMap<NFAClosure, NFAState>();
        
        
        NFAClosure init = root_.getClosure();
        NFAState dfa = init.isAcceptable()
            ? new AcceptableNFAState() : new NFAState();
        mapping.put(init, dfa);
        
        visit(init, new NFAClosureFinder(
                new IFinderCallback<NFAClosure>() {
                    public boolean onNext(NFAClosure src_, NFAClosure dest_, Object input_) {
                        assert(input_ != null);
                        
                        if (!mapping.containsKey(dest_)) {
                            mapping.put(dest_,
                                    dest_.isAcceptable()
                                        ? new AcceptableNFAState() : new NFAState());
                        }
                        // add the corresponding transitions in the newly created DFA
                        NFAState src = mapping.get(src_);
                        src.addTransition(mapping.get(dest_), input_);
                        return true;
                    }
                }));
        return dfa;
    }
    
    /**
     * Alternative implementation of NFAConvertor.
     */
    public static DFA collapse(NFA nfa_) {
        if (! (nfa_ instanceof V3Adapter)) {
            throw new UnsupportedOperationException("Only V3Adapter impl is supported");
        }
        
        NFAState root = ((V3Adapter)nfa_).getUnderlying();
        // NFA(v4) --> DFA(v4) --> DFA(v3)
        return convert(collapse(root));
    }
    
    /**
     * Helper method used to convert V4 back to V3
     */
    public static DFA convert(NFAState root_) {
        final Map<NFAState, State> mapping = new HashMap<NFAState, State>();    
        final DFABuilder builder = ComponentFactory.createDFABuilder();
        
        State init = (root_ instanceof AcceptableNFAState)
            ? new AcceptableInitialState() : new InitialState();
        mapping.put(root_, init);
        
        visit(root_, new NFAStateFinder(
                new IFinderCallback<NFAState>() {
                    public boolean onNext(NFAState src_, NFAState dest_, Object input_) {
                        if (!mapping.containsKey(dest_)) {
                            mapping.put(dest_,
                                    (dest_ instanceof AcceptableNFAState)
                                        ? new AcceptableState() : new State());
                        }
                        builder.addTransition(mapping.get(src_), mapping.get(dest_), input_);
                        return true;
                    }
                }));
        return builder.buildDFA();
    }
}
