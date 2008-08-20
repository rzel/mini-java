package mini.java.fa;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mini.java.fa.legacy.SimpleFA;
import mini.java.fa.legacy.State;

/**
 * Adapter from a SimpleFA to the NFiniteAutomaton interface. SimpleFA is legacy
 * implementation for NFA. It is hard to use because it can only take Character
 * as input. SimpleFAAdapter adds the ability for SimpleFA to accept arbitrary
 * objects as input by creating a mapping from objects to characters. This only
 * eases the use of the SimpleFA implementation. It doesn't eliminate the limit
 * imposed by the limited number of the characters.
 * 
 * @author Alex
 * 
 */
public class SimpleFAAdapter implements NFiniteAutomaton {
    // the underlying SimpleFA object
    private SimpleFA               simpleFA          = null;

    // mapping from NFiniteAutomaton input to SimpleFA input
    private Map<Object, Character> objectToCharacter = new HashMap<Object, Character>();

    // mapping from SimpleFA input to NFiniteAutomaton input
    private Map<Character, Object> characterToObject = new HashMap<Character, Object>();

    // used to track used characters by simpleFA
    private char                   charValue         = '\000';

    /**
     * Get the underlying SimpleFA
     * @return
     */
    public SimpleFA getSimpleFA() {
        return simpleFA;
    }

    /**
     * Constructor. SimpleFAAdapter should be an immutable object.
     * @param simpleFA
     */
    public SimpleFAAdapter(SimpleFA simpleFA) {
        this.simpleFA = simpleFA;
    }

//    /**
//     * Set the underlying SimpleFA
//     * @param simpleFA
//     */
//    public void setSimpleFA(SimpleFA simpleFA) {
//        this.simpleFA = simpleFA;
//    }

    public void addTransition(State from, State to, Object input) {
        Character c = null;

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

        // TODO: check out the specific behavior of SimpleFA in case the same
        // accepted state being added multiple times
        // TODO: also check "from" state
        if (to instanceof AcceptedState) {
            simpleFA.addAcceptedState(to);
        }
    }

    public Set<State> reachableStates(State from, Object input) {
        // never return null for collection
        Set<State> states = new HashSet<State>();

        if (objectToCharacter.containsKey(input)) {
            Character c = objectToCharacter.get(input);
            Set<Character> newInput = new HashSet<Character>(
                    Collections.singleton(c));
            states.addAll(simpleFA.e_closure(simpleFA.move(
                    simpleFA.e_closure(from), newInput)));
        }

        return states;
    }

    public Set<Object> possibleInputs(State from) {
        // TODO: character inputs and object inputs should have one-to-one mapping
        // TODO: try to find such an one-to-one mapping implementation
        Set<Character> charInput = simpleFA.getInputMixed(from);
        Set<Object> objectInput = new HashSet<Object>();

        for (Character c : charInput) {
            Object o = characterToObject.get(c);
            if (o != null)
                objectInput.add(o);
        }
        return objectInput;
    }

    public void addTransition(State from, State to) {
        simpleFA.addTransition(from, to);

        // TOOD: do the same thing for "from" state
        if (to instanceof AcceptedState) {
            simpleFA.addAcceptedState(to);
        }
    }

    public Set<State> reachableStates(State from) {
        return simpleFA.e_closure(from);
    }
}
