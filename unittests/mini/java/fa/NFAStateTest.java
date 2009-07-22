package mini.java.fa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.HashSet;

import mini.java.fa.helper.Helper;

import org.junit.Test;

public class NFAStateTest {
    @Test
    public final void testGetClosure() {
        // TEST:        GetClosure() should always return an object
        NFAState state = new NFAState();
        assertNotNull(state);
        assertNotNull(state.getClosure());
    }

//    @Test    
//    public final void testAddTransition() {
//        NFAState A = new NFAState(),
//            B = new NFAState(), C = new NFAState();
//        A.addTransition(B, "a");
//        A.addTransition(C, "a"); // should override the previous one
//        
//        assertEquals(C, A.getState("a"));
//    }
    
    
    @Test
    public void testAddTransition() {
        NFAState A = new NFAState(),
            B = new NFAState(), C = new NFAState();
        A.addTransition(B, "a");
        A.addTransition(C, "a"); // another with the same input
        
        assertNotNull("NFAState should be able to handle multiple transitions with the same input",
                A.getEpsilons());
        assertEquals(1, A.getEpsilons().size());
        assertEquals(new HashSet<NFAState>(Arrays.asList(B, C)),
                A.getClosure().getClosure("a").getStates());
        
        assertEquals(B, A.getState("a"));
        assertEquals("0 =>(a) 1\n", Helper.dump(Helper.collapse(A)));
    }
}
