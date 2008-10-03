package mini.java.fa;


public class NFATestForImmutableNFA extends NFATest {
    protected NFABuilder getBuilder() {
        return new ImmutableNFA.Builder();
    }
}
