package mini.java.fa.v3.helper;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mini.java.ComponentFactory;
import mini.java.fa.v3.Acceptable;
import mini.java.fa.v3.AcceptableInitialState;
import mini.java.fa.v3.AcceptableState;
import mini.java.fa.v3.DFA;
import mini.java.fa.v3.DFABuilder;
import mini.java.fa.v3.InitialState;
import mini.java.fa.v3.NFA;
import mini.java.fa.v3.State;

public class NFAConvertor {
    public static DFA convert(NFA nfa_) {
        assert(nfa_ != null);
        // mapping from set of NFA states (closures) to new DFA states
        // NOTE: this also serves as the "flags" for checked states
        Map<Set<State>, State> newStates = new HashMap<Set<State>, State>();
        
        // the default DFABuilder
        DFABuilder builder = ComponentFactory.createDFABuilder();
        
        // 1. get the closure for the initial state
        InitialState initialState = nfa_.getInitialState();
        Set<State> initialClosure = nfa_.closure(initialState);
        
        // 2. create a new state for the initial closure
        newStates.put(initialClosure, isAcceptable(initialClosure)
                ? new AcceptableInitialState() : new InitialState());
        
        // the "todo" list, containing unchecked states
        List<Set<State>> uncheckedStates = new LinkedList<Set<State>>(
                Collections.singleton(initialClosure));
        while (!uncheckedStates.isEmpty()) {
            Set<State> sourceClosure = uncheckedStates.remove(0);
            State sourceState = newStates.get(sourceClosure);
            
            // 3. walk through all possible inputs
            for (Object input : getInputs(nfa_, sourceClosure)) {
                // 4. get closure for the target states
                Set<State> targetClosure = closure(nfa_, sourceClosure, input);
                
                // 5. create new state for the target closure if there isn't one
                if (!newStates.containsKey(targetClosure)) {
                    newStates.put(targetClosure, isAcceptable(targetClosure)
                            ? new AcceptableState() : new State());
                    uncheckedStates.add(targetClosure);
                }
                
                // 6. add the corresponding transition to the DFA
                State targetState = newStates.get(targetClosure);
                builder.addTransition(sourceState, targetState, input);
            }
        }
        
        return builder.buildDFA();
    }
    
    // helper function for checking whether a closure contains acceptable state
    private final static boolean isAcceptable(Set<State> closure_) {
        assert (closure_ != null);
        
        for (State state : closure_) {
            if (state instanceof Acceptable)
                return true;
        }
        return false;
    }
    
    // helper function used to get possible inputs for a closure
    private final static Set<Object> getInputs(NFA nfa_, Set<State> closure_) {
        assert(nfa_     != null);
        assert(closure_ != null);
        
        Set<Object> inputs = new HashSet<Object>();
        for (State state : closure_) {
            // add all valid inputs for the closure
            inputs.addAll(nfa_.getInputs(state));
        }
        return inputs;
    }
    
    // helper function used to get the target closure from the source closure
    private final static Set<State> closure(NFA nfa_, Set<State> closure_, Object input_) {
        assert(nfa_     != null);
        assert(closure_ != null);
        assert(input_   != null);
        
        Set<State> targetClosure = new HashSet<State>();
        for (State state : closure_) {
            // add the target state and its closure          
            targetClosure.addAll(nfa_.closure(state, input_));
        }        
        return targetClosure;
    }
}
