package mini.java.fa;

import mini.java.fa.v1.adapter.SimpleFAtoNFAAdapter;
import mini.java.fa.v3.NFABuilder;
import mini.java.fa.v3.NFATest;

import org.junit.Test;


public class SimpleFATest  extends NFATest {
    @Override
    protected NFABuilder getBuilder() {
        return new SimpleFAtoNFAAdapter();
    }
}
