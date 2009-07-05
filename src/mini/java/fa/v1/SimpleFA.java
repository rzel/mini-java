package mini.java.fa.v1;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mini.java.fa.v3.InitialState;
import mini.java.fa.v3.State;

public class SimpleFA {
    // private int stateId;
    private State                                  initialState;
    private Set<State>                             acceptedStates;
    // a 2-dimension array to represent the directed graph
    // Set<Character> is the input(weight) of each transition
    private Map<State, Map<State, Set<Character>>> map;
    // for empty transitions
    private Map<State, Set<State>>                 emap;

    public SimpleFA() {
        // stateId = 0;
        initialState = null;
        acceptedStates = new HashSet<State>();
        map = new HashMap<State, Map<State, Set<Character>>>();
        emap = new HashMap<State, Set<State>>();
    }

    // factory method to create unique states
    public State createState() {
        return new State();
    }

    public void addTransition(State frm, State to, Set<Character> input) {
        if (frm == null || to == null)
            return;
        if (input == null)
            return;
        if (map.containsKey(frm)) {
            Map<State, Set<Character>> map0 = map.get(frm);
            if (map0.containsKey(to)) {
                Set<Character> set = map0.get(to);
                // merge the input
                set.addAll(input);
            } else {
                // won't reach here
                map0.put(to, input);
            }
        } else {
            Map<State, Set<Character>> map0 = new HashMap<State, Set<Character>>();
            map0.put(to, input);
            map.put(frm, map0);
        }
    }

    // add empty transition
    public void addTransition(State frm, State to) {
        if (frm == null || to == null)
            return;
        if (emap.containsKey(frm)) {
            Set<State> s = emap.get(frm);
            s.add(to);
        } else {
            Set<State> s = new HashSet<State>();
            s.add(to);
            emap.put(frm, s);
        }
    }

    public void addAcceptedState(State state) {
        acceptedStates.add(state);
    }

    public Set<State> getAcceptedStates() {
        return acceptedStates;
    }

    public void setInitialState(State state) {
        initialState = state;
    }

    public State getInitialState() {
        return initialState;
    }

    // find the states State frm can reach through input
    public Set<State> move(State frm, Set<Character> input) {
        Set<State> ret = new HashSet<State>();
        if (map.containsKey(frm)) {
            Map<State, Set<Character>> map0 = map.get(frm);
            for (Map.Entry<State, Set<Character>> entry : map0.entrySet()) {
                Set<Character> s = entry.getValue();
                if (s.containsAll(input)) { // input can be a subset anyway
                    ret.add(entry.getKey());
                }
            }
        }
        return ret;
    }

    public Set<State> move(Set<State> frms, Set<Character> input) {
        Set<State> ret = new HashSet<State>();
        for (State s : frms) {
            ret.addAll(move(s, input));
        }
        return ret;
    }

    // find the states State frm can reach through an empty input
    public Set<State> e_closure(State frm) {
        Set<State> ret = new HashSet<State>(Collections.singleton(frm));

        if (emap.containsKey(frm)) {
            Set<State> ret0 = new HashSet<State>();
            ret.addAll(emap.get(frm));
            // keep doing until ret doesn't change anymore
            do {
                ret0.clear();
                for (State s : ret) {
                    if (emap.containsKey(s)) {
                        ret0.addAll(emap.get(s));
                    }
                }
            } while (ret.addAll(ret0)); // addAll() returns true if the set
            // changes
        }
        return ret;
    }

    public Set<State> e_closure(Set<State> frms) {
        Set<State> ret = new HashSet<State>();
        for (State s : frms) {
            ret.addAll(e_closure(s));
        }
        return ret;
    }

    // helper function returns all the input that State frm can accept
    public Set<Character> getInputMixed(State frm) {
        Set<Character> ret = new HashSet<Character>();
        if (map.containsKey(frm)) {
            Map<State, Set<Character>> map0 = map.get(frm);
            for (State s : map0.keySet()) {
                ret.addAll(map0.get(s));
            }
        }
        return ret;
    }

    public SimpleFA toDFA() {
        SimpleFA fa = new SimpleFA();
        // states of nfa --> state of dfa
        Map<Set<State>, State> map0 = new HashMap<Set<State>, State>();
        // an array of new states from the nfa;
        List<Set<State>> todo = new LinkedList<Set<State>>();

//        State initState = fa.createState();
        State initState = new InitialState();
        fa.setInitialState(initState);

        // Bug1: ".*" should accept empty string;
        Set<State> initStates = e_closure(initialState);
        for (State acceptedState : acceptedStates) {
            if (initStates.contains(acceptedState)) {
                fa.addAcceptedState(initState);
            }
        }

        map0.put(initStates, initState);
        todo.add(initStates);
        while (todo.size() > 0) {
            Set<State> states = todo.remove(0);
            // System.out.println("T0D0: " + states);
            Set<Character> inputs = new HashSet<Character>();

            // find all the inputs that the states can accept
            for (State s : states) {
                inputs.addAll(getInputMixed(s));
            }
            // System.out.println("INPUTS: " + inputs);
            for (Character c : inputs) {
                Set<Character> input = new HashSet<Character>();
                input.add(c);
                // find the next states
                Set<State> states0 = e_closure(move(states, input));

                // System.out.println("C: " + c + " FOLLOW: " + states0);
                if (!map0.containsKey(states0)) {
                    State newState = fa.createState();
                    map0.put(states0, newState);

                    // check to see whether the new state is an accepted state
                    // in the dfa
                    for (State acceptedState : acceptedStates) {
                        if (states0.contains(acceptedState)) {
                            fa.addAcceptedState(newState);
                        }
                    }
                    todo.add(states0);
                }

                fa.addTransition((State) map0.get(states),
                        (State) map0.get(states0), input);
            }
        }
        return fa;
    }
}
