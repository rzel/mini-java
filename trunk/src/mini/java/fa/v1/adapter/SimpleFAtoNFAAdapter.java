package mini.java.fa.v1.adapter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import mini.java.fa.v1.SimpleFA;
import mini.java.fa.v3.Acceptable;
import mini.java.fa.v3.DFA;
import mini.java.fa.v3.DFABuilder;
import mini.java.fa.v3.InitialState;
import mini.java.fa.v3.NFA;
import mini.java.fa.v3.NFABuilder;
import mini.java.fa.v3.State;

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

public class SimpleFAtoNFAAdapter
        implements NFA, NFABuilder, DFA, DFABuilder {
    private SimpleFA       simpleFA;
    private InputConvertor _inputConvertor;

    // cached initial state
    private InitialState   _initialState;

    public SimpleFAtoNFAAdapter() {
        this(new SimpleFA(), new InputConvertor());
    }
    
    /**
     * Wrapper around an existing SimpleFA object.
     */
    public SimpleFAtoNFAAdapter(SimpleFA fa_, InputConvertor inputConvertor_) {
        this.simpleFA = fa_;
        this._initialState = (InitialState)fa_.getInitialState();
        this._inputConvertor = inputConvertor_;
    }
    
    public SimpleFA getSimpleFA() {
        return this.simpleFA;
    }
    
    public InputConvertor getInputConvertor() {
        return _inputConvertor;
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
                Object o = _inputConvertor.convert(c);
                assert (o != null);
                objectInputs.add(o);
            }
        }
        return objectInputs;
    }

    @Override
    public void addTransition(State from, State to, Object input) {
        Character c = _inputConvertor.convert(input);
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

    @Override
    public InitialState getInitialState() {
        State state = simpleFA.getInitialState();
        
        assert(state instanceof InitialState);
        assert(state == _initialState);
        
        return _initialState;
    }

    @Override
    public Set<State> closure(State from, Object input) {
        Character c = _inputConvertor.convert(input);
        // never return null for collection
        Set<State> targetClosures = new HashSet<State>();

        // 1. get the source state and its closure
        Set<State> sourceClosure = closure(from);
        // 2. get the target states
        Set<State> targets = simpleFA.move(sourceClosure,
                new HashSet<Character>(Collections.singleton(c)));

        // NOTE: SimpleFA.e_closure doesn't return the source state.
        targetClosures.addAll(simpleFA.e_closure(targets));
        targetClosures.addAll(targets);
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
        if (_initialState == null) {
            return null;
        }
        return this;
    }

    @Override
    public State getState(State from, Object input) {
        Character c = _inputConvertor.convert(input);
        Set<State> ret = simpleFA.move(from, Collections.singleton(c));
        if (ret.isEmpty()) {
            return null;
        }
        // if we have more than one transition, throw
        if (ret.size() != 1) {
            throw new RuntimeException("DFA being used as NFA");
        }
        return ret.iterator().next();
    }

    @Override
    public DFA buildDFA() {
        if (_initialState == null) {
            return null;
        }        
        return this;
    }
}
