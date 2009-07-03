package mini.java.fa.my;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import mini.java.TestHelper;
import mini.java.fa.FAComparisonTest;
import mini.java.fa.Helper;
import mini.java.fa.v3.DFA;

import org.junit.Test;

public class DumpStringTest extends FAComparisonTest {
    private DFA _dfaA;
    private DFA _dfaB;
    private boolean _result;
    
    public DumpStringTest(String dfaA_, String dfaB_, boolean result_) {
        assert(dfaA_ != null);
        assert(dfaB_ != null);
        
        _dfaA = TestHelper.buildDFA(dfaA_);
        _dfaB = TestHelper.buildDFA(dfaB_);
        _result = result_;
        
        assertNotNull(_dfaA);
        assertNotNull(_dfaB);
    }
    
    @Test
    public final void testDumpString() {
        String A = Helper.dumpString(_dfaA);
        String B = Helper.dumpString(_dfaB);
        
        if (_result) {
            assertEquals(A, B);
        } else {
            assertFalse(("" + A).equals("" + B));
        }
    }
}

