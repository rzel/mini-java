package mini.java.fa;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * ImmutableDFA is an implemetation of DFA which appreciates the immutable
 * aspect of the interface. ImmutableDFA includes a builder inner class which
 * can be used to construct ImmutableDFA objects. But once a object is created,
 * you won't be able to alter it.
 * 
 * @author Alex
 */
public final class ImmutableDFA implements DFA {
    // transition collection, formed by a mapping from source states to
    // input/target state pairs. In order to enforce that each source state
    // can reach a single target state through the same input object, the input
    // is made the key in the target paris for that source state.
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
            Map<Object, State> targets;
            
            // ignore invalid states
            //if (from == null || to == null) { return; }
            // Null source state will definitely lead to an invalid DFA;
            if (to == null) { return; }
            
            // ignore invalid inputs
            if (input == null) { return; }
            
            // target state must not be initial state
            if (to instanceof InitialState) { return; }
            
            // set the source state to be the initial state
            if (from instanceof InitialState && from != initialState) {
                if (initialState == null) {
                    initialState = (InitialState)from;
                } else {
                    // A DFA cannot have more than one initial state;
                    // ignore this transition
                    return;
                }                
            }
            
            if (transitions.containsKey(from)) {
                targets = transitions.get(from);
                targets.put(input, to);
            } else {
                // If we haven't seen this source state before, create a new
                // target collection.
                targets = new HashMap<Object, State>();
                targets.put(input, to);
                transitions.put(from, targets);
            }
        }

        /**
         * Factory method which returns an ImmutableDFA instance. This method
         * can be called multiple times. Each call will return a different instance.
         * Through technically speaking, it's possible to return the same instance
         * if the underlying data haven't changed. We leave this out for simplicity.
         * 
         * NOTE: each DFA must have one and only one initial state. If the initial
         * state haven't been provided yet, no DFA will be created.
         * 
         * @return an instance of ImmutableDFA; null if the builder haven't got
         * enough data to create a valid ImmutableDFA instance. 
         */
        public ImmutableDFA buildDFA() {
            ImmutableDFA instance = null;
            
            // create instance only if initialState is available;
            if (initialState != null) {
                instance = new ImmutableDFA();
                // Defensive copy to protect the immutable instance
                instance.transitions =
                    new HashMap<State, Map<Object, State>>(transitions);
                // initialState can be assigned directly since State is immutable
                // object itself.
                instance.initialState = initialState;
            }            
            return instance;
        }
    }
}
