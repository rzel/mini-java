package mini.java.fa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class NFATest {
    private NFA _nfa;

    public NFATest(NFA nfa_) {
        assertNotNull(nfa_); // avoid error
        _nfa = nfa_;
    }
    
    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {{ createImmutableNFA() }});
    }
    
    public static ImmutableNFA createImmutableNFA() {
        return Helper.buildNFA("ABa,BCb,CDc,AC,AD,DE");
    }
    
    // this is not for the getter, but rather to make sure
    // the NFA is a valid one
    @Test
    public final void testGetInitialState() {
        assertNotNull(_nfa.getInitialState());
    }
    
    @Test
    public final void testGetInputs() {
        InitialState initialState = _nfa.getInitialState();
        assertNotNull(initialState); // avoid error
        
        Set<Object> got = _nfa.getInputs(initialState);
        Set<Object> expected = new HashSet<Object>();
        expected.add('a');
        expected.add('c');
        assertEquals(got, expected);
    }
    
    @Test
    public final void testSourceClosure() {
        InitialState initialState = _nfa.getInitialState();
        assertNotNull(initialState); // avoid error
        
        Set<State> got = _nfa.closure(initialState);
        assertNotNull(got);
        assertTrue(got.size() == 4);
    }
    
    @Test
    public final void testTargetClosure() {
        InitialState initialState = _nfa.getInitialState();
        assertNotNull(initialState); // avoid error
        
        Set<State> got = _nfa.closure(initialState, 'c');
        assertNotNull(got);
        assertTrue(got.size() == 2);
    }
}
