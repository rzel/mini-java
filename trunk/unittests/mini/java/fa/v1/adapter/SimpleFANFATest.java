package mini.java.fa.v1.adapter;

import mini.java.fa.v3.NFABuilder;
import mini.java.fa.v3.NFATest;


public class SimpleFANFATest  extends NFATest {
    @Override
    protected NFABuilder getBuilder() {
        return new SimpleFAtoNFAAdapter();
    }
}
