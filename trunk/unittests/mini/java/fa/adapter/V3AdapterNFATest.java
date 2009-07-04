package mini.java.fa.adapter;

import mini.java.fa.adapter.V3Adapter;
import mini.java.fa.v3.NFABuilder;
import mini.java.fa.v3.NFATest;

public class V3AdapterNFATest extends NFATest {

    @Override
    protected NFABuilder getBuilder() {
        return new V3Adapter();
    }

}
