package mini.java.fa.v3.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import mini.java.fa.v3.DFA;
import mini.java.fa.v3.State;

/**
 * Helper class used to compare two DFA for their equality. DFA will be equal if
 * they have the same structure, that is the same transitions with the same
 * inputs. The equality checking isn't based on states, since states can be
 * easily "renamed".
 */
public final class DFAComparator {
    private DFA               _dfaA;
    private DFA               _dfaB;
    // we need to mark checked states to avoid heap
    // overflow caused by loops
    private Map<State, State> _checkedStates;

    public DFAComparator(DFA dfaA_, DFA dfaB_) {
        assert (dfaA_ != null);
        assert (dfaB_ != null);

        _dfaA = dfaA_;
        _dfaB = dfaB_;
        _checkedStates = new HashMap<State, State>();
    }

    /**
     * Compare the two DFA by comparing their transitions recursively, starting
     * from the initial states. NOTE: this method doesn't check the type of the
     * state, eg. InitialState or AcceptableState.
     */
    public boolean compare() {
        return compare(_dfaA.getInitialState(), _dfaB.getInitialState());
    }

    /**
     * Helper function for comparing part of the DFA that starts from the
     * specified states. NOTE: this is a recursive function, it will call itself
     * to compare following states.
     */
    private boolean compare(State stateA_, State stateB_) {
        assert (stateA_ != null);
        assert (stateB_ != null);

        // we have checked "stateA_" before; if it is checked against
        // "stateB_" then we are done, they are the same(?)
        if (_checkedStates.containsKey(stateA_)) {
            return (_checkedStates.get(stateA_) == stateB_);
        }

        Set<Object> sourceInputs = _dfaA.getInputs(stateA_);
        Set<Object> targetInputs = _dfaB.getInputs(stateB_);
        // inputs must be the same
        if (!sourceInputs.equals(targetInputs)) {
            return false;
        }

        for (Object input : sourceInputs) {
            State sourceState = _dfaA.getState(stateA_, input);
            State targetState = _dfaB.getState(stateB_, input);
            assert (sourceState != null);
            assert (targetState != null);

            // before we do the recursion, save the states
            _checkedStates.put(stateA_, stateB_);
            // compare the following states recursively
            if (!compare(sourceState, targetState)) {
                return false;
            }
        }

        return true;
    }
}
