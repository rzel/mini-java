package mini.java.fa.v3;

import mini.java.fa.v3.DFA;
import mini.java.fa.v3.DFASimulator;
import mini.java.fa.v3.impl.DFASimulatorImpl;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class DFASimulatorTestForDFASimulatorImpl extends DFASimulatorTest {
    public DFASimulatorTestForDFASimulatorImpl(String dfa_, String inputs_, boolean isRunning_) {
        super(dfa_, inputs_, isRunning_);
    }
    
    @Override
    protected DFASimulator getSimulator(DFA dfa_) {
        return new DFASimulatorImpl(dfa_);
    }
}
