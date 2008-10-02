package mini.java.fa;

import mini.java.fa.legacy.v1.NFAAdapter;


public class NFATestForNFAAdapter extends NFATest {    
    public NFATestForNFAAdapter() {
        super();
    }
    
    @Override
    protected NFA createNFA() {
        return Helper.buildNFA("ABa,BCb,CDc,AC,AD,DE", new NFAAdapter());
    }
}
