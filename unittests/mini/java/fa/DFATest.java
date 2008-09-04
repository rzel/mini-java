package mini.java.fa;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DFATest {
    private static int MAXCOUNT = 10;
    private DFA _dfa;

    // Constructor. Get the DFA implementation from getParameters().
    public DFATest(DFA dfa_) {
        assertNotNull(dfa_); // avoid error
        _dfa = dfa_;
    }

    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {{ createImmutableDFA() }});
    }

    // factory method used by getParameters()
    // this will build some DFA randomly
    public static ImmutableDFA createImmutableDFA() {
        ImmutableDFA.Builder builder = new ImmutableDFA.Builder();
        InitialState initialState = new InitialState();
        Random rnd = new Random();
        // There should be at least one transition
        for (int i = 0, n = rnd.nextInt(MAXCOUNT)+1; i < n; ++i) {
            builder.addTransition(initialState, new State(), new Object());
        }
        return builder.buildDFA();
    }

    @Test
    public void testGetState() {
        InitialState initialState = _dfa.getInitialState();
        Set<Object> inputs = _dfa.getInputs(initialState);
        for (Object input : inputs) {
            // make sure every transition is valid
            assertNotNull(_dfa.getState(initialState, input));
        }
    }

    @Test
    public void testGetStateWithNullInput() {
        InitialState initialState = _dfa.getInitialState();
        // try to use Null as input
        assertNull(_dfa.getState(initialState, null));
    }

    @Test
    public void testGetStateNull() {
        InitialState initialState = _dfa.getInitialState();
        // try to use an invalid input
        assertNull(_dfa.getState(initialState, new Object()));
    }

    @Test
    public void testGetInitialState() {
        InitialState initialState = _dfa.getInitialState();
        // make sure every DFA has an initial state
        assertNotNull(initialState);
    }
}
