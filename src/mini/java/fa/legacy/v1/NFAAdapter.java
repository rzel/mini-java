package mini.java.fa.legacy.v1;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mini.java.fa.Acceptable;
import mini.java.fa.DFA;
import mini.java.fa.DFABuilder;
import mini.java.fa.ImmutableDFA;
import mini.java.fa.InitialState;
import mini.java.fa.NFA;
import mini.java.fa.NFABuilder;
import mini.java.fa.State;

/**
 * Adapter from a SimpleFA to the NFA interface. SimpleFA is legacy
 * implementation for NFA. It is hard to use because it can only take Character
 * as input. NFAAdapter adds the ability for SimpleFA to accept arbitrary
 * objects as input by creating a mapping from objects to characters. This only
 * eases the use of the SimpleFA implementation. It doesn't eliminate the limit
 * imposed by the limited number of the characters.
 * 
 * @author Alex
 */

public class NFAAdapter implements NFA, NFABuilder {
    // the underlying SimpleFA object
    private SimpleFA               simpleFA;

    // mapping from NFA input to SimpleFA input
    private Map<Object, Character> objectToCharacter;

    // mapping from SimpleFA input to NFA input
    private Map<Character, Object> characterToObject;

    // used to track used characters by simpleFA
    private char                   charValue;

//    // used to track added states; SimpleFA doesn't track states;
//    private Set<State>             states;
    
    // cached initial state
    private InitialState           _initialState;

//    public Set<Object> getInputs() {
//        return objectToCharacter.keySet();
//    }

//    public Set<State> getStates() {
//        return states;
//    }

//    public Set<State> getStates(State from) {
//        Set<State> states = new HashSet<State>();
//        for (Object input : this.getInputs(from)) {
//            states.addAll(getStates(from, input));
//        }
//
//        // the behavior in NFA is slightly different than the DFA version, that
//        // NFA will also return any states reachable through an epsilon
//        // transition
//        states.addAll(this.simpleFA.e_closure(from));
//
//        return states;
//    }

//    /**
//     * Get the underlying SimpleFA
//     * 
//     * @return
//     */
//    public SimpleFA getSimpleFA() {
//        return simpleFA;
//    }

    public NFAAdapter() {
        this.simpleFA           = new SimpleFA();
        this.objectToCharacter  = new HashMap<Object, Character>();
        this.characterToObject  = new HashMap<Character, Object>();
//        this.states             = new HashSet<State>();
        this.charValue          = 'A';
    }
    
    // Helper function used to check whether the given state is
    // an initial state or an acceptable state; and update the underlying
    // simpleFA to set the special state.
    private void checkState(State state_) {
        if (state_ instanceof Acceptable) {
            simpleFA.addAcceptedState(state_);
        }
        
        if (state_ instanceof InitialState) {
            if (_initialState != null) {
                // TODO throw IllegalTransitionException
                assert(state_ == _initialState);
            } else {
                simpleFA.setInitialState(_initialState = (InitialState)state_);
            }
        }
    }

    @Override
    public Set<Object> getInputs(State from) {
        Set<Object> objectInputs = new HashSet<Object>();
        Set<State> sourceStates = new HashSet<State>();
        
        sourceStates.addAll(simpleFA.e_closure(from));
        sourceStates.add(from);
        for (State state : sourceStates) {
            Set<Character> charInput = simpleFA.getInputMixed(state);
            for (Character c : charInput) {
                Object o = characterToObject.get(c);
                // if (o != null)
                // objectInput.add(o);
                assert (o != null);
                objectInputs.add(o);
            }
        }
        return objectInputs;
    }

    @Override
    public void addTransition(State from, State to, Object input) {
        Character c = null;

//        // keep tracking states
//        states.add(from);
//        states.add(to);

        if (objectToCharacter.containsKey(input)) {
            // have seen this input before
            c = objectToCharacter.get(input);
        } else {
            // get a new character for the input
            c = new Character(charValue++);

            // update the mapping
            objectToCharacter.put(input, c);
            characterToObject.put(c, input);
        }

        // create a new input for simpleFA
        Set<Character> newInput = new HashSet<Character>(
                Collections.singleton(c));

        // add the transition
        simpleFA.addTransition(from, to, newInput);

        checkState(from);
        checkState(to);
    }

    @Override
    public void addTransition(State from, State to) {
        simpleFA.addTransition(from, to);
        
        checkState(from);
        checkState(to);
    }

//    public Set<AcceptableState> getAcceptableState() {
//        Set<AcceptableState> states = new HashSet<AcceptableState>();
//
//        for (State state : getStates()) {
//            if (state instanceof AcceptableState) {
//                states.add((AcceptableState) state);
//            }
//        }
//
//        return states;
//    }

    @Override
    public InitialState getInitialState() {
        State state = simpleFA.getInitialState();
        
        assert(state instanceof InitialState);
        assert(state == _initialState);
        
        return _initialState;
    }

    @Override
    public DFA buildDFA() {
        SimpleFA fa = simpleFA.toDFA();
        DFABuilder builder = new ImmutableDFA.Builder();
        
        // convert the SimpleFA to ImmutableDFA
        Set<State> checkedStates = new HashSet<State>();
        List<State> uncheckedStates = new LinkedList<State>();
        
        // get the initial state from the DFA
        State initialState = fa.getInitialState();
        assert(initialState instanceof InitialState);
        
        // we will start from the InitialState
        uncheckedStates.add(initialState);
        while (!uncheckedStates.isEmpty()) {
            State sourceState = uncheckedStates.remove(0);
            checkedStates.add(sourceState);
            
            Set<Character> inputs = fa.getInputMixed(sourceState);
            for (Character input : inputs) {
                Set<State> targetStates = fa.move(sourceState, Collections.singleton(input));
                assert(targetStates.size() == 1);
                
                // get the single element from the set
                State targetState = targetStates.toArray(new State[0])[0];
                
                // get the actual input for this transition
                Object objectInput = characterToObject.get(input);
                assert(objectInput != null);
                
                // add this transition
                builder.addTransition(sourceState, targetState, objectInput);
                
                if (!checkedStates.contains(targetState)) {
                    uncheckedStates.add(targetState);
                }
            }
        }
        
        return builder.buildDFA();
    }

    @Override
    public Set<State> closure(State from, Object input) {
        // never return null for collection
        Set<State> targetClosures = new HashSet<State>();

        if (objectToCharacter.containsKey(input)) {
            Character c = objectToCharacter.get(input);
            assert(c != null);
            
            // 1. get the source state and its closure
            Set<State> sourceClosure = closure(from);
            // 2. get the target states
            Set<State> targets = simpleFA.move(sourceClosure,
                    new HashSet<Character>(Collections.singleton(c)));
            
            // NOTE: SimpleFA.e_closure doesn't return the source state.
            targetClosures.addAll(simpleFA.e_closure(targets));
            targetClosures.addAll(targets);
        }

        return targetClosures;
    }

    @Override
    public Set<State> closure(State from) {
        Set<State> states = new HashSet<State>();
        // NOTE: SimpleFA.e_closure doesn't return the source state.
        states.addAll(simpleFA.e_closure(from));
        states.add(from);
        return states;
    }

    @Override
    public NFA buildNFA() {
        return (_initialState == null) ? null : this;
    }
}
