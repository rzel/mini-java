package mini.java.fa;

import mini.java.fa.legacy.v1.NFAAdapter;


public class NFABuilderTestForNFAAdapter  extends NFABuilderTest {
    protected NFABuilder createNFABuilder() {
        return new NFAAdapter();
    }
}
