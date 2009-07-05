package mini.java.fa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import mini.java.TestHelper;
import mini.java.fa.helper.Helper;
import mini.java.fa.helper.NFAConversionData;
import mini.java.fa.v1.SimpleFA;
import mini.java.fa.v1.adapter.InputConvertor;
import mini.java.fa.v1.adapter.SimpleFAtoNFAAdapter;
import mini.java.fa.v3.DFA;
import mini.java.fa.v3.NFA;

import org.junit.Test;


public class SimpleFAToDFATest extends NFAConversionData {
    private String _nfa; // for NFA
    private String _dfa; // for DFA

    public SimpleFAToDFATest(String nfa_, String dfa_) {
        _nfa = nfa_;
        _dfa = dfa_;
    }
    
    @Test
    public final void testToDFA() {
        // NFA(v3) --> NFA(v1)
        NFA nfa = TestHelper.buildNFA(_nfa, new SimpleFAtoNFAAdapter());
        SimpleFAtoNFAAdapter adapter = (SimpleFAtoNFAAdapter) nfa;
        SimpleFA fa = adapter.getSimpleFA();
        InputConvertor inputConvertor = adapter.getInputConvertor();
        
        // NFA(v1) --> DFA(v1)
        SimpleFA dfa = fa.toDFA();
        // DFA(v1) --> DFA(v3)
        DFA got = new SimpleFAtoNFAAdapter(dfa, inputConvertor);        
        DFA expected = TestHelper.buildDFA(_dfa);
        
        assertNotNull(got);
        assertNotNull(expected);
        assertEquals("" + Helper.dumpString(expected), "" + Helper.dumpString(got));
    }
}
