package mini.java.fa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public abstract class NFATest {
    // factory method for NFABuilder implementation
    protected abstract NFABuilder getBuilder();

    @Test
    public final void testAddTransition001() {
        // TEST:        Single transition
        // INPUTS:      (InitialState, State, Object)
        State from      = new InitialState();
        State to        = new State();
        Object input    = new Object();
        
        NFABuilder builder = getBuilder();
        builder.addTransition(from, to, input);
        
        NFA nfa = builder.buildNFA();
        assertNotNull(nfa); // avoid error
        assertEquals(nfa.closure(from, input), Collections.singleton(to));
    }
    
    @Test
    public final void testAddTransition002() {
        // TEST:        Multiple Transitions
        // INPUTS:      (s1,s2,input),(s2,s3,input);
        State s1        = new InitialState();
        State s2        = new State();
        State s3        = new State();
        Object input    = new Object();
        
        NFABuilder builder = getBuilder();
        builder.addTransition(s1, s2, input);
        builder.addTransition(s2, s3, input);
        
        NFA nfa = builder.buildNFA();
        assertNotNull(nfa); // avoid error
        assertEquals(nfa.closure(s1, input), Collections.singleton(s2));
        assertEquals(nfa.closure(s2, input), Collections.singleton(s3));
    }
    
    @Test
    public final void testAddTransition003() {
        // TEST:        Single epsilon transition
        // INPUTS:      (s1,s2);
        State s1 = new InitialState();
        State s2 = new State();
        
        NFABuilder builder = getBuilder();
        builder.addTransition(s1, s2);
        
        NFA nfa = builder.buildNFA();
        assertNotNull(nfa); // avoid error
        
        Set<State> got = nfa.closure(s1);
        assertNotNull(got); // avoid error
        
        Set<State> expected = new HashSet<State>();
        expected.add(s1);
        expected.add(s2);        
        assertEquals(expected, got);
    }
    
    @Test
    public final void testAddTransition004() {
        // TEST:        Single epsilon transition (Loop)
        // INPUTS:      (s1,s1);
        State state = new InitialState();
        
        NFABuilder builder = getBuilder();
        builder.addTransition(state, state);
        
        NFA nfa = builder.buildNFA();
        assertNotNull(nfa); // avoid error
        assertEquals(nfa.closure(state), Collections.singleton(state));
    }
    
    @Test
    public final void testAddTransition005() {
        // TEST:        Multiple epsilon transitions (Same source state)
        // INPUTS:      (s1,s2),(s1,s3);
        State s1 = new InitialState();
        State s2 = new State();
        State s3 = new State();
        
        NFABuilder builder = getBuilder();
        builder.addTransition(s1, s2);
        builder.addTransition(s1, s3);
        
        NFA nfa = builder.buildNFA();
        assertNotNull(nfa); // avoid error
        
        Set<State> got = nfa.closure(s1);
        assertNotNull(got); // avoid error
        
        Set<State> expected = new HashSet<State>();
        expected.add(s1);
        expected.add(s2);        
        expected.add(s3);
        assertEquals(expected, got);
    }
    
    @Test
    public final void testAddTransition006() {
        // TEST:        Multiple epsilon transitions
        // INPUTS:      (s1,s2),(s2,s3);
        State s1 = new InitialState();
        State s2 = new State();
        State s3 = new State();
        
        NFABuilder builder = getBuilder();
        builder.addTransition(s1, s2);
        builder.addTransition(s2, s3);
        
        NFA nfa = builder.buildNFA();
        assertNotNull(nfa); // avoid error
        
        Set<State> got = nfa.closure(s1);
        assertNotNull(got); // avoid error
        
        Set<State> expected = new HashSet<State>();
        expected.add(s1);
        expected.add(s2);        
        expected.add(s3);
        assertEquals(expected, got);
    }    
    
    @Test
    public final void testAddTransition007() {
        // TEST:        Both normal transitions and epsilon transitions
        // INPUTS:      (s1,s2),(s2,s3,input);
        State s1 = new InitialState();
        State s2 = new State();
        State s3 = new State();
        Object input = new Object();
        
        NFABuilder builder = getBuilder();
        builder.addTransition(s1, s2);
        builder.addTransition(s2, s3, input);
        
        NFA nfa = builder.buildNFA();
        assertNotNull(nfa); // avoid error
        
        // check epsilon transitions
        Set<State> got = nfa.closure(s1);
        assertNotNull(got); // avoid error        
        Set<State> expected = new HashSet<State>();
        expected.add(s1);
        expected.add(s2);
        assertEquals(got, expected);
        
        // check normal transitions
        assertEquals(nfa.closure(s2, input), Collections.singleton(s3));
    }
    
    @Test
    public final void testAddTransition008() {
        // TEST:        Both normal transitions and epsilon transitions
        // INPUTS:      (s1,s2),(s1,s3,input);
        State s1 = new InitialState();
        State s2 = new State();
        State s3 = new State();
        Object input = new Object();
        
        NFABuilder builder = getBuilder();
        builder.addTransition(s1, s2);
        builder.addTransition(s1, s3, input);
        
        NFA nfa = builder.buildNFA();
        assertNotNull(nfa); // avoid error
        
        // check epsilon transitions
        Set<State> got = nfa.closure(s1);
        assertNotNull(got); // avoid error        
        Set<State> expected = new HashSet<State>();
        expected.add(s1);
        expected.add(s2);
        assertEquals(got, expected);
        
        // check normal transitions
        assertEquals(nfa.closure(s1, input), Collections.singleton(s3));
    }
    
    @Test
    public final void testAddTransition009() {
        // TEST:        Both normal transitions and epsilon transitions
        // INPUTS:      (s1,s2),(s1,s2,input);
        State s1 = new InitialState();
        State s2 = new State();
        Object input = new Object();
        
        NFABuilder builder = getBuilder();
        builder.addTransition(s1, s2);
        builder.addTransition(s1, s2, input);
        
        NFA nfa = builder.buildNFA();
        assertNotNull(nfa); // avoid error
        
        // check epsilon transitions
        Set<State> got = nfa.closure(s1);
        assertNotNull(got); // avoid error        
        Set<State> expected = new HashSet<State>();
        expected.add(s1);
        expected.add(s2);
        assertEquals(got, expected);
        
        // check normal transitions
        assertEquals(nfa.closure(s1, input), Collections.singleton(s2));
    }

    @Test
    public final void testBuildNFA001() {
        // TEST:        NFA with no initial state
        // INPUTS:      (State,State)
        State s1 = new State();
        State s2 = new State();
        
        NFABuilder builder = getBuilder();
        builder.addTransition(s1, s2);
        
        NFA nfa = builder.buildNFA();
        assertNull(nfa);
    }
    
    @Test
    public final void testBuildNFA002() {
        // TEST:        NFA with no transitions
        // INPUTS:      null
        NFABuilder builder = getBuilder();
        NFA nfa = builder.buildNFA();
        assertNull(nfa);
    }
    
    @Test
    public final void testBuildNFA003() {
        // TEST:        Build multiple NFA
        // INPUTS:      (s1, s2)
        State s1 = new InitialState();
        State s2 = new State();
        
        NFABuilder builder = getBuilder();
        builder.addTransition(s1, s2);
        
        NFA firstNFA = builder.buildNFA();
        NFA secondNFA = builder.buildNFA();
        assertNotNull(firstNFA); // avoid error
        assertNotNull(secondNFA); // avoid error
        assertEquals(firstNFA, secondNFA);
    }
    
    // should create an NFA with "ABa,BCb,CDc,AC,AD,DE"
    
    @Test
    public final void testGetInputs() {
        // TEST:        Get input from the initial state
        // INPUTS:      "ABa,BCb,CDc,AC,AD,DE"
        NFABuilder builder = getBuilder();
        NFA nfa = TestHelper.buildNFA("ABa,BCb,CDc,AC,AD,DE", builder);
        assertNotNull(nfa); // avoid error
        
        InitialState initialState = nfa.getInitialState();
        assertNotNull(initialState);
        
        Set<Object> got = nfa.getInputs(initialState);
        Set<Object> expected = new HashSet<Object>();
        expected.add('a');
        expected.add('c');
        assertEquals(expected, got);
    }
    
    @Test
    public final void testGetInitialState() {
        // TEST:        Get initial state from the NFA
        // INPUTS:      "ABa,BCb,CDc,AC,AD,DE"
        NFABuilder builder = getBuilder();
        NFA nfa = TestHelper.buildNFA("ABa,BCb,CDc,AC,AD,DE", builder);
        assertNotNull(nfa); // avoid error
        
        InitialState initialState = nfa.getInitialState();
        assertNotNull(initialState);
    }
    
    @Test
    public final void testSourceClosure001() {
        // TEST:        Get closure from the initial state
        // INPUTS:      "ABa,BCb,CDc,AC,AD,DE"
        NFABuilder builder = getBuilder();
        NFA nfa = TestHelper.buildNFA("ABa,BCb,CDc,AC,AD,DE", builder);
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
        NFABuilder builder = getBuilder();
        NFA nfa = TestHelper.buildNFA("AB,BC,CD", builder);
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
        NFABuilder builder = getBuilder();
        NFA nfa = TestHelper.buildNFA("AB,BA", builder);
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
        NFABuilder builder = getBuilder();
        NFA nfa = TestHelper.buildNFA("ABa,BCb,CDc,AC,AD,DE", builder);
        assertNotNull(nfa); // avoid error
        
        InitialState initialState = nfa.getInitialState();
        assertNotNull(initialState);
        
        Set<State> got = nfa.closure(initialState, 'c');
        assertNotNull(got);
        assertEquals(2, got.size());
    }
}
