package mini.java.fa.v1;

import java.util.HashSet;
import java.util.Set;

import mini.java.fa.v3.State;

// this is a "Matcher", not a simulator
public class SimpleFASimulator {
    private SimpleFA fa        = null;
    private State    currState = null;

    public SimpleFASimulator(SimpleFA fa) {
        this.fa = fa;
        this.currState = fa.getInitialState();
    }

    public void init() {
        this.currState = fa.getInitialState();
    }

    public boolean match(String str) {
        for (int i = 0; i < str.length(); ++i) {
            char ch = str.charAt(i);
            Set<Character> input = new HashSet<Character>();
            input.add(ch);
            Set<State> set = fa.move(currState, input);
            if (set.isEmpty() || set.size() > 1) {
                return false;
            }
            currState = (set.toArray(new State[0]))[0];
            // System.out.println("CH: " + ch + "; CURRSTATE: " + currState);
        }
        if (fa.getAcceptedStates().contains(currState)) {
            return true;
        } else {
            return false;
        }
    }
}
