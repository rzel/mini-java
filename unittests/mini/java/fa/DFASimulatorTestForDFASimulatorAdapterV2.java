package mini.java.fa;

import mini.java.fa.v2.DFASimulatorAdapter;
import mini.java.fa.v3.DFA;
import mini.java.fa.v3.DFASimulator;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class DFASimulatorTestForDFASimulatorAdapterV2 extends DFASimulatorTest{
        public DFASimulatorTestForDFASimulatorAdapterV2(
                String dfa_, String inputs_, boolean isRunning_) {
            super(dfa_, inputs_, isRunning_);
        }
        
        @Override
        protected DFASimulator getSimulator(DFA dfa_) {
            return new DFASimulatorAdapter(dfa_);
        }
}
