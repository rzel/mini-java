package mini.java.fa;


public class NFABuilderTestForImmutableNFABuilder extends NFABuilderTest {
    protected NFABuilder createNFABuilder() {
        return new ImmutableNFA.Builder();
    }
}
