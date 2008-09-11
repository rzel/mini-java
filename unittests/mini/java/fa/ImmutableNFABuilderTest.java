package mini.java.fa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class ImmutableNFABuilderTest {

    @Test
    public final void testSingleTransition() {
        State from      = new InitialState();
        State to        = new State();
        Object input    = new Object();
        
        ImmutableNFA.Builder builder = new ImmutableNFA.Builder();
        builder.addTransition(from, to, input);
        
        ImmutableNFA immutableNFA = builder.buildNFA();
        assertNotNull(immutableNFA); // avoid error
        assertEquals(immutableNFA.closure(from, input), Collections.singleton(to));
    }
    
    @Test
    public final void testMultipleTransitions() {
        State s1        = new InitialState();
        State s2        = new State();
        State s3        = new State();
        Object input    = new Object();
        
        ImmutableNFA.Builder builder = new ImmutableNFA.Builder();
        builder.addTransition(s1, s2, input);
        builder.addTransition(s2, s3, input);
        
        ImmutableNFA immutableNFA = builder.buildNFA();
        assertNotNull(immutableNFA); // avoid error
        assertEquals(immutableNFA.closure(s1, input), Collections.singleton(s2));
        assertEquals(immutableNFA.closure(s2, input), Collections.singleton(s3));
    }
    
    @Test
    public final void testSingleEpsilonTransition() {
        State s1 = new InitialState();
        State s2 = new State();
        
        ImmutableNFA.Builder builder = new ImmutableNFA.Builder();
        builder.addTransition(s1, s2);
        
        ImmutableNFA immutableNFA = builder.buildNFA();
        assertNotNull(immutableNFA); // avoid error
        
        Set<State> got = immutableNFA.closure(s1);
        assertNotNull(got); // avoid error
        
        Set<State> expected = new HashSet<State>();
        expected.add(s1);
        expected.add(s2);        
        assertEquals(got, expected);
    }
    
    @Test
    public final void testLoopEpsilonTransition() {
        State state = new InitialState();
        
        ImmutableNFA.Builder builder = new ImmutableNFA.Builder();
        builder.addTransition(state, state);
        
        ImmutableNFA immutableNFA = builder.buildNFA();
        assertNotNull(immutableNFA); // avoid error
        assertEquals(immutableNFA.closure(state), Collections.singleton(state));
    }
    
    @Test
    public final void testMultipleEpsilonTransitionsSameSourceState() {
        State s1 = new InitialState();
        State s2 = new State();
        State s3 = new State();
        
        ImmutableNFA.Builder builder = new ImmutableNFA.Builder();
        builder.addTransition(s1, s2);
        builder.addTransition(s1, s3);
        
        ImmutableNFA immutableNFA = builder.buildNFA();
        assertNotNull(immutableNFA); // avoid error
        
        Set<State> got = immutableNFA.closure(s1);
        assertNotNull(got); // avoid error
        
        Set<State> expected = new HashSet<State>();
        expected.add(s1);
        expected.add(s2);        
        expected.add(s3);
        assertEquals(got, expected);
    }
    
    @Test
    public final void testMultipleEpsilonTransitionsDifferentSourceState() {
        State s1 = new InitialState();
        State s2 = new State();
        State s3 = new State();
        
        ImmutableNFA.Builder builder = new ImmutableNFA.Builder();
        builder.addTransition(s1, s2);
        builder.addTransition(s2, s3);
        
        ImmutableNFA immutableNFA = builder.buildNFA();
        assertNotNull(immutableNFA); // avoid error
        
        Set<State> got = immutableNFA.closure(s1);
        assertNotNull(got); // avoid error
        
        Set<State> expected = new HashSet<State>();
        expected.add(s1);
        expected.add(s2);        
        expected.add(s3);
        assertEquals(got, expected);
    }    
    
    @Test
    public final void testMixedTransitions() {
        State s1 = new InitialState();
        State s2 = new State();
        State s3 = new State();
        Object input = new Object();
        
        ImmutableNFA.Builder builder = new ImmutableNFA.Builder();
        builder.addTransition(s1, s2);
        builder.addTransition(s2, s3, input);
        
        ImmutableNFA immutableNFA = builder.buildNFA();
        assertNotNull(immutableNFA); // avoid error
        
        // check epsilon transitions
        Set<State> got = immutableNFA.closure(s1);
        assertNotNull(got); // avoid error        
        Set<State> expected = new HashSet<State>();
        expected.add(s1);
        expected.add(s2);
        assertEquals(got, expected);
        
        // check normal transitions
        //assertEquals(immutableNFA.closure(s1, input), Collections.singleton(s3));
        assertEquals(immutableNFA.closure(s2, input), Collections.singleton(s3));
    }

    @Test
    public final void testInvalidNFAWithoutInitialState() {
        // build a NFA with no initial state
        State s1 = new State();
        State s2 = new State();
        
        ImmutableNFA.Builder builder = new ImmutableNFA.Builder();
        builder.addTransition(s1, s2);
        
        ImmutableNFA immutableNFA = builder.buildNFA();
        assertNull(immutableNFA);
    }
    
//    @Test
//    public final void testInvalidNFAWithMutipleInitialState() {
//        // build a NFA with no initial state
//        State s1 = new InitialState();
//        State s2 = new InitialState();
//        
//        ImmutableNFA.Builder builder = new ImmutableNFA.Builder();
//        builder.addTransition(s1, s2);
//        
//        ImmutableNFA immutableNFA = builder.buildNFA();
//        assertNull(immutableNFA);
//    }
    
    @Test
    public final void testEmptyNFA() {
        ImmutableNFA.Builder builder = new ImmutableNFA.Builder();
        ImmutableNFA immutableNFA = builder.buildNFA();
        assertNull(immutableNFA);
    }
    
    @Test
    public final void testMultipleNFA() {
        State s1 = new InitialState();
        State s2 = new State();
        
        ImmutableNFA.Builder builder = new ImmutableNFA.Builder();
        builder.addTransition(s1, s2);
        
        ImmutableNFA immutableNFA = builder.buildNFA();
        ImmutableNFA anotherImmutableNFA = builder.buildNFA();
        assertNotNull(immutableNFA); // avoid error
        assertNotNull(anotherImmutableNFA); // avoid error
        assertEquals(immutableNFA, anotherImmutableNFA);
        assertTrue(immutableNFA == anotherImmutableNFA);
    }
}
