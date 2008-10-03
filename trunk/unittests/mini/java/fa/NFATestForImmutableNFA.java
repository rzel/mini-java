package mini.java.fa;

public class NFATestForImmutableNFA extends NFATest {    
    public NFATestForImmutableNFA() {
        super();
    }
    
    @Override
    protected NFA createNFA() {
        return TestHelper.buildNFA("ABa,BCb,CDc,AC,AD,DE");
    }
}