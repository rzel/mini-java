package mini.java.fa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ImmutableDFABuilderTest {

    @Test
    public void testAddSingleTransition() {
        State from = new InitialState();
        State to = new State();
        Object input = new Object();
        
        ImmutableDFA.Builder builder = new ImmutableDFA.Builder();
        builder.addTransition(from, to, input);
        
        DFA immutableDFA = builder.buildDFA();
        assertNotNull(immutableDFA); // avoid error
        assertEquals(immutableDFA.getState(from, input), to);
    }
    
//    @Test
//    public void testAddMultipleTransitionsWithInvalidInput() {
//        State s1 = new InitialState();
//        State s2 = new State();
//        State s3 = new State();
//        Object input1 = new Object();
//        Object input2 = null;
//        
//        ImmutableDFA.Builder builder = new ImmutableDFA.Builder();
//        builder.addTransition(s1, s2, input1);
//        builder.addTransition(s1, s3, input2);
//        
//        ImmutableDFA immutableDFA = builder.buildDFA();
//        assertNotNull(immutableDFA);
//        assertEquals(immutableDFA.getState(s1, input1), s2);
//        // the transition from s1 to s3 with null input should be ignored
//        assertNull(immutableDFA.getState(s1, input2));
//    }
    
    @Test
    public void testAddMultipleTransitionsWithSameSource() {
        State from = new InitialState();
        State to = new State();
        State to1 = new State();
        
        Object input = new Object();
        Object input1 = new Object();
        
        ImmutableDFA.Builder builder = new ImmutableDFA.Builder();
        builder.addTransition(from, to, input);
        builder.addTransition(from, to1, input1);
        
        DFA immutableDFA = builder.buildDFA();
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
        
        DFA immutableDFA = builder.buildDFA();
        assertNotNull(immutableDFA);
        assertEquals(immutableDFA.getState(s1, input), s2);
        assertEquals(immutableDFA.getState(s2, input1), s3);
    }
    
    @Test
    public void testAddMultipleTransitionsWithSameInputs() {
        State from = new InitialState();
        State to = new State();
        State to1 = new State();
        // same input for different target states
        Object input = new Object();
        
        ImmutableDFA.Builder builder = new ImmutableDFA.Builder();
        builder.addTransition(from, to, input);
        builder.addTransition(from, to1, input); // should override previous one
        
        DFA immutableDFA = builder.buildDFA();
        assertNotNull(immutableDFA);
        assertEquals(immutableDFA.getState(from, input), to1);
    }
    
//    @Test
//    public void testAddMultipleInitialStates() {
//        State s1 = new InitialState();
//        State s2 = new InitialState();
//        State s3 = new State();
//
//        Object input = new Object();
//        
//        ImmutableDFA.Builder builder = new ImmutableDFA.Builder();
//        builder.addTransition(s1, s3, input);
//        builder.addTransition(s2, s3, input); // should be ignored
//        
//        ImmutableDFA immutableDFA = builder.buildDFA();
//        assertNotNull(immutableDFA);
//        assertNull(immutableDFA.getState(s2, input));
//    }
    
    @Test
    public void testAddLoopTransition() {
        State  state    = new InitialState();
        Object input    = new Object();

        ImmutableDFA.Builder builder = new ImmutableDFA.Builder();
        builder.addTransition(state, state, input);

        DFA immutableDFA = builder.buildDFA();
        assertNotNull(immutableDFA);
        assertEquals(immutableDFA.getState(state, input), state);
    }
    
//    @Test
//    public void testAddNullTargetState() {
//        State from = new InitialState();
//        State to = null; // invalid target state
//        Object input = new Object();
//        
//        ImmutableDFA.Builder builder = new ImmutableDFA.Builder();
//        builder.addTransition(from, to, input);
//        
//        ImmutableDFA immutableDFA = builder.buildDFA();
//        assertNull(immutableDFA);
//    }
    
    @Test
    public void testAddNullSourceState() {
        // Not needed. A source state is also a target state unless it's an
        // initial state. Both cases have already been covered by other tests.
    }
    
//    @Test
//    public void testAddNullInput() {
//        State from = new InitialState();
//        State to = new State();
//        Object input = null; // null input should be ignored
//        
//        ImmutableDFA.Builder builder = new ImmutableDFA.Builder();
//        builder.addTransition(from, to, input);
//        
//        ImmutableDFA immutableDFA = builder.buildDFA();
//        assertNull(immutableDFA);
//    }

//    @Test
//    public void testAddInvalidTargetState() {
//        State from = new InitialState();
//        // try to add an invalid target state
//        State to = new InitialState();
//        // make sure the DFA can be built
//        State to1 = new State();
//        
//        Object input = new Object();
//        Object input1 = new Object();
//        
//        ImmutableDFA.Builder builder = new ImmutableDFA.Builder();
//        builder.addTransition(from, to, input); // should be ignored
//        builder.addTransition(from, to1, input1);
//        
//        ImmutableDFA immutableDFA = builder.buildDFA();
//        assertNotNull(immutableDFA);
//        assertNull(immutableDFA.getState(from, input));
//    }

    @Test
    public void testBuildEmptyDFA() {
        ImmutableDFA.Builder builder = new ImmutableDFA.Builder();
        DFA immutableDFA = builder.buildDFA();
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
        
        DFA immutableDFA = builder.buildDFA();
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
        
        DFA immutableDFA = builder.buildDFA();
        assertNull(immutableDFA);
    }
    
    @Test
    public void testBuildMultipleTimes() {
        State from = new InitialState();
        State to = new State();
        Object input = new Object();
        
        ImmutableDFA.Builder builder = new ImmutableDFA.Builder();
        builder.addTransition(from, to, input);
        
        DFA firstDFA = builder.buildDFA();
        DFA secondDFA = builder.buildDFA();        
        
        assertNotNull(firstDFA);
        assertNotNull(secondDFA);
        
        // same instance should be returned
        assertTrue(firstDFA == secondDFA);
//        assertTrue(firstDFA.equals(secondDFA));
    }
}
