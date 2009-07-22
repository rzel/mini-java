package mini.java.fa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class NFAStateTest {
    @Test
    public final void testGetClosure() {
        // TEST:        GetClosure() should always return an object
        NFAState state = new NFAState();
        assertNotNull(state);
        assertNotNull(state.getClosure());
    }

    @Test
    public final void testAddTransition() {
        NFAState A = new NFAState(),
            B = new NFAState(), C = new NFAState();
        A.addTransition(B, "a");
        A.addTransition(C, "a"); // should override the previous one
        
        assertEquals(C, A.getState("a"));
    }
}
