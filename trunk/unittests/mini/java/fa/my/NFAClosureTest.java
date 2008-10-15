package mini.java.fa.my;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;


public class NFAClosureTest {
    private static NFAState S0, S1;
    private static Object O1, O2;
    
    @Before
    public void setUp() {
        S0 = new AcceptableNFAState();
        S1 = new NFAState();
        O1 = new Object();
        O2 = new Object();
    }
    
    @Test
    public void testGetClosure001() {
        // TEST:        Get Null Closure (No Target States)
        // INPUTS:      S0
        NFAClosure closure = new NFAClosure(S0);
        assertNotNull(closure);
        assertNull(closure.getClosure(O1));
    }

    @Test
    public void testGetClosure002() {
        // TEST:        Get Null Closure (Invalid Input)
        // INPUTS:      (S0,S1,O1)
        S0.addTransition(S1, O1);
        
        NFAClosure closure = new NFAClosure(S0);
        assertNotNull(closure);
        assertNotNull(closure.getClosure(O1));
        assertNull(closure.getClosure(O2));
    }
    
    @Test
    public void testGetInputs001() {
        // TEST:        Get Single Input
        // INPUTS:      (S0,S1,O1)
        S0.addTransition(S1, O1);
        
        NFAClosure closure = new NFAClosure(S0);
        assertNotNull(closure);
        
        Set<Object> expected = Collections.singleton(O1);
        Set<Object> got = closure.getInputs();
        assertEquals(got, expected);
    }
    
    @Test
    public void testGetInputs002() {
        // TEST:        Get Multiple Inputs
        // INPUTS:      (S0,S1,O1)(S0,S1,O2)
        S0.addTransition(S1, O1);
        S0.addTransition(S1, O2);
        
        NFAClosure closure = new NFAClosure(S0);
        assertNotNull(closure);
        
        Set<Object> expected = new HashSet<Object>(Arrays.asList(O1, O2));
        Set<Object> got = closure.getInputs();
        assertEquals(got, expected);
    }
    
    @Test
    public void testGetInputs003() {
        // TEST:        Get Null Input
        // INPUTS:      S0
        NFAClosure closure = new NFAClosure(S0);
        assertNotNull(closure);
        
        Set<Object> expected = Collections.emptySet();
        Set<Object> got = closure.getInputs();
        assertEquals(got, expected);
    }
    
    @Test
    public void testIsAcceptable001() {
        // TEST:        Single Acceptable State
        // INPUTS:      S0
        NFAClosure closure = new NFAClosure(S0);
        assertNotNull(closure);
        assertEquals(true, closure.isAcceptable());
    }
    
    @Test
    public void testIsAcceptable002() {
        // TEST:        Single Non-Acceptable State
        // INPUTS:      S1
        NFAClosure closure = new NFAClosure(S1);
        assertNotNull(closure);
        assertEquals(false, closure.isAcceptable());
    }
    
    @Test
    public void testIsAcceptable003() {
        // TEST:        Multiple States
        // INPUTS:      S0,S1
        NFAClosure closure = new NFAClosure(new HashSet<NFAState>(Arrays.asList(S0, S1)));
        assertNotNull(closure);
        assertEquals(true, closure.isAcceptable());
    }
}
