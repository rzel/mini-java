package mini.java.fa.legacy.v1;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mini.java.fa.AcceptableState;
import mini.java.fa.InitialState;
import mini.java.fa.NFA;
import mini.java.fa.State;

/**
 * Adapter from a SimpleFA to the NFA interface. SimpleFA is legacy
 * implementation for NFA. It is hard to use because it can only take Character
 * as input. SimpleFAAdapter adds the ability for SimpleFA to accept arbitrary
 * objects as input by creating a mapping from objects to characters. This only
 * eases the use of the SimpleFA implementation. It doesn't eliminate the limit
 * imposed by the limited number of the characters.
 * 
 * @author Alex
 * 
 */
public class SimpleFAAdapter {
    // the underlying SimpleFA object
    private SimpleFA               simpleFA;

    // mapping from NFA input to SimpleFA input
    private Map<Object, Character> objectToCharacter;

    // mapping from SimpleFA input to NFA input
    private Map<Character, Object> characterToObject;

    // used to track used characters by simpleFA
    private char                   charValue;

    // used to track added states; SimpleFA doesn't track states;
    private Set<State>             states;

    public Set<Object> getInputs() {
        return objectToCharacter.keySet();
    }

    public Set<State> getStates() {
        return states;
    }

    public Set<State> getStates(State from) {
        Set<State> states = new HashSet<State>();
        for (Object input : this.getInputs(from)) {
            states.addAll(getStates(from, input));
        }

        // the behavior in NFA is slightly different than the DFA version, that
        // NFA will also return any states reachable through an epsilon
        // transition
        states.addAll(this.simpleFA.e_closure(from));

        return states;
    }

    /**
     * Get the underlying SimpleFA
     * 
     * @return
     */
    public SimpleFA getSimpleFA() {
        return simpleFA;
    }

    /**
     * Constructor. SimpleFAAdapter should be an immutable object.
     * 
     * @param simpleFA
     */
    // TODO: enforce the presence of the initial state by adding a initialState
    // parameter in this construtor
    public SimpleFAAdapter(SimpleFA simpleFA) {
        this.simpleFA = simpleFA;
        this.objectToCharacter = new HashMap<Object, Character>();
        this.characterToObject = new HashMap<Character, Object>();
        this.states = new HashSet<State>();
        this.charValue = '\000';
    }

    public void addTransition(State from, State to, Object input) {
        Character c = null;

        // keep tracking states
        states.add(from);
        states.add(to);

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
        if (to instanceof AcceptableState) {
            simpleFA.addAcceptedState(to);
        }
    }

    public Set<State> getStates(State from, Object input) {
        // never return null for collection
        Set<State> states = new HashSet<State>();

        if (objectToCharacter.containsKey(input)) {
            Character c = objectToCharacter.get(input);
            Set<Character> newInput = new HashSet<Character>(
                    Collections.singleton(c));

            // NOTE: SimpleFA.e_closure no long returns the source state. So we
            // need to do three times, the first for "from" state itself, the
            // second for "from" state's e_closure, and the last one, we do a
            // e_closure on all states so far.
            states.addAll(simpleFA.move(from, newInput));
            states.addAll(simpleFA.move(simpleFA.e_closure(from), newInput));
            states.addAll(simpleFA.e_closure(states));
        }

        return states;
    }

    public Set<Object> getInputs(State from) {
        // TODO: character inputs and object inputs should have one-to-one
        // mapping
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
        if (to instanceof AcceptableState) {
            simpleFA.addAcceptedState(to);
        }
    }

    public Set<AcceptableState> getAcceptableState() {
        Set<AcceptableState> states = new HashSet<AcceptableState>();

        for (State state : getStates()) {
            if (state instanceof AcceptableState) {
                states.add((AcceptableState) state);
            }
        }

        return states;
    }

    public InitialState getInitialState() {
        // TODO Auto-generated method stub
        return null;
    }

    public State getState(State from, Object input) {
        // TODO Auto-generated method stub
        return null;
    }

    // public Set<State> reachableStates(State from) {
    // return simpleFA.e_closure(from);
    // }
}
