package mini.java.fa;

import mini.java.fa.v3.NFABuilder;
import mini.java.fa.v3.impl.ImmutableNFA;


public class NFATestForImmutableNFA extends NFATest {
    @Override
    protected NFABuilder getBuilder() {
        return new ImmutableNFA.Builder();
    }
}
