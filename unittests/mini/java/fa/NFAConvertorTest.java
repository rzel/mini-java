package mini.java.fa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import mini.java.TestHelper;
import mini.java.fa.helper.Helper;
import mini.java.fa.v3.DFA;

import org.junit.Test;

public class NFAConvertorTest extends NFAConversionData {
    private String _nfa; // for NFA
    private String _dfa; // for DFA

    public NFAConvertorTest(String nfa_, String dfa_) {
        assert (nfa_ != null);
        assert (dfa_ != null);
        _nfa = nfa_;
        _dfa = dfa_;
    }
    
    @Test
    public final void testBuildDFA() {
        DFA got = Helper.collapse(TestHelper.buildNFA(_nfa));
        DFA expected = TestHelper.buildDFA(_dfa);
        
        assertNotNull(got);
        assertNotNull(expected);
        assertEquals("" + Helper.dumpString(expected), "" + Helper.dumpString(got));
    }
}
