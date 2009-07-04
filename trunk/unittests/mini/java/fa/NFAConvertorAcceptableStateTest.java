package mini.java.fa;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import mini.java.ComponentFactory;
import mini.java.TestHelper;
import mini.java.fa.helper.Helper;
import mini.java.fa.v3.Acceptable;
import mini.java.fa.v3.AcceptableInitialState;
import mini.java.fa.v3.AcceptableState;
import mini.java.fa.v3.DFA;
import mini.java.fa.v3.InitialState;
import mini.java.fa.v3.NFA;
import mini.java.fa.v3.NFABuilder;
import mini.java.fa.v3.State;

import org.junit.Test;


public class NFAConvertorAcceptableStateTest {
    
    @Test
    public final void testBuildDFAAcceptableInitialState() {
        State A = new InitialState();
        State B = new AcceptableState();
        Object input = new Object();
        
//        ImmutableNFA.Builder builder = new ImmutableNFA.Builder();
        NFABuilder builder = ComponentFactory.createNFABuilder();
        builder.addTransition(A, B);
        builder.addTransition(B, A, input);
        
        NFA nfa = builder.buildNFA();
        assertNotNull(nfa);
        
        DFA dfa = Helper.collapse(nfa);
        assertNotNull(dfa);
        
        InitialState initialState = dfa.getInitialState();
        assertNotNull(initialState);
        assertTrue(initialState instanceof Acceptable);
        
        State state = dfa.getState(initialState, input);
        assertNotNull(state);
        assertTrue(state instanceof AcceptableInitialState);
        // the result dfa should contain only one state
        assertTrue(state == initialState);
    }
    
    @Test
    public final void testBuildDFAInitialState() {
        State A = new InitialState();
        State B = new State();
        Object input = new Object();
        
//        ImmutableNFA.Builder builder = new ImmutableNFA.Builder();
        NFABuilder builder = ComponentFactory.createNFABuilder();
        builder.addTransition(A, B, input);
        
        NFA nfa = builder.buildNFA();
        assertNotNull(nfa);
        
        DFA dfa = Helper.collapse(nfa);
        assertNotNull(dfa);
        
        InitialState initialState = dfa.getInitialState();
        assertNotNull(initialState);
        
        State state = dfa.getState(initialState, input);
        assertNotNull(state);
    }
    
    @Test
    public final void testBuildDFAAcceptableState() {
        State A = new InitialState();
        State B = new State();
        State C = new AcceptableState();
        Object input = new Object();
        
//        ImmutableNFA.Builder builder = new ImmutableNFA.Builder();
        NFABuilder builder = ComponentFactory.createNFABuilder();
        builder.addTransition(A, B, input);
        builder.addTransition(B, C);
        
        NFA nfa = builder.buildNFA();
        assertNotNull(nfa);
        
        DFA dfa = Helper.collapse(nfa);
        assertNotNull(dfa);
        
        InitialState initialState = dfa.getInitialState();
        assertNotNull(initialState);
        
        State state = dfa.getState(initialState, input);
        assertNotNull(state);
        assertTrue(state instanceof AcceptableState);
    }
    
    @Test
    public final void testBuildDFAMultipleAcceptableStates() {
        State A = new AcceptableInitialState();
        State B = new State();
        Object input = new Object();
        
//        ImmutableNFA.Builder builder = new ImmutableNFA.Builder();
        NFABuilder builder = ComponentFactory.createNFABuilder();
        builder.addTransition(A, B, input);
        builder.addTransition(B, A);
        
        NFA nfa = builder.buildNFA();
        assertNotNull(nfa);
        
        DFA dfa = Helper.collapse(nfa);
        assertNotNull(dfa);
        
        InitialState initialState = dfa.getInitialState();
        assertNotNull(initialState);
        assertTrue(initialState instanceof Acceptable);
        
        State state = dfa.getState(initialState, input);
        assertNotNull(state);
        assertTrue(state instanceof Acceptable);
    }
    
    @Test
    public final void testBuildDFAInvalidDFA() {
        NFA nfa = TestHelper.buildNFA("AB"); // no valid transition for DFA
        DFA dfa = Helper.collapse(nfa);
        assertNull(dfa); // invalid DFA
    }
}
