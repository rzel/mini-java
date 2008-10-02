package mini.java.fa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public abstract class NFATest {
    private NFA _nfa;
    private InitialState _initialState;

    public NFATest() {
        _nfa = createNFA();
        _initialState = _nfa.getInitialState();
        
        assertNotNull(_nfa);
        assertNotNull(_initialState);
    }
    
    // should create an NFA with "ABa,BCb,CDc,AC,AD,DE"
    protected abstract NFA createNFA();
    
    @Test
    public final void testGetInputs() {
        Set<Object> got = _nfa.getInputs(_initialState);
        Set<Object> expected = new HashSet<Object>();
        expected.add('a');
        expected.add('c');
        assertEquals(expected, got);
    }
    
    @Test
    public final void testSourceClosure() {
        Set<State> got = _nfa.closure(_initialState);
        assertNotNull(got);
        assertEquals(4, got.size());
    }
    
    @Test
    public final void testTargetClosure() {
        Set<State> got = _nfa.closure(_initialState, 'c');
        assertNotNull(got);
        assertEquals(2, got.size());
    }
}
