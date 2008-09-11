package mini.java.fa;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Default implementation for NFA. Similar to ImmutableDFA, ImmutableNFA will use a
 * nested builder to create instances.
 * 
 * @author Alex
 */
public final class ImmutableNFA implements NFA {
    // fake inputs used to represent epsilon transitions
    private Set<Object> _epsilons;
    private DFA _dfa;

    @Override
    public DFA buildDFA() {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public Set<State> closure(State from, Object input) {
        // first get the closure of the source state
        Set<State> todo = closure(from);
        
        Set<State> states = new HashSet<State>();
        for (State state : todo) {
            // valid transitions from the closure
            // NOTE: target state can be null;
            State to = _dfa.getState(state, input);
            
            // add the target state and its closure
            // NOTE: closure will include the target state itself
            states.addAll(closure(to));
        }
        
        return states;
    }


    @Override
    public Set<State> closure(State from) {
        Set<State> ret = new HashSet<State>();
        Set<State> done = new HashSet<State>();
        List<State> todo = new LinkedList<State>();
       
        todo.add(from); // start from the source state
        while (!todo.isEmpty()) {
            State source = todo.remove(0); // remove the head

            Set<Object> inputs = _dfa.getInputs(source);
            // retain only epsilon transitions
            inputs.retainAll(_epsilons);

            // add target states for epsilon transitions
            for (Object input : inputs) {
                State target = _dfa.getState(source, input);
                // add the target to the todo list
                if (!done.contains(target)) {
                    todo.add(target);
                }
            }
            // add source state to the result set
            ret.add(source);
            // mark source state as "done"
            done.add(source);
        }
        
        return ret;
    }


    @Override
    public InitialState getInitialState() {
        return _dfa.getInitialState();
    }


    @Override
    public Set<Object> getInputs(State from) {
        Set<Object> inputs = new HashSet<Object>();
        // first get the closure of the source state
        for (State state : closure(from)) {
            // then add all valid inputs from the closure
            inputs.addAll(_dfa.getInputs(state));
        }
        // remove fake inputs for epsilon transitions
        inputs.removeAll(_epsilons);
        
        return inputs;
    }

    /**
     * Nested builder class for constructing ImmutableNFA objects.
     * NFA.Builder uses DFA.Builder internally as composition instead
     * of inherit from DFA.Builder.
     *
     * @author Alex
     */
    public static final class Builder {
        private ImmutableDFA.Builder _dfaBuilder;
        
        // cached objects for implemetation of "==" operation
        private ImmutableDFA _cachedDFA;
        private ImmutableNFA _cachedInstance;
        
        // fake inputs used to represent epsilon transitions
        private Set<Object> _epsilons;
        
        public Builder() {
            _dfaBuilder = new ImmutableDFA.Builder();
            _epsilons = new HashSet<Object>();
        }
        
        /**
         * Add a normal transition to the NFA being built. Will call the underlying
         * DFA.Builder to add the transition. See DFA.Builder for details.
         */
        public void addTransition(State from_, State to_, Object input_) {
            assert from_  != null;
            assert to_    != null;
            assert input_ != null;
            _dfaBuilder.addTransition(from_, to_, input_);
        }
        
        /**
         * Add an epsilon transition to the NFA being built. A fake input will be
         * created for this transition to simulate an epsilon transition.
         */
        public void addTransition(State from_, State to_) {
            assert from_ != null;
            assert to_   != null;
            Object epsilon = new Object();
            _epsilons.add(epsilon);
            _dfaBuilder.addTransition(from_, to_, epsilon);
        }
        
        /**
         * Factory method which creats an ImmutableNFA instance. Since this NFA
         * implementaion is immutable, it's possible to use "==" to check the equality
         * of two NFA. So the same NFA will be returned if there are no new valid
         * transitions are added bewteen two calls of this method.
         * 
         * @return an instance of ImmutableNFA; null if no valid NFA can be created.
         */
        public ImmutableNFA buildNFA() {
            ImmutableDFA dfa = _dfaBuilder.buildDFA();

            // If nothing changed, return the cached inistance
            if (dfa != _cachedDFA && dfa != null) {
                _cachedDFA = dfa;
                _cachedInstance = new ImmutableNFA();                
                _cachedInstance._dfa = dfa;
                // Defensive copy to protect the immutable instance
                _cachedInstance._epsilons = new HashSet<Object>(_epsilons);
            }
            
            // null will be returned if no instance can be created
            return _cachedInstance;
        }
    }

}
