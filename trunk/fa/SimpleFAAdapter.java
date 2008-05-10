package fa;
import fa.legacy.State;
import fa.legacy.SimpleFA;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class SimpleFAAdapter<T> implements NFiniteAutomaton<T> {
    private SimpleFA fa = null;
    private Map<T, Character> objectToCharacter =
        new HashMap<T, Character>();
    private Map<Character, T> characterToObject =
        new HashMap<Character, T>();
    //private Character charCounter = null;
    private char charValue = '\000';

    //public SimpleFAAdapter(SimpleFA fa) {
    //    this.fa = SimpleFA;
    //}
    public SimpleFA getFa() {
        return fa;
    }
    public void setFa(SimpleFA fa) {
        this.fa = fa;
    }

    public void addTransition(State from, State to, T input) {
        Character c = new Character(charValue++);
        objectToCharacter.put(input, c);
        characterToObject.put(c, input);

        Set<Character> s = new HashSet<Character>();
        s.add(c);
        fa.addTransition(from, to, s);
        if (to instanceof AcceptedState) {
            fa.addAcceptedState(to);
        }
    }

    //public void addAcceptedState(State state) {
    //}

    public Set<State> reachableStates(State from, T input) {
        Character c = objectToCharacter.get(input);
        if (c == null) return null;

        Set<Character> s = new HashSet<Character>();
        s.add(c);
        return fa.e_closure(fa.move(fa.e_closure(from), s));
    }

    public Set<T> possibleInputs(State from) {
        Set<Character> charInput = fa.getInputMixed(from);
        Set<T> objectInput = new HashSet<T>();
        for (Character c : charInput) {
            //T o = objectToCharacter.get(c);
            T o = characterToObject.get(c);
            if (o != null)
                objectInput.add(o);
        }
        return objectInput;
    }

    public void addTransition(State from, State to) {
        fa.addTransition(from, to);
        if (to instanceof AcceptedState) {
            fa.addAcceptedState(to);
        }
    }

    public Set<State> reachableStates(State from) {
        return fa.e_closure(from);
    }
}
