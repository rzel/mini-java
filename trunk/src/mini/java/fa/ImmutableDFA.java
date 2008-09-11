package mini.java.fa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * ImmutableDFA is an implemetation of DFA which appreciates the immutable
 * aspect of the interface. ImmutableDFA includes a builder inner class which
 * can be used to construct ImmutableDFA objects. Once an ImmutableDFA object is
 * created, you won't be able to alter it.
 * 
 * @author Alex
 */
public final class ImmutableDFA implements DFA {
    // transition collection, formed by a mapping from source states to
    // input/target state pairs. In order to enforce that each source state
    // can reach a single target state through the same input object, the input
    // is made the key in the target pair.
    private Map<State, Map<Object, State>> transitions;
    
    // keep track of the initial state
    private InitialState initialState;

    // private construct to prevent invalid DFA being created
    private ImmutableDFA() {
    }

    @Override
    public InitialState getInitialState() {
        return initialState;
    }

    @Override
    public Set<Object> getInputs(State from) {
        // never return null for collections
        Set<Object> inputs = new HashSet<Object>();
        if (transitions.containsKey(from)) {
            inputs.addAll(transitions.get(from).keySet());
        }
        return inputs;
    }

    @Override
    public State getState(State from, Object input) {
        if (transitions.containsKey(from)) {
            Map<Object, State> targets = transitions.get(from);
            if (targets.containsKey(input)) {
                return targets.get(input);
            }
        }
        return null;
    }
    
    /**
     * Nested builder class used to create ImmutableDFA instances. This is
     * the only way to create objects of ImmutableDFA. See java builder pattern
     * for detail.
     *
     * @author Alex
     */
    public static final class Builder {
        // cached instance for implementation of "==" operation;
        private ImmutableDFA _cachedInstance;
        
        private Map<State, Map<Object, State>> transitions =
            new HashMap<State, Map<Object, State>>();
        
        // Keep track of the initial state. No DFA will be created
        // if initialState is null. NOTE: each DFA must have one and only
        // one initial state. Newly added initial states will override
        // previous ones.
        private InitialState initialState;

        /**
         * Add a transition to the DFA being built. A transition consists a
         * source state, a target state and an input which will trigger the
         * transition. Note: a source/target states pair can only be associated
         * with a single input; new inputs will override previous ones.
         * 
         * InitialState and AcceptableState can be set be passing corresponding
         * states as parameters. Note that a DFA can have no more than one
         * InitialState. Invalid transitions will be ignored.
         * 
         * @param from -- source state
         * @param to -- target state
         * @param input -- input object
         */
        public void addTransition(State from, State to, Object input) {
            
            
//            // ignore invalid states
//            //if (from == null || to == null) { return; }
//            // Null source state will definitely lead to an invalid DFA;
//            if (to == null) { return; }
//            
//            // ignore invalid inputs
//            if (input == null) { return; }
            
            assert (from  != null);
            assert (to    != null);
            assert (input != null);
    
//            // set the source state to be the initial state
//            if (from instanceof InitialState && from != initialState) {
//                if (initialState == null) {
//                    initialState = (InitialState)from;
//                } else {
//                    // A DFA cannot have more than one initial state;
//                    // ignore this transition
//                    return;
//                }                
//            }
            
            // if the source state is an initial state, set it to be
            // the only initial state of the DFA
            if (from instanceof InitialState) {
                if (initialState == null) {
                    initialState = (InitialState)from;
                } else {
                    assert (from == initialState);
                }
            }
            
            // the target state can be an initial state only after the
            // initial state has been set and the target state is the
            // same as the initial state
            if (to instanceof InitialState) {
                assert (to == initialState);
            }
            
            Map<Object, State> targets;
            if (transitions.containsKey(from)) {
                targets = transitions.get(from);
                targets.put(input, to);
            } else {
                // if we haven't seen this source state before, create a new
                // target collection for it
                targets = new HashMap<Object, State>();
                targets.put(input, to);
                transitions.put(from, targets);
            }
            
            // clear the cache, so new instance will be created
            _cachedInstance = null;
        }

        /**
         * Factory method which returns an ImmutableDFA instance. This method
         * can be called multiple times. Same instance will be returned if no
         * transitions are added.
         * 
         * NOTE: each DFA must have one and only one initial state. If the initial
         * state haven't been provided yet, no DFA will be created.
         * 
         * @return an instance of ImmutableDFA; null if no valid ImmutableDFA can
         * be created. 
         */
        public ImmutableDFA buildDFA() {
            // a new instance needs to be created
            if (_cachedInstance == null) {

                // create an instance only if initialState is available;
                if (initialState != null) {
                    _cachedInstance = new ImmutableDFA();
                    // Defensive copy to protect the immutable instance
                    _cachedInstance.transitions = new HashMap<State, Map<Object, State>>(
                            transitions);
                    // initialState can be assigned directly since State is
                    // immutable object itself.
                    _cachedInstance.initialState = initialState;
                }
            }
            return _cachedInstance;
        }
    }
}
