package mini.java.fa.my;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import mini.java.TestHelperV2;
import mini.java.fa.AcceptableNFAState;
import mini.java.fa.NFAState;
import mini.java.fa.helper.Helper;
import mini.java.fa.v3.DFA;

import org.junit.Test;

public class HelperTest {

    @Test
    public void testCollapseAcceptableState() {
        Object input = new Object();
        NFAState A = new NFAState();
        NFAState B = new NFAState();
        NFAState C = new AcceptableNFAState();
        
        A.addTransition(B, input);
        B.addTransition(C);
        
        NFAState dfa = Helper.collapse(A);
        NFAState got = dfa.getState(input);
        assertTrue(got instanceof AcceptableNFAState);
    }
    
    @Test
    public void testCollapseInitialAcceptableState() {
        NFAState A = new NFAState();
        NFAState B = new AcceptableNFAState();
        
        A.addTransition(B);
        NFAState got = Helper.collapse(A);
        assertTrue(got instanceof AcceptableNFAState);
    }
    
    @Test
    public void testConvert() {
        TestHelperV2 helper = new TestHelperV2();
        helper.addNFAStates("AB");
        helper.addTransitions("ABa,BAa");
        
        NFAState v4 = helper.getNFAState('A');
        DFA v3 = Helper.convert(v4);
        assertEquals(Helper.dump(v4), Helper.dumpString(v3));
    }
}
