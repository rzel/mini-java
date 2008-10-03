package mini.java.fa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public abstract class DFATest {
    // factory method to get the DFABuilder implementation
    protected abstract DFABuilder getBuilder();
    
    // helper function used to test whether a DFA can be built correctly
    private final void testDFA(
            List<State> sources_,
            List<State> targets_,
            List<Object> inputs_) {
        assert (sources_ != null);
        assert (targets_ != null);
        assert (inputs_ != null);
        assert (sources_.size() == targets_.size());
        assert (sources_.size() == inputs_.size());
        
        DFABuilder builder = getBuilder();
        for (int i=0; i<sources_.size(); ++i) {
            builder.addTransition(sources_.get(i), targets_.get(i), inputs_.get(i));
        }
        
        DFA dfa = builder.buildDFA();
        assertNotNull(dfa); // avoid error
        for (int i=0; i<sources_.size(); ++i) {
            assertEquals(dfa.getState(sources_.get(i), inputs_.get(i)), targets_.get(i));
        }
    }
    
    @Test
    public void testAddTransition001() {
        // TEST:        Single transition
        // INPUTS:      (InitialState, State, Object)
        State from      = new InitialState();
        State to        = new State();
        Object input    = new Object();
        
//        DFABuilder builder = getBuilder();
//        builder.addTransition(from, to, input);
//        
//        DFA dfa = builder.buildDFA();
//        assertNotNull(dfa); // avoid error
//        assertEquals(dfa.getState(from, input), to);
        testDFA(
            Arrays.asList(from),
            Arrays.asList(to),
            Arrays.asList(input));
    }
    
    @Test
    public void testAddTransition002() {
        // TEST:        Multiple transitions (Same source state)
        // INPUTS:      (from, to, input),(from, to0, input0)
        State from      = new InitialState();
        State to        = new State();
        State to0       = new State();
        
        Object input    = new Object();
        Object input0   = new Object();
        
//        DFABuilder builder = getBuilder();
//        builder.addTransition(from, to, input);
//        builder.addTransition(from, to0, input0);
//        
//        DFA dfa = builder.buildDFA();
//        assertNotNull(dfa);
//        assertEquals(dfa.getState(from, input), to);
//        assertEquals(dfa.getState(from, input0), to0);
        testDFA(
                Arrays.asList(from,     from),
                Arrays.asList(to,       to0),
                Arrays.asList(input,    input0));
    }
    
    @Test
    public void testAddTransition003() {
        // TEST:        Multiple transitions
        // INPUTS:      (s1,s2,input),(s2,s3,input0)
        State s1 = new InitialState();
        State s2 = new State();
        State s3 = new State();
        
        Object input    = new Object();
        Object input0   = new Object();
        
//        DFABuilder builder = getBuilder();
//        builder.addTransition(s1, s2, input);
//        builder.addTransition(s2, s3, input0);
//        
//        DFA dfa = builder.buildDFA();
//        assertNotNull(dfa);
//        assertEquals(dfa.getState(s1, input), s2);
//        assertEquals(dfa.getState(s2, input0), s3);
        testDFA(
                Arrays.asList(s1,     s2),
                Arrays.asList(s2,     s3),
                Arrays.asList(input,  input0));
    }
    
    @Test
    public void testAddTransition004() {
        // TEST:        Multiple transitions (Same input)
        // INPUTS:      (from,to,input),(from,to0,input)
        State from      = new InitialState();
        State to        = new State();
        State to0       = new State();
        // same input for different target states
        Object input    = new Object();
        
        DFABuilder builder = getBuilder();
        builder.addTransition(from, to, input);
        builder.addTransition(from, to0, input); // should override previous one
        
        DFA dfa = builder.buildDFA();
        assertNotNull(dfa);
        assertEquals(dfa.getState(from, input), to0);
    }
    
    @Test
    public void testAddTransition005() {
        // TEST:        Multiple transitions (Loop)
        // INPUTS:      (from,from,input)
        State  state    = new InitialState();
        Object input    = new Object();

        DFABuilder builder = getBuilder();
        builder.addTransition(state, state, input);

        DFA dfa = builder.buildDFA();
        assertNotNull(dfa);
        assertEquals(dfa.getState(state, input), state);
    }

    @Test
    public void testBuildDFA001() {
        // TEST:        Build empty DFA
        // INPUTS:      null
        DFABuilder builder = getBuilder();
        DFA dfa = builder.buildDFA();
        // won't build since no initial state
        assertNull(dfa);
    }

    @Test
    public void testBuildDFA002() {
        // TEST:        Build invalid DFA (no initial state)
        // INPUTS:      (State,State,Object)
        State from      = new State();
        State to        = new State();
        Object input    = new Object();
        
        DFABuilder builder = getBuilder();
        builder.addTransition(from, to, input);
        
        DFA immutableDFA = builder.buildDFA();
        assertNull(immutableDFA);
    }
    
    @Test
    public void testBuildDFA003() {
        // TEST:        Build same DFA
        // INPUTS:      (State,State,Object)
        State from      = new InitialState();
        State to        = new State();
        Object input    = new Object();
        
        DFABuilder builder = getBuilder();
        builder.addTransition(from, to, input);
        
        DFA firstDFA    = builder.buildDFA();
        DFA secondDFA   = builder.buildDFA();        
        
        assertNotNull(firstDFA);
        assertNotNull(secondDFA);
        assertEquals(firstDFA, secondDFA);
    }

    @Test
    public void testGetState001() {
        // TEST:        Null input; should return null for target state
        // INPUTS:      (State,State,Object)
        State from      = new InitialState();
        State to        = new State();
        Object input    = new Object();
        
        DFABuilder builder = getBuilder();
        builder.addTransition(from, to, input);
        
        DFA dfa    = builder.buildDFA();
        // try to use a null input
        assertNull(dfa.getState(from, null));
    }

    @Test
    public void testGetState002() {
        // TEST:        Invalid input; should return null for target state
        // INPUTS:      (State,State,Object)
        State from      = new InitialState();
        State to        = new State();
        Object input    = new Object();
        Object input0   = new Object();

        DFABuilder builder = getBuilder();
        builder.addTransition(from, to, input);
        
        DFA dfa    = builder.buildDFA();
        // try to use an invalid input
        assertNull(dfa.getState(from, input0));
    }

    @Test
    public void testGetInitialState() {
        // TEST:        initial state should be the same
        // INPUTS:      (State,State,Object)
        State from      = new InitialState();
        State to        = new State();
        Object input    = new Object();

        DFABuilder builder = getBuilder();
        builder.addTransition(from, to, input);
        
        DFA dfa    = builder.buildDFA();
        InitialState initialState = dfa.getInitialState();
        // make sure every DFA has an initial state
        assertNotNull(initialState);
        assertEquals(from, initialState);
    }
}
