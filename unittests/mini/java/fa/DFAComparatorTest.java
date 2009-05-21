package mini.java.fa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collection;

import mini.java.fa.v3.DFA;
import mini.java.fa.v3.helper.DFAComparator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DFAComparatorTest {
    private DFA _dfaA;
    private DFA _dfaB;
    private boolean _result;
//    private DFAComparator _comparator;
    
    public DFAComparatorTest(String dfaA_, String dfaB_, boolean result_) {
        assert(dfaA_ != null);
        assert(dfaB_ != null);
        
        _dfaA = TestHelper.buildDFA(dfaA_);
        _dfaB = TestHelper.buildDFA(dfaB_);
        _result = result_;
        
        assertNotNull(_dfaA);
        assertNotNull(_dfaB);
    }
    
    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
                {"ABa",         "CDa",          true}, // single transition
                {"ABb,ACc",     "CDb,CEc",      true}, // multiple transitions
                {"ABa,BCb",     "CDa,DEb",      true},
                {"abA,acB",     "abA,acB",      true},
                {"ABa,BAa",     "CDa,DCa",      true}, // loop
                {"ABa,BAa",     "ABa,BCb",      false},
                {"ABa",         "ABa,ABb",      false}, // different structure
                {"ABb",         "ACc",          false}, // different inputs
                {"DFd,BEd,ACd,ECa,ACa", "EFd",   true},
        });
    }
    
//    @Before
//    public void setUp() {
//        _comparator = new DFAComparator(_dfaA, _dfaB);
//        assertNotNull(_comparator);
//    }
//    
//    @After
//    public void tearDown() {
//        _comparator = null;
//    }
    
    @Test
    public final void testCompare() {
        assertEquals(_result, new DFAComparator(_dfaA, _dfaB).compare());
    }
}

