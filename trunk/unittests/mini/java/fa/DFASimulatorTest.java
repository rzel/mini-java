package mini.java.fa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test cases for interface DFASimulator.
 *
 * @author Alex
 */
@RunWith(Parameterized.class)
public class DFASimulatorTest {
    private static State S1 = new InitialState();
    private static State S2 = new State();
    private static State S3 = new State();
    
    private static Object INPUT1 = new Object();
    private static Object INPUT2 = new Object();
    
    // the simulator being tested
    private DFASimulator _simulator;
    
    @Parameters
    public static Collection<Object[]> getParameters() {
        DFA dfa = createImmutableDFA();
        return Arrays.asList(new Object[][] {{ new DFASimulatorImpl(dfa) }});
    }
    
    public DFASimulatorTest(DFASimulator simulator_) {
        _simulator = simulator_;
    }

    // factory method used by getParameters()
    // this will build a simple test DFA
    public static ImmutableDFA createImmutableDFA() {
        ImmutableDFA.Builder builder = new ImmutableDFA.Builder();
        builder.addTransition(S1, S2, INPUT1);
        builder.addTransition(S2, S3, INPUT2);
        
        return builder.buildDFA();
    }
    
    @Test
    public void testReset() {
        _simulator.reset();
        assertTrue(_simulator.isRunning());
        assertTrue(_simulator.getDFAState() instanceof InitialState);
    }
    
    @Test
    public void testResetAfterStopped() {
        _simulator.reset();
        _simulator.step(new Object()); // use a different input
        assertFalse(_simulator.isRunning());
        
        _simulator.reset();
        assertTrue(_simulator.isRunning());
    }
    
    @Test
    public void testStepOneStep() {
        _simulator.reset();
        _simulator.step(INPUT1);
        assertEquals(_simulator.getDFAState(), S2);
        assertTrue(_simulator.isRunning());
    }
    
    @Test
    public void testStepMultipleSteps() {
        _simulator.reset();
        _simulator.step(INPUT1);
        _simulator.step(INPUT2);
        assertEquals(_simulator.getDFAState(), S3);
        assertTrue(_simulator.isRunning());
    }
    
    @Test
    public void testStepStopped() {
        _simulator.reset();
        _simulator.step(new Object()); // use a different input
        assertFalse(_simulator.isRunning());
    }
    
    @Test
    public void testStepAfterStopped() {
        _simulator.reset();
        _simulator.step(new Object()); // use a different input
        assertFalse(_simulator.isRunning());
        
        _simulator.step(INPUT1); // should be ignored
        assertEquals(_simulator.getDFAState(), S1);
    }
}
