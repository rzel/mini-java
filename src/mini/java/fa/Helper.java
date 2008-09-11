package mini.java.fa;

import java.util.HashMap;
import java.util.Map;

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
}
