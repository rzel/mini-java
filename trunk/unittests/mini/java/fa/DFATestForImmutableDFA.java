package mini.java.fa;


public class DFATestForImmutableDFA extends DFATest {
    @Override
    protected DFABuilder getBuilder() {
        return new ImmutableDFA.Builder();
    }
}
