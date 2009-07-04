package mini.java.fa.v3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashSet;

import mini.java.fa.v3.DFA;
import mini.java.fa.v3.DFABuilder;
import mini.java.fa.v3.InitialState;
import mini.java.fa.v3.State;

import org.junit.Before;
import org.junit.Test;

public abstract class DFATest {
    private static InitialState S0 = new InitialState();
    private static State S1 = new State();
    private static State S2 = new State();
    
    // input objects for tests
    private static Object O1 = new Object();
    private static Object O2 = new Object();
    private static Object O3 = new Object();
    
    private DFABuilder _builder;
    
    // factory method to get the DFABuilder implementation
    protected abstract DFABuilder getBuilder();
    
//    // helper function used to test whether a DFA can be built correctly
//    private final void testDFA(
//            List<State> sources_,
//            List<State> targets_,
//            List<Object> inputs_) {
//        assert (sources_ != null);
//        assert (targets_ != null);
//        assert (inputs_ != null);
//        assert (sources_.size() == targets_.size());
//        assert (sources_.size() == inputs_.size());
//        
//        DFABuilder builder = getBuilder();
//        for (int i=0; i<sources_.size(); ++i) {
//            builder.addTransition(sources_.get(i), targets_.get(i), inputs_.get(i));
//        }
//        
//        DFA dfa = builder.buildDFA();
//        assertNotNull(dfa); // avoid error
//        for (int i=0; i<sources_.size(); ++i) {
//            assertEquals(dfa.getState(sources_.get(i), inputs_.get(i)), targets_.get(i));
//        }
//    }
    
    @Before
    public void setUp() {
        // get a new builder before each test
        _builder = getBuilder();
    }
    
    @Test
    public void testAddTransition001() {
        // TEST:        Single transition
        // INPUTS:      (InitialState, State, Object)
        _builder.addTransition(S0, S1, O1);
        
        DFA dfa = _builder.buildDFA();
        assertNotNull(dfa); // avoid error
        assertEquals(S1, dfa.getState(S0, O1));
//        assertNull(dfa.getState(S0, O2));
//        assertNull(dfa.getState(S1, O1));
//        assertNull(dfa.getState(S1, O2));
    }
    
    @Test
    public void testAddTransition002() {
        // TEST:        Multiple transitions (Same source state)
        // INPUTS:      (S0,S1,O1),(S0,S2,O2)
        _builder.addTransition(S0,S1,O1);
        _builder.addTransition(S0,S2,O2);
        
        DFA dfa = _builder.buildDFA();
        assertNotNull(dfa);
        assertEquals(S1, dfa.getState(S0, O1));
        assertEquals(S2, dfa.getState(S0, O2));
    }
    
    @Test
    public void testAddTransition003() {
        // TEST:        Multiple transitions
        // INPUTS:      (s1,s2,input),(s2,s3,input0)
        _builder.addTransition(S0, S1, O1);
        _builder.addTransition(S1, S2, O2);
        
        DFA dfa = _builder.buildDFA();
        assertNotNull(dfa);
        assertEquals(S1, dfa.getState(S0, O1));
        assertEquals(S2, dfa.getState(S1, O2));
    }
    
    @Test
    public void testAddTransition004() {
        // TEST:        Multiple transitions (Same input)
        // INPUTS:      (S0,S1,O1),(S0,S2,O1)
        _builder.addTransition(S0, S1, O1);
        _builder.addTransition(S0, S2, O1); // should override previous one
        
        DFA dfa = _builder.buildDFA();
        assertNotNull(dfa);
        assertEquals(S2, dfa.getState(S0, O1));
    }
    
    @Test
    public void testAddTransition005() {
        // TEST:        Multiple transitions (Loop)
        // INPUTS:      (from,from,input)
        _builder.addTransition(S0,S0,O1);

        DFA dfa = _builder.buildDFA();
        assertNotNull(dfa);
        assertEquals(S0, dfa.getState(S0,O1));
    }

    @Test
    public void testBuildDFA001() {
        // TEST:        Build empty DFA
        // INPUTS:      null
        DFA dfa = _builder.buildDFA();
        // won't build since no initial state
        assertNull(dfa);
    }

    @Test
    public void testBuildDFA002() {
        // TEST:        Build invalid DFA (no initial state)
        // INPUTS:      (State,State,Object)
        _builder.addTransition(S1,S2,O1);
        
        DFA dfa = _builder.buildDFA();
        assertNull(dfa);
    }
    
    @Test
    public void testBuildDFA003() {
        // TEST:        Build the same DFA
        // INPUTS:      (State,State,Object)
        _builder.addTransition(S0,S1,O1);
        
        DFA firstDFA    = _builder.buildDFA();
        DFA secondDFA   = _builder.buildDFA();        
        
        assertNotNull(firstDFA);
        assertNotNull(secondDFA);
        assertEquals(firstDFA, secondDFA);
    }

    @Test
    public void testGetState001() {
        // TEST:        Null input; should return null for target state
        // INPUTS:      (State,State,Object)
        _builder.addTransition(S0,S1,O1);
        
        DFA dfa = _builder.buildDFA();
        assertNotNull(dfa);
        assertNull(dfa.getState(S0, null)); // try to use a null input
        assertNull(dfa.getState(S1, null));
    }

    @Test
    public void testGetState002() {
        // TEST:        Invalid input; should return null for target state
        // INPUTS:      (State,State,Object)
        _builder.addTransition(S0,S1,O1);
        
        DFA dfa = _builder.buildDFA();
        assertNotNull(dfa);
        assertNull(dfa.getState(S0, O2)); // try to use an invalid input
        assertNull(dfa.getState(S1, O1));
        assertNull(dfa.getState(S1, O2));
    }
    
    @Test
    public void testGetState003() {
        // TEST:        Invalid input; multiple transitions
        // INPUTS:      (State,State,Object)
        _builder.addTransition(S0,S1,O1);
        _builder.addTransition(S0,S2,O2);
        
        DFA dfa = _builder.buildDFA();
        assertNotNull(dfa);
        assertNull(dfa.getState(S0, O3));
        assertNull(dfa.getState(S1, O1));
        assertNull(dfa.getState(S1, O2));
        assertNull(dfa.getState(S1, O3));
        assertNull(dfa.getState(S2, O1));
        assertNull(dfa.getState(S2, O2));
        assertNull(dfa.getState(S2, O3));
    }

    @Test
    public void testGetInitialState() {
        // TEST:        Initial state should be the same
        // INPUTS:      (State,State,Object)
        _builder.addTransition(S0,S1,O1);
        
        DFA dfa    = _builder.buildDFA();
        assertNotNull(dfa);
        
        InitialState initialState = dfa.getInitialState();
        // make sure every DFA has an initial state
        assertNotNull(initialState);
        assertEquals(S0, initialState);
    }
    
    @Test
    public void testGetInputs() {
        // TEST: getInputs() should return the correct inputs
        _builder.addTransition(S0,S1,O1);
        _builder.addTransition(S0, S2, O2);
        
        DFA dfa = _builder.buildDFA();
        assertNotNull(dfa);
        
        InitialState initState = dfa.getInitialState();
        assertNotNull(initState);
        assertEquals(
                new HashSet<Object>(Arrays.asList(O1,O2)),
                dfa.getInputs(initState));
    }
}
