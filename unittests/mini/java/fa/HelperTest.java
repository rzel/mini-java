package mini.java.fa;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

public class HelperTest {

    // helper function used to get the only element from a singleton set
    private static <T> T getSingletonContent(Set<T> singleton_) {
        assert(singleton_.size() == 1); // singleton_ must be a real singleton
        
        T ret = null;
        for (T element : singleton_) {
            ret = element;
        }
        // to make compiler happy
        return ret;
    }
    
    @Test
    public final void testBuildNFASingleTransition() {        
        ImmutableNFA immutableNFA = Helper.buildNFA("ABa");
        assertNotNull(immutableNFA); // avoid error
        
        // check initial state
        State source = immutableNFA.getInitialState();
        assertNotNull(source);
                
        // check inputs for initial state
        Set<Object> inputs = immutableNFA.getInputs(source);
        assertNotNull(inputs);
        assertTrue(inputs.size() == 1);        
        Object input = getSingletonContent(inputs);
        assertNotNull(input); // avoid error
        
        // check target state
        Set<State> targets = immutableNFA.closure(source, input);
        assertTrue(targets.size() == 1);
    }
    
    @Test
    public final void testBuildNFAMultipleTransitionsDifferentInputs() {
        ImmutableNFA immutableNFA = Helper.buildNFA("ABa,ABb");
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
    
    @Test
    public final void testBuildNFAMultipleTransitionsDifferentTransitionsLinked() {
        ImmutableNFA immutableNFA = Helper.buildNFA("ABa,BCb");
        assertNotNull(immutableNFA); // avoid error
        
        // get initial state "A"
        State A = immutableNFA.getInitialState();
        assertNotNull(A);
        
        // get input for state "A" -- "a"
        Set<Object> inputsForA = immutableNFA.getInputs(A);
        assertNotNull(inputsForA);
        assertTrue(inputsForA.size() == 1);        
        Object inputForA = getSingletonContent(inputsForA);
        assertNotNull(inputForA);
        
        // get state "B"
        Set<State> Bs = immutableNFA.closure(A, inputForA);
        assertNotNull(Bs);
        assertTrue(Bs.size() == 1);        
        State B = getSingletonContent(Bs);
        assertNotNull(B);
        
        // get input for state "B" -- "b"
        Set<Object> inputsForB = immutableNFA.getInputs(B);
        assertNotNull(inputsForB);
        assertTrue(inputsForB.size() == 1);        
        Object inputForB = getSingletonContent(inputsForB);
        assertNotNull(inputForB);
        
        // get state "C"
        Set<State> Cs = immutableNFA.closure(B, inputForB);
        assertNotNull(Cs);
        assertTrue(Cs.size() == 1);
    }
    
    @Test
    public final void testBuildNFAMultipleTransitionsDifferentTransitions() {
        ImmutableNFA immutableNFA = Helper.buildNFA("ABa,CDc,BCb");
        assertNotNull(immutableNFA); // avoid error
        
        // get initial state "A"
        State A = immutableNFA.getInitialState();
        assertNotNull(A);
        
        // get state "B"
        Set<State> Bs = immutableNFA.closure(A, 'a');
        assertNotNull(Bs);
        assertTrue(Bs.size() == 1);
        State B = getSingletonContent(Bs);
                
        // get state "C"
        Set<State> Cs = immutableNFA.closure(B, 'b');
        assertNotNull(Cs);
        assertTrue(Cs.size() == 1);
        State C = getSingletonContent(Cs);
        
        // get state "D"
        Set<State> Ds = immutableNFA.closure(C, 'c');
        assertNotNull(Ds);
        assertTrue(Ds.size() == 1);
    }
    
    @Test
    public final void testBuildNFASingleEpsilonTransition() {
        ImmutableNFA immutableNFA = Helper.buildNFA("AB");
        assertNotNull(immutableNFA); // avoid error
        
        // get initial state "A"
        State A = immutableNFA.getInitialState();
        assertNotNull(A);
        
        Set<State> closure = immutableNFA.closure(A);
        assertNotNull(closure);
        assertTrue(closure.size() == 2);
    }

}
