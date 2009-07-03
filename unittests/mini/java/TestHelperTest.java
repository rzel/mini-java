package mini.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import mini.java.fa.v3.DFA;
import mini.java.fa.v3.NFA;
import mini.java.fa.v3.State;

import org.junit.Test;

public class TestHelperTest {

    // helper function used to get a single element from the collection
    private static <T> T getSingleton(Collection<T> collection_) {
        assert(collection_ != null);
        
        T ret = null;
        for (T element : collection_) {
            ret = element;
            break; // we need only one element
        }        
        return ret;
    }
    
    @Test
    public final void testBuildNFASingleTransition() {        
        NFA immutableNFA = TestHelper.buildNFA("ABa");
        assertNotNull(immutableNFA); // avoid error
        
        // check initial state
        State source = immutableNFA.getInitialState();
        assertNotNull(source);
                
        // check inputs for initial state
        Set<Object> inputs = immutableNFA.getInputs(source);
//        assertNotNull(inputs);
//        assertTrue(inputs.size() == 1);
        assertEquals(inputs, Collections.singleton('a'));
//        Object input = getSingleton(inputs);
//        assertNotNull(input); // avoid error
        
        // check target state
        Set<State> targets = immutableNFA.closure(source, 'a');
        assertTrue(targets.size() == 1);
    }
    
    @Test
    public final void testBuildNFAMultipleTransitionsDifferentInputs() {
        NFA immutableNFA = TestHelper.buildNFA("ABa,ABb");
        assertNotNull(immutableNFA); // avoid error

        // check initial state
        State source = immutableNFA.getInitialState();
        assertNotNull(source);

        // check inputs for initial state
        Set<Object> inputs = immutableNFA.getInputs(source);
        assertNotNull(inputs);
        assertTrue(inputs.size() == 2);

        // check target states
        for (Object input : inputs) {
            Set<State> targets = immutableNFA.closure(source, input);
            assertTrue(targets.size() == 1);
        }
    }
    
    // NOTE: same input for multiple targets is prohibited
    
//    @Test
//    public final void testBuildNFAMultipleTransitionsDifferentTransitionsDifferentSources() {
//        ImmutableNFA immutableNFA = Helper.buildNFA("ABa,BCb");
//        assertNotNull(immutableNFA); // avoid error
//        
//        // get initial state "A"
//        State A = immutableNFA.getInitialState();
//        assertNotNull(A);
//        
//        // get input for state "A" -- "a"
//        Set<Object> inputsForA = immutableNFA.getInputs(A);
//        assertNotNull(inputsForA);
//        assertTrue(inputsForA.size() == 1);        
//        Object inputForA = getSingleton(inputsForA);
//        assertNotNull(inputForA);
//        
//        // get state "B"
//        Set<State> Bs = immutableNFA.closure(A, inputForA);
//        assertNotNull(Bs);
//        assertTrue(Bs.size() == 1);        
//        State B = getSingleton(Bs);
//        assertNotNull(B);
//        
//        // get input for state "B" -- "b"
//        Set<Object> inputsForB = immutableNFA.getInputs(B);
//        assertNotNull(inputsForB);
//        assertTrue(inputsForB.size() == 1);        
//        Object inputForB = getSingleton(inputsForB);
//        assertNotNull(inputForB);
//        
//        // get state "C"
//        Set<State> Cs = immutableNFA.closure(B, inputForB);
//        assertNotNull(Cs);
//        assertTrue(Cs.size() == 1);
//    }
    
    @Test
    public final void testBuildNFAMultipleTransitionsDifferentTransitions() {
        NFA immutableNFA = TestHelper.buildNFA("ABa,CDc,BCb");
        assertNotNull(immutableNFA); // avoid error
        
        // get initial state "A"
        State A = immutableNFA.getInitialState();
        assertNotNull(A);
        
        // get state "B"
        Set<State> Bs = immutableNFA.closure(A, 'a');
        assertNotNull(Bs);
        assertTrue(Bs.size() == 1);
        State B = getSingleton(Bs);
                
        // get state "C"
        Set<State> Cs = immutableNFA.closure(B, 'b');
        assertNotNull(Cs);
        assertTrue(Cs.size() == 1);
        State C = getSingleton(Cs);
        
        // get state "D"
        Set<State> Ds = immutableNFA.closure(C, 'c');
        assertNotNull(Ds);
        assertTrue(Ds.size() == 1);
    }
    
    @Test
    public final void testBuildNFASingleEpsilonTransition() {
        NFA immutableNFA = TestHelper.buildNFA("AB");
        assertNotNull(immutableNFA); // avoid error
        
        // get initial state "A"
        State A = immutableNFA.getInitialState();
        assertNotNull(A);
        
        Set<State> closure = immutableNFA.closure(A);
        assertNotNull(closure);
        assertTrue(closure.size() == 2);
    }
    
    @Test
    public final void testBuildDFASingleTransition() {
        DFA immutableDFA = TestHelper.buildDFA("ABa");
        assertNotNull(immutableDFA);
        
        // get initial state "A"
        State A = immutableDFA.getInitialState();
        assertNotNull(A);
        
        // check inputs for initial state
        Set<Object> inputs = immutableDFA.getInputs(A);
        //assertNotNull(inputs);
        //assertTrue(inputs.size() == 1);
        assertEquals(inputs, Collections.singleton('a'));
        //Object input = getSingleton(inputs);
        //assertNotNull(input); // avoid error
        
        State B = immutableDFA.getState(A, 'a');
        assertNotNull(B);
    }

    @Test
    public final void testBuildDFAMultipleTransitionsDifferentInputs() {
        DFA immutableDFA = TestHelper.buildDFA("ABb,ACc");
        assertNotNull(immutableDFA);
        
        // get initial state "A"
        State A = immutableDFA.getInitialState();
        assertNotNull(A);
        
        // check inputs for initial state
        Set<Object> inputs = immutableDFA.getInputs(A);
        assertNotNull(inputs);
        assertTrue(inputs.size() == 2);

        for (Object input : inputs) {
            State BorC = immutableDFA.getState(A, input);
            assertNotNull(BorC);
        }
    }
    
    @Test
    public final void testBuildDFAMultipleTransitionsDifferentTransitions() {
        DFA immutableDFA = TestHelper.buildDFA("ABa,CDc,BCb");
        assertNotNull(immutableDFA); // avoid error
        
        // get initial state "A"
        State A = immutableDFA.getInitialState();
        assertNotNull(A);        
        // get state "B"
        State B = immutableDFA.getState(A, 'a');
        assertNotNull(B);        
        // get state "C"
        State C = immutableDFA.getState(B, 'b');
        assertNotNull(C);        
        // get state "D"
        State D = immutableDFA.getState(C, 'c');
        assertNotNull(D);
    }
}
