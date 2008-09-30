package mini.java.fa;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class NFABuildDFATest {
    private String _repA; // for NFA
    private String _repB; // for DFA

    public NFABuildDFATest(String repA_, String repB_) {
        assert (repA_ != null);
        assert (repB_ != null);
        _repA = repA_;
        _repB = repB_;
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
        DFA got = Helper.buildNFA(_repA).buildDFA();
        DFA expected = Helper.buildDFA(_repB);
        
        assertNotNull(got);
        assertNotNull(expected);
        assertTrue(new DFAComparator(got, expected).compare());
    }
}
