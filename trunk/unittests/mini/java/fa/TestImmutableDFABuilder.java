package mini.java.fa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TestImmutableDFABuilder {

    @Test
    public void testAddSingleTransition() {
        State from = new InitialState();
        State to = new State();
        Object input = new Object();
        ImmutableDFA.Builder builder = new ImmutableDFA.Builder();
        builder.addTransition(from, to, input);
        
        ImmutableDFA immutableDFA = builder.buildDFA();
        assertNotNull(immutableDFA); // avoid error
        assertEquals(immutableDFA.getState(from, input), to);
    }
    
    @Test
    public void testAddMultipleTransitionsForSameSource() {
        State from = new InitialState();
        State to = new State();
        State to1 = new State();
        
        Object input = new Object();
        Object input1 = new Object();
        
        ImmutableDFA.Builder builder = new ImmutableDFA.Builder();
        builder.addTransition(from, to, input);
        builder.addTransition(from, to1, input1);
        
        ImmutableDFA immutableDFA = builder.buildDFA();
        assertNotNull(immutableDFA);
        assertEquals(immutableDFA.getState(from, input), to);
        assertEquals(immutableDFA.getState(from, input1), to1);
    }
    
    @Test
    public void testAddMultipleTransitions() {
        State s1 = new InitialState();
        State s2 = new State();
        State s3 = new State();
        
        Object input = new Object();
        Object input1 = new Object();
        
        ImmutableDFA.Builder builder = new ImmutableDFA.Builder();
        builder.addTransition(s1, s2, input);
        builder.addTransition(s2, s3, input1);
        
        ImmutableDFA immutableDFA = builder.buildDFA();
        assertNotNull(immutableDFA);
        assertEquals(immutableDFA.getState(s1, input), s2);
        assertEquals(immutableDFA.getState(s2, input1), s3);
    }
    
    @Test
    public void testOverridenTransition() {
        State from = new InitialState();
        State to = new State();
        State to1 = new State();
        // same input for different target state
        Object input = new Object();
        
        ImmutableDFA.Builder builder = new ImmutableDFA.Builder();
        builder.addTransition(from, to, input);
        builder.addTransition(from, to1, input); // "from --> to" should be overriden
        
        ImmutableDFA immutableDFA = builder.buildDFA();
        assertNotNull(immutableDFA);
        assertEquals(immutableDFA.getState(from, input), to1);
    }
    
    @Test
    public void testOverridenInitialState() {
        State s1 = new InitialState();
        State s2 = new InitialState();
        State s3 = new State();

        Object input = new Object();
        
        ImmutableDFA.Builder builder = new ImmutableDFA.Builder();
        builder.addTransition(s1, s3, input);
        builder.addTransition(s2, s3, input); // should be ignored
        
        ImmutableDFA immutableDFA = builder.buildDFA();
        assertNotNull(immutableDFA);
        assertNull(immutableDFA.getState(s2, input));
    }
    
    @Test
    public void testInvalidTargetState() {
        State from = new InitialState();
        // try to add an invalid target state
        State to = new InitialState();
        // make sure the DFA can be built
        State to1 = new State();
        
        Object input = new Object();
        Object input1 = new Object();
        
        ImmutableDFA.Builder builder = new ImmutableDFA.Builder();
        builder.addTransition(from, to, input); // should be ignored
        builder.addTransition(from, to1, input1);
        
        ImmutableDFA immutableDFA = builder.buildDFA();
        assertNotNull(immutableDFA);
        assertNull(immutableDFA.getState(from, input));
    }

    @Test
    public void testBuildEmptyDFA() {
        ImmutableDFA.Builder builder = new ImmutableDFA.Builder();
        ImmutableDFA immutableDFA = builder.buildDFA();
        // won't build since no initial state
        assertNull(immutableDFA);
    }
    
    @Test
    public void testBuildDFA() {
        State from = new InitialState();
        State to = new State();
        Object input = new Object();
        
        ImmutableDFA.Builder builder = new ImmutableDFA.Builder();
        builder.addTransition(from, to, input);
        
        ImmutableDFA immutableDFA = builder.buildDFA();
        assertNotNull(immutableDFA);
    }

    @Test
    public void testBuildInvalidDFA() {
        // try to build a DFA with no InitialState
        State from = new State();
        State to = new State();
        Object input = new Object();
        
        ImmutableDFA.Builder builder = new ImmutableDFA.Builder();
        builder.addTransition(from, to, input);
        
        ImmutableDFA immutableDFA = builder.buildDFA();
        assertNull(immutableDFA);
    }
    
    @Test
    public void testBuildMultipleTimes() {
        State from = new InitialState();
        State to = new State();
        Object input = new Object();
        
        ImmutableDFA.Builder builder = new ImmutableDFA.Builder();
        builder.addTransition(from, to, input);
        
        ImmutableDFA immutableDFA = builder.buildDFA();
        ImmutableDFA immutableDFA1 = builder.buildDFA();
        
        assertNotNull(immutableDFA);
        assertNotNull(immutableDFA1);
        assertFalse(immutableDFA == immutableDFA1);
        assertFalse(immutableDFA.equals(immutableDFA1));
    }
}
