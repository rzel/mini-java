package mini.java.fa.legacy;
import java.util.Set;
import java.util.HashSet;

import mini.java.fa.State;

public class SimpleFASimulator {
    private SimpleFA fa = null;
    private State currState = null;

    public SimpleFASimulator(SimpleFA fa) {
        this.fa = fa;
        this.currState = fa.getInitialState();
    }

    public void init() {
        this.currState = fa.getInitialState();
    }

    public boolean match(String str) {
        for (int i=0; i<str.length(); ++i) {
            char ch = str.charAt(i);
            Set<Character> input = new HashSet<Character>();
            input.add(ch);
            Set<State> set = fa.move(currState, input);
            if (set.isEmpty() || set.size() > 1) {
                return false;
            }
            currState = (set.toArray(new State[0]))[0];
            //System.out.println("CH: " + ch + "; CURRSTATE: " + currState);
        }
        if (fa.getAcceptedStates().contains(currState)) {
            return true;
        } else {
            return false;
        }
    }

/*
    public static void main(String[] args) throws Exception {
        String regex = ".*";
        SimpleFA nfa = RegexCompiler.compile(regex); nfa.dump();
        SimpleFA dfa = nfa.toDFA(); dfa.dump();
        SimpleFASimulator faSimulator = new SimpleFASimulator(dfa);
        System.out.println("REGEX: " + regex);
        faSimulator.init();
        System.out.println("a: " + faSimulator.match("a"));
        faSimulator.init();
        System.out.println("EMPTY: " + faSimulator.match(""));
    }
*/
}
