package mini.java.fa.my;

import static org.junit.Assert.assertEquals;
import mini.java.TestHelperV2;
import mini.java.fa.Helper;
import mini.java.fa.NFAState;

import org.junit.Test;

public class HelperTest {

    @Test
    public final void testDumpString() {
        TestHelperV2 helper = new TestHelperV2();
        helper.addNFAStates("ABCDEF");
        helper.addTransitions("AB,AA,ACa,BDa,CEb,DFc");
        
        NFAState state = helper.getNFAState('A');
        String got = Helper.dumpString(state);
        String expected =
            "0 => 0\n"
          + "0 => 1\n"
          + "0 =>(a) 2\n"
          + "1 =>(a) 3\n"
          + "2 =>(b) 4\n"
          + "3 =>(c) 5\n";
        assertEquals(expected, got);        
    }

    @Test
    public final void testDumpStringSortedEpsilons() {
        TestHelperV2 helper = new TestHelperV2();
        helper.addNFAStates("ABCDEF");
        helper.addTransitions("AA,AB,AC,AD,AE,AF");
        
        NFAState state = helper.getNFAState('A');
        String got = Helper.dumpString(state);
        String expected =
            "0 => 0\n"
          + "0 => 1\n"
          + "0 => 2\n"
          + "0 => 3\n"
          + "0 => 4\n"
          + "0 => 5\n";
        assertEquals(expected, got);  
    }
}
