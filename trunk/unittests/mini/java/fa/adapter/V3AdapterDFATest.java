package mini.java.fa.adapter;

import mini.java.fa.adapter.V3Adapter;
import mini.java.fa.v3.DFABuilder;
import mini.java.fa.v3.DFATest;

public class V3AdapterDFATest extends DFATest {

    @Override
    protected DFABuilder getBuilder() {
        return new V3Adapter();
    }

}
