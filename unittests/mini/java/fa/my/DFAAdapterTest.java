package mini.java.fa.my;

import mini.java.fa.DFATest;
import mini.java.fa.adapter.V3Adapter;
import mini.java.fa.v3.DFABuilder;

public class DFAAdapterTest extends DFATest {

    @Override
    protected DFABuilder getBuilder() {
        return new V3Adapter();
    }

}
