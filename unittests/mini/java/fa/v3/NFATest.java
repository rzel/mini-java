package mini.java.fa.v3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import mini.java.TestHelper;
import mini.java.fa.v3.InitialState;
import mini.java.fa.v3.NFA;
import mini.java.fa.v3.NFABuilder;
import mini.java.fa.v3.State;

import org.junit.Before;
import org.junit.Test;

public abstract class NFATest {
    private static InitialState S0 = new InitialState();
    private static State S1 = new State();
    private static State S2 = new State();
    
    // input objects for tests
    private static Object O1 = new Object();
    
    private NFABuilder _builder;
    
    // factory method for NFABuilder implementation
    protected abstract NFABuilder getBuilder();
    
    @Before
    public void setUp() {
        // get a new builder before each test
        _builder = getBuilder();
    }

    @Test
    public final void testAddTransition001() {
        // TEST:        Single transition
        // INPUTS:      (InitialState, State, Object)
        _builder.addTransition(S0, S1, O1);
        
        NFA nfa = _builder.buildNFA();
        assertNotNull(nfa); // avoid error
        assertEquals(Collections.singleton(S1), nfa.closure(S0, O1));
    }
    
    @Test
    public final void testAddTransition002() {
        // TEST:        Multiple Transitions
        // INPUTS:      (s1,s2,input),(s2,s3,input);
        _builder.addTransition(S0, S1, O1);
        _builder.addTransition(S1, S2, O1);
        
        NFA nfa = _builder.buildNFA();
        assertNotNull(nfa); // avoid error
        assertEquals(Collections.singleton(S1), nfa.closure(S0, O1));
        assertEquals(Collections.singleton(S2), nfa.closure(S1, O1));
    }
    
    @Test
    public final void testAddTransition003() {
        // TEST:        Single epsilon transition
        // INPUTS:      (s1,s2);
        _builder.addTransition(S0, S1);
        
        NFA nfa = _builder.buildNFA();
        assertNotNull(nfa); // avoid error
        
        Set<State> got = nfa.closure(S0);
        Set<State> expected = new HashSet<State>(Arrays.asList(S0,S1));
        assertEquals(expected, got);
    }
    
    @Test
    public final void testAddTransition004() {
        // TEST:        Single epsilon transition (Loop)
        // INPUTS:      (s1,s1);
        _builder.addTransition(S0, S0);
        
        NFA nfa = _builder.buildNFA();
        assertNotNull(nfa); // avoid error
        
        Set<State> got = nfa.closure(S0);
        Set<State> expected = Collections.singleton((State)S0);
        assertEquals(expected, got);
    }
    
    @Test
    public final void testAddTransition005() {
        // TEST:        Multiple epsilon transitions (Same source state)
        // INPUTS:      (s1,s2),(s1,s3);
        _builder.addTransition(S0, S1);
        _builder.addTransition(S0, S2);
        
        NFA nfa = _builder.buildNFA();
        assertNotNull(nfa); // avoid error
        
        Set<State> got = nfa.closure(S0);
        Set<State> expected = new HashSet<State>(Arrays.asList(S0,S1,S2));
        assertEquals(expected, got);
    }
    
    @Test
    public final void testAddTransition006() {
        // TEST:        Multiple epsilon transitions
        // INPUTS:      (s1,s2),(s2,s3);
        _builder.addTransition(S0, S1);
        _builder.addTransition(S1, S2);
        
        NFA nfa = _builder.buildNFA();
        assertNotNull(nfa); // avoid error
        
        Set<State> got = nfa.closure(S0);
        Set<State> expected = new HashSet<State>(Arrays.asList(S0,S1,S2));
        assertEquals(expected, got);
    }    
    
    @Test
    public final void testAddTransition007() {
        // TEST:        Both normal transitions and epsilon transitions
        // INPUTS:      (s1,s2),(s2,s3,input);
        _builder.addTransition(S0, S1);
        _builder.addTransition(S1, S2, O1);
        
        NFA nfa = _builder.buildNFA();
        assertNotNull(nfa); // avoid error
        
        // check the epsilon transition
        Set<State> got = nfa.closure(S0);
        Set<State> expected = new HashSet<State>(Arrays.asList(S0,S1));
        assertEquals(expected, got);
        
        // check the normal transition
        assertEquals(Collections.singleton(S2), nfa.closure(S1,O1));
    }
    
    @Test
    public final void testAddTransition008() {
        // TEST:        Both normal transitions and epsilon transitions
        // INPUTS:      (s1,s2),(s1,s3,input);
        _builder.addTransition(S0, S1);
        _builder.addTransition(S0, S2, O1);
        
        NFA nfa = _builder.buildNFA();
        assertNotNull(nfa); // avoid error
        
        // check the epsilon transition
        Set<State> got = nfa.closure(S0);
        Set<State> expected = new HashSet<State>(Arrays.asList(S0,S1));
        assertEquals(expected, got);
        
        // check the normal transition
        assertEquals(Collections.singleton(S2), nfa.closure(S0,O1));
    }
    
    @Test
    public final void testAddTransition009() {
        // TEST:        Both normal transitions and epsilon transitions
        // INPUTS:      (s1,s2),(s1,s2,input);
        _builder.addTransition(S0, S1);
        _builder.addTransition(S0, S1, O1);
        
        NFA nfa = _builder.buildNFA();
        assertNotNull(nfa); // avoid error
        
        // check the epsilon transition
        Set<State> got = nfa.closure(S0);
        Set<State> expected = new HashSet<State>(Arrays.asList(S0,S1));
        assertEquals(expected, got);
        
        // check the normal transition
        assertEquals(Collections.singleton(S1), nfa.closure(S0,O1));
    }

    @Test
    public final void testBuildNFA001() {
        // TEST:        NFA with no initial state
        // INPUTS:      (State,State)
        _builder.addTransition(S1,S2);
        
        NFA nfa = _builder.buildNFA();
        assertNull(nfa);
    }
    
    @Test
    public final void testBuildNFA002() {
        // TEST:        NFA with no transitions
        // INPUTS:      null
        NFA nfa = _builder.buildNFA();
        assertNull(nfa);
    }
    
    @Test
    public final void testBuildNFA003() {
        // TEST:        Build multiple NFA
        // INPUTS:      (s1, s2)
        _builder.addTransition(S0, S0);
        
        NFA firstNFA = _builder.buildNFA();
        NFA secondNFA = _builder.buildNFA();
        assertNotNull(firstNFA); // avoid error
        assertNotNull(secondNFA); // avoid error
        assertEquals(firstNFA, secondNFA);
    }

    
    @Test
    public final void testGetInputs() {
        // TEST:        Get input from the initial state
        // INPUTS:      "ABa,BCb,CDc,AC,AD,DE"
        NFA nfa = TestHelper.buildNFA("ABa,BCb,CDc,AC,AD,DE", _builder);
        assertNotNull(nfa); // avoid error
        
        InitialState initialState = nfa.getInitialState();
        assertNotNull(initialState);
        
        Set<Object> got = nfa.getInputs(initialState);
        Set<Object> expected = new HashSet<Object>(Arrays.asList('a','c'));
        assertEquals(expected, got);
    }
    
    @Test
    public final void testGetInitialState() {
        // TEST:        Get initial state from the NFA
        // INPUTS:      "ABa,BCb,CDc,AC,AD,DE"
        NFA nfa = TestHelper.buildNFA("ABa,BCb,CDc,AC,AD,DE", _builder);
        assertNotNull(nfa); // avoid error
        
        InitialState initialState = nfa.getInitialState();
        assertNotNull(initialState);
    }
    
    @Test
    public final void testSourceClosure001() {
        // TEST:        Get closure from the initial state
        // INPUTS:      "ABa,BCb,CDc,AC,AD,DE"
        NFA nfa = TestHelper.buildNFA("ABa,BCb,CDc,AC,AD,DE", _builder);
        assertNotNull(nfa); // avoid error
        
        InitialState initialState = nfa.getInitialState();
        assertNotNull(initialState);
        
        Set<State> got = nfa.closure(initialState);
        assertNotNull(got);
        assertEquals(4, got.size());
    }
    
    @Test
    public final void testSourceClosure002() {
        // TEST:        Get closure from the initial state
        // INPUTS:      "AB,BC,CD"
        NFA nfa = TestHelper.buildNFA("AB,BC,CD", _builder);
        assertNotNull(nfa); // avoid error
        
        InitialState initialState = nfa.getInitialState();
        assertNotNull(initialState);
        
        Set<State> got = nfa.closure(initialState);
        assertNotNull(got);
        assertEquals(4, got.size());
    }
    
    @Test
    public final void testSourceClosure003() {
        // TEST:        Loop epsilon transitions
        // INPUTS:      "AB,BA"
        NFA nfa = TestHelper.buildNFA("AB,BA", _builder);
        assertNotNull(nfa); // avoid error
        
        // actual closure
        Set<State> got = null;
        
        // closure for "A"
        InitialState initialState = nfa.getInitialState();
        assertNotNull(initialState);
        
        got = nfa.closure(initialState);
        assertNotNull(got);
        assertEquals(2, got.size());
        
        // closure for "B"
        State targetState = null;
        for (State state : got) {
            if (!(state instanceof InitialState)) {
                targetState = state;
                break;
            }
        }
        assertNotNull(targetState);
        
        got = nfa.closure(targetState);
        assertNotNull(got);
        assertEquals(2, got.size());
    }
    
    @Test
    public final void testTargetClosure() {
        // TEST:        Get target closure from the initial state
        // INPUTS:      "ABa,BCb,CDc,AC,AD,DE"
        NFA nfa = TestHelper.buildNFA("ABa,BCb,CDc,AC,AD,DE", _builder);
        assertNotNull(nfa); // avoid error
        
        InitialState initialState = nfa.getInitialState();
        assertNotNull(initialState);
        
        Set<State> got = nfa.closure(initialState, 'c');
        assertNotNull(got);
        assertEquals(2, got.size());
    }
}
