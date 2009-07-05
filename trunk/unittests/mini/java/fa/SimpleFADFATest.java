package mini.java.fa;

import mini.java.fa.v1.adapter.SimpleFAtoNFAAdapter;
import mini.java.fa.v3.DFABuilder;
import mini.java.fa.v3.DFATest;


public class SimpleFADFATest extends DFATest {

    @Override
    protected DFABuilder getBuilder() {
        return new SimpleFAtoNFAAdapter();
    }
    
    @Override
    public void testAddTransition004() {
        // skipped
    }
}
