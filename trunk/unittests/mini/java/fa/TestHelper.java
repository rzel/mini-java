package mini.java.fa;

import java.util.HashMap;
import java.util.Map;

import mini.java.fa.v3.DFA;
import mini.java.fa.v3.DFABuilder;
import mini.java.fa.v3.InitialState;
import mini.java.fa.v3.NFA;
import mini.java.fa.v3.NFABuilder;
import mini.java.fa.v3.State;
import mini.java.fa.v3.impl.ImmutableDFA;
import mini.java.fa.v3.impl.ImmutableNFA;

public class TestHelper {    
    /**
     * Helper function used to build NFA. It takes a string
     * parameter which represents a NFA. The string has the form of:
     * "ABa,ABb,AB,CD"; in which:
     * <ul>
     * <li>Each normal transition is represented by three characters;
     * <li>Epsilon transitions are represented by twocharacters;
     * <li>Transitions are seperated by commas;
     * <li>The first source state will be treated as an initial state;
     * </ul>
     */
    protected static final NFA buildNFA(String rep_, NFABuilder builder_) {
        assert(rep_ != null);
        
        // "states" is a mapping from character representation of the
        // state to the actual nfa state
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
                builder_.addTransition(sourceState, targetState);
            } else {
                // add a normal transition
                builder_.addTransition(sourceState, targetState, transition.charAt(2));
            }
        }
        
        // build and return the NFA
        return builder_.buildNFA();
    }
    
    protected static final NFA buildNFA(String rep_) {
        // use the default NFABuilder
        return buildNFA(rep_, new ImmutableNFA.Builder());
    }

    /**
     * Helper function used to build DFA. It takes a string parameter which
     * represents a DFA. The string has the form of: "ABa,ABb"; in which:
     * <ul>
     * <li>Each transition is represented by three characters;
     * <li>Transitions are seperated by commas;
     * <li>The first source state will be treated as the initial state;
     * </ul>
     */
    protected static final DFA buildDFA(String rep_) {
        assert(rep_ != null);
        
        // use the only implementation of DFA.Builder
        DFABuilder builder = new ImmutableDFA.Builder();
        
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
}
