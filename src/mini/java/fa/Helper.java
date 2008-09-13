package mini.java.fa;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A collection of helper functions for finite automata operations.
 *
 * @author Alex
 */
public final class Helper {    
    /**
     * Helper function used to build NFA. It takes a string
     * parameter which represents a NFA. The string has the form of:
     * "ABa,ABb,AB,CD"; in which each normal transition is represented by
     * three characters, one for source state, one for target state and one
     * for the input object; epsilon transitions are represented by two
     * characters; transitions are seperated by commas; the first source state
     * will be treated as an initial state.
     */
    public static final ImmutableNFA buildNFA(String rep_) {
        assert(rep_ != null);
        assert(!rep_.isEmpty());
        
        // use the only implementation of NFA.Builder
        ImmutableNFA.Builder builder = new ImmutableNFA.Builder();
        
        // "states" is a mapping from character representation of the
        // state to the actual state
        Map<Character, State> states = new HashMap<Character, State>();
        
        // the first source state should be treated as an initial state
        states.put(rep_.charAt(0), new InitialState());
        
        for (String transition : rep_.split(",")) {
            assert(transition.length() == 2 || transition.length() == 3);
            
            Character source = transition.charAt(0);
            Character target = transition.charAt(1);
            
            // create the corresponding states if they don't exist
            if (!states.containsKey(source)) {
                states.put(source, new State());
            }            
            if (!states.containsKey(target)) {
                states.put(target, new State());
            }
            
            State sourceState = states.get(source);
            State targetState = states.get(target);
            if (transition.length() == 2) {
                // add an epsilon transition
                builder.addTransition(sourceState, targetState);
            } else {
                // add a normal transition
                builder.addTransition(sourceState, targetState, transition.charAt(2));
            }
        }
        
        // build and return the NFA
        return builder.buildNFA();
    }

    /**
     * Helper function used to build DFA. It takes a string parameter which
     * represents a DFA. The string has the form of: "ABa,ABb"; in which each
     * transition is represented by three characters, one for source state, one
     * for target state and one for the input object; transitions are seperated
     * by commas; the first source state will be treated as the initial state.
     */
    public static final ImmutableDFA buildDFA(String rep_) {
        assert(rep_ != null);
        assert(!rep_.isEmpty());
        
        // use the only implementation of NFA.Builder
        ImmutableDFA.Builder builder = new ImmutableDFA.Builder();
        
        // "states" is a mapping from character representation sof the
        // state to the actual states
        Map<Character, State> states = new HashMap<Character, State>();
        
        // the first source state should be treated as an initial state
        states.put(rep_.charAt(0), new InitialState());
        
        for (String transition : rep_.split(",")) {
            assert(transition.length() == 3);
            
            Character source = transition.charAt(0);
            Character target = transition.charAt(1);
            Object input = transition.charAt(2);
            
            // create the corresponding states if they don't exist
            if (!states.containsKey(source)) {
                states.put(source, new State());
            }            
            if (!states.containsKey(target)) {
                states.put(target, new State());
            }
            
            // get the states adn add the transition
            State sourceState = states.get(source);
            State targetState = states.get(target);
            builder.addTransition(sourceState, targetState, input);
        }
        
        // build and return the DFA
        return builder.buildDFA();
    }
    
    /**
     * Helper class used to compare two DFA for their equality. DFA will be
     * equal if they have the same structure, that is the same transitions with
     * the same inputs. The equality checking isn't based on states, since
     * states can be easily "renamed".
     */
    public static final class Comparator {
        private DFA _dfaA;
        private DFA _dfaB;
        // we need to mark checked states to avoid heap
        // overflow caused by loops
        private Map<State,State> _checkedStates;
        
        public Comparator(DFA dfaA_, DFA dfaB_) {
            assert(dfaA_ != null);
            assert(dfaB_ != null);
            
            _dfaA = dfaA_;
            _dfaB = dfaB_;
            _checkedStates = new HashMap<State,State>();
        }
        
        /**
         * Compare the two DFA by comparing their transitions recursively, starting
         * from the initial states.
         * NOTE: this method doesn't check the type of the state, eg. InitialState or
         * AcceptableState.
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
}
