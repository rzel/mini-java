package mini.java.fa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import mini.java.TestHelper;
import mini.java.fa.v3.DFA;
import mini.java.fa.v3.helper.DFAComparator;

import org.junit.Test;

public class DFAComparatorTest extends FAComparisonTest {
    private DFA _dfaA;
    private DFA _dfaB;
    private boolean _result;

    public DFAComparatorTest(String dfaA_, String dfaB_, boolean result_) {
        assert(dfaA_ != null);
        assert(dfaB_ != null);
        
        _dfaA = TestHelper.buildDFA(dfaA_);
        _dfaB = TestHelper.buildDFA(dfaB_);
        _result = result_;
        
        assertNotNull(_dfaA);
        assertNotNull(_dfaB);
    }
    
    @Test
    public final void testCompare() {
        assertEquals(_result, new DFAComparator(_dfaA, _dfaB).compare());
    }
}

