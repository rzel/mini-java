package mini.java.fa.my;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import mini.java.TestHelperV2;
import mini.java.fa.FAComparisonTest;
import mini.java.fa.Helper;
import mini.java.fa.NFAState;

import org.junit.Test;

public final class DumpStringV1Test extends FAComparisonTest {
    private final static String STATES = "ABCDEFabc";
    private NFAState _A, _B;
    private boolean _result;

    private static NFAState buildNFAState(String rep_) {
        TestHelperV2 helper = new TestHelperV2();
        helper.addNFAStates(STATES);
        helper.addTransitions(rep_);
        return helper.getNFAState(rep_.charAt(0));
    }
    
    public DumpStringV1Test(String A_, String B_, boolean result_) {
        _A = buildNFAState(A_);
        _B = buildNFAState(B_);
        _result = result_;
    }        
        
    @Test
    public final void testDumpString() {
        String A = Helper.dumpString(_A);
        String B = Helper.dumpString(_B);
        
        if (_result) {
            assertEquals(A, B);
        } else {
            assertFalse(("" + A).equals("" + B));
        }
    }
}
