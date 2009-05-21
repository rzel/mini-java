package mini.java.fa;

import mini.java.fa.v3.DFABuilder;
import mini.java.fa.v3.impl.ImmutableDFA;


public class DFATestForImmutableDFA extends DFATest {
    @Override
    protected DFABuilder getBuilder() {
        return new ImmutableDFA.Builder();
    }
}
