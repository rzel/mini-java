package mini.java.fa;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import mini.java.TestHelper;
import mini.java.fa.v3.DFA;
import mini.java.fa.v3.helper.NFAConvertor;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class NFAConvertorTest extends TestHelper {
    private String _nfa; // for NFA
    private String _dfa; // for DFA

    public NFAConvertorTest(String nfa_, String dfa_) {
        assert (nfa_ != null);
        assert (dfa_ != null);
        _nfa = nfa_;
        _dfa = dfa_;
    }
    
    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
                { "ABa",        "ABa" },
                { "ABa,BCb",    "ABa,BCb" },
                { "ABb,ACc",    "ABb,ACc" },
                { "ABa,BC",     "ABa" },
                { "AB,BCa",     "ABa" },
                { "ABa,BC,BD",  "ABa" },
                { "AB,BC,BDa",  "ABa" },
                { "ABa,BA,AC,CDb,CDc", "ADb,ADc,ACa,CCa,CDb,CDc" }, // a*(b|c)
                { "ABa,BCa,BCb,CB,BD,ADc", "ABa,BEa,BEb,EEa,EEb,ADc" }, // (a(a|b)*)|c
        });
    }
    
    @Test
    public final void testBuildDFA() {
        DFA got = NFAConvertor.convert(buildNFA(_nfa));
        DFA expected = buildDFA(_dfa);
        
        assertNotNull(got);
        assertNotNull(expected);
        assertTrue(Helper.compare(got, expected));
    }
}
