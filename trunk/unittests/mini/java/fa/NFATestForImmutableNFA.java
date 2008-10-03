package mini.java.fa;


public class NFATestForImmutableNFA extends NFATest {
    @Override
    protected NFABuilder getBuilder() {
        return new ImmutableNFA.Builder();
    }
}
