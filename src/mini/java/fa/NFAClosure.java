package mini.java.fa;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import mini.java.fa.helper.Helper;

public class NFAClosure {
    private Set<NFAState> _states;
    
    /**
     * Constructor. Creates a closure object for a single NFAState.
     */
    public NFAClosure(NFAState state_) {
        assert(state_ != null);
        
        _states = Helper.findClosure(state_);
    }
    
    /**
     * Constructor. Creates a closure object for a collection of NFAStates.
     */
    public NFAClosure(Set<NFAState> states_) {
//        assert(states_ != null);
//        assert(!states_.isEmpty());
//        
//        _states = new HashSet<NFAState>();
//        for (NFAState state : states_) {
//            _states.addAll(Helper.findClosure(state));
//        }
        
        _states = Helper.findClosure(states_);
    }
    
    /**
     * Returns all NFAStates in this closure.
     */
    public Set<NFAState> getStates() {
        return Collections.unmodifiableSet(_states);
    }
    
    /**
     * Returns the target closure of this closure for the give input object.
     * Target closure is constructed using the target states from the source states
     * in this closure. Null will be returned if no such closure.
     */
    public NFAClosure getClosure(Object input_) {
        Set<NFAState> targetStates = new HashSet<NFAState>();
        for (NFAState state : _states) {
            if (state.getState(input_) != null) { // needs to be skipped
                targetStates.add(state.getState(input_));
            }
        }
        
        return (!targetStates.isEmpty())
            ? new NFAClosure(targetStates) : null;
    }
    
    /**
     * Helper method used to return all valid inputs for this closure.
     */
    public Set<Object> getInputs() {
        Set<Object> inputs = new HashSet<Object>();
        for (NFAState state : _states) {
            inputs.addAll(state.getInputs());
        }
        
        return inputs;
    }
    
    /**
     * Helper method used to determining whether this closure contains
     * acceptable NFAState or not.
     */
    public boolean isAcceptable() {
        for (NFAState state : _states) {
            if (state instanceof Acceptable) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_states == null) ? 0 : _states.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NFAClosure other = (NFAClosure) obj;
        if (_states == null) {
            if (other._states != null)
                return false;
        } else if (!_states.equals(other._states))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return _states.toString();
    }
    
    
}
