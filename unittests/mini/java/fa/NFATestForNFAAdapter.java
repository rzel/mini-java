package mini.java.fa;

import mini.java.fa.legacy.v1.NFAAdapter;


public class NFATestForNFAAdapter  extends NFATest {
    protected NFABuilder getBuilder() {
        return new NFAAdapter();
    }
}
