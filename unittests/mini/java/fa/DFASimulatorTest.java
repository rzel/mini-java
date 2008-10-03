package mini.java.fa;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runners.Parameterized.Parameters;


public abstract class DFASimulatorTest {
    private DFA _dfa;
    private char[] _inputs;
    private boolean _isRunning;
    private DFASimulator _simulator;
    
    // factory method for DFASimulator implementations
    protected abstract DFASimulator getSimulator(DFA dfa_);
    
    public DFASimulatorTest(String dfa_, String inputs_, boolean isRunning_) {
        assert(dfa_     != null);
        assert(inputs_  != null);
        
        _dfa = TestHelper.buildDFA(dfa_);
        _inputs = inputs_.toCharArray();
        _isRunning = isRunning_;
        
        assert(_dfa     != null);
        assert(_inputs  != null);
        
        _simulator = getSimulator(_dfa);
        assert(_simulator != null);
    }
    
    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
                {"ABa",         "a",    true},
                {"ABa",         "b",    false},
                {"ABa",         "ab",   false},
                {"ABa,BCb",     "a",    true},
                {"ABa,BCb",     "ab",   true},
                {"ABa,BCb",     "aa",   false},
                {"ABa,ACb,BAc", "acb",  true},
        });
    }
    
    @Test
    public void testSimulator() {
        for (Object input : _inputs) {
            _simulator.step(input);
        }
        assertEquals(_isRunning, _simulator.isRunning());
    }
}

