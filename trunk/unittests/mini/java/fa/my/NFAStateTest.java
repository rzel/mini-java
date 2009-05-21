package mini.java.fa.my;

import static org.junit.Assert.assertNotNull;
import mini.java.fa.NFAState;

import org.junit.Test;

public class NFAStateTest {
    @Test
    public final void testGetClosure() {
        // TEST:        GetClosure() should always return an object
        NFAState state = new NFAState();
        assertNotNull(state);
        assertNotNull(state.getClosure());
    }

}
