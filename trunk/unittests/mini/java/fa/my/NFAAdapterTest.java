package mini.java.fa.my;

import mini.java.fa.NFATest;
import mini.java.fa.adapter.V3Adapter;
import mini.java.fa.v3.NFABuilder;

public class NFAAdapterTest extends NFATest {

    @Override
    protected NFABuilder getBuilder() {
        return new V3Adapter();
    }

}
