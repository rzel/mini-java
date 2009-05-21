package mini.java.fa.adapter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mini.java.fa.AcceptableNFAState;
import mini.java.fa.NFAState;
import mini.java.fa.v3.Acceptable;
import mini.java.fa.v3.DFA;
import mini.java.fa.v3.DFABuilder;
import mini.java.fa.v3.InitialState;
import mini.java.fa.v3.NFA;
import mini.java.fa.v3.NFABuilder;
import mini.java.fa.v3.State;

/**
 * This is a DFA/NFA and the corresponding DFABuilder/NFABuilder implementation
 * using the new NFAState data structure.
 * @author Alex
 */
public class V3Adapter implements DFABuilder, DFA, NFABuilder, NFA {
    private Map<State, NFAState> _states = new HashMap<State, NFAState>();
    private Map<NFAState, State> _reversedMap = new HashMap<NFAState, State>();
    private InitialState _initState;
    
    private NFAState _getNFAState(State state_) {
        if (!_states.containsKey(state_)) {
            if (state_ instanceof InitialState) {
                if (_initState != null) {
                    throw new IllegalArgumentException("Multiple initial states are not allowed: " + state_);
                }
                _initState = (InitialState)state_;
            }
            NFAState newState = (state_ instanceof Acceptable)
              ? new AcceptableNFAState()
              : new NFAState();
            _states.put(state_, newState);
            _reversedMap.put(newState, state_);
        }
        return _states.get(state_);
    }
    
    private NFAState _getNFAStateOrDie(State state_) {
        if (!_states.containsKey(state_)) {
            throw new IllegalArgumentException("Invalid state: " + state_);
        }
        return _states.get(state_);
    }
    
    /* (non-Javadoc)
     * @see mini.java.fa.v3.DFABuilder#addTransition(mini.java.fa.v3.State, mini.java.fa.v3.State, java.lang.Object)
     */
    @Override
    public void addTransition(State from_, State to_, Object input_) {
        NFAState from = _getNFAState(from_);
        NFAState to = _getNFAState(to_);
        from.addTransition(to, input_);
    }

    /* (non-Javadoc)
     * @see mini.java.fa.v3.DFABuilder#buildDFA()
     */
    @Override
    public DFA buildDFA() {
        return
          (_states.size() > 0 && _initState != null)
            ? this : null;
    }

    /* (non-Javadoc)
     * @see mini.java.fa.v3.DFA#getInitialState()
     */
    @Override
    public InitialState getInitialState() {
        return _initState;
    }

    /* (non-Javadoc)
     * @see mini.java.fa.v3.DFA#getInputs(mini.java.fa.v3.State)
     */
    @Override
    public Set<Object> getInputs(State from_) {
        return _getNFAStateOrDie(from_).getClosure().getInputs();
    }

    /* (non-Javadoc)
     * @see mini.java.fa.v3.DFA#getState(mini.java.fa.v3.State, java.lang.Object)
     */
    @Override
    public State getState(State from_, Object input_) {
        return _reversedMap.get(
                _getNFAStateOrDie(from_).getState(input_));
    }

    @Override
    public void addTransition(State from_, State to_) {
        NFAState from = _getNFAState(from_);
        NFAState to = _getNFAState(to_);
        from.addTransition(to);
    }

    @Override
    public NFA buildNFA() {
        return
        (_states.size() > 0 && _initState != null)
          ? this : null;
    }
    
    private Set<State> _getStates(Set<NFAState> states_) {
        Set<State> ret = new HashSet<State>();
        for (NFAState state : states_) {
            // TODO - do the proper check
            ret.add(_reversedMap.get(state));
        }
        return ret;
    }

    @Override
    public Set<State> closure(State from_, Object input) {
        NFAState from = _getNFAStateOrDie(from_);
        Set<NFAState> states = from.getClosure() // first the source closure
            .getClosure(input).getStates(); // then the target closure
        return _getStates(states);
    }

    @Override
    public Set<State> closure(State from_) {
        NFAState from = _getNFAStateOrDie(from_);
        Set<NFAState> states = from.getClosure().getStates();
        return _getStates(states);
    }

}
