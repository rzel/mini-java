package mini.java.fa.my;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import mini.java.TestHelperV2;
import mini.java.fa.NFAConversionData;
import mini.java.fa.NFAState;
import mini.java.fa.helper.Helper;


public class HelperCollapseTest extends NFAConversionData {
    private final static String STATES = "ABCDE";
    private NFAState _A, _B;
    
    private static NFAState buildNFAState(String rep_) {
        TestHelperV2 helper = new TestHelperV2();
        helper.addNFAStates(STATES);
        helper.addTransitions(rep_);
        return helper.getNFAState(rep_.charAt(0));
    }
    
    public HelperCollapseTest(String A_, String B_) {
        _A = buildNFAState(A_);
        _B = buildNFAState(B_);
    }

    @Test
    public void testCollapse() {
        NFAState dfa = Helper.collapse(_A);
        String got = Helper.dump(dfa);
        String expected = Helper.dump(_B);

        assertEquals(expected, got);
    }
}
