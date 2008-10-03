package mini.java.fa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DFAComparatorTest {
    
    @Test
    public final void testCompare() {
        String rep = "abA,acB";
        DFA sourceDFA = TestHelper.buildDFA(rep);
        DFA targetDFA = TestHelper.buildDFA(rep);
        assertNotNull(sourceDFA);
        assertNotNull(targetDFA);
        assertTrue(new DFAComparator(sourceDFA, targetDFA).compare());
    }    

    @Test
    public final void testCompareSingleTransition() {
        String Rep = "ABa";
        DFA sourceDFA = TestHelper.buildDFA(Rep);
        DFA targetDFA = TestHelper.buildDFA(Rep);
        assertTrue(new DFAComparator(sourceDFA, targetDFA).compare());
    }
    
    @Test
    public final void testCompareMultipleTransitionsDifferentInputs() {
        String Rep = "ABb,ACc";
        DFA sourceDFA = TestHelper.buildDFA(Rep);
        DFA targetDFA = TestHelper.buildDFA(Rep);
        assertTrue(new DFAComparator(sourceDFA, targetDFA).compare());
    }
    
    @Test
    public final void testCompareMultipleTransitionsDifferentTransitions() {
        String Rep = "ABa,BCb";
        DFA sourceDFA = TestHelper.buildDFA(Rep);
        DFA targetDFA = TestHelper.buildDFA(Rep);
        assertTrue(new DFAComparator(sourceDFA, targetDFA).compare());
    }
    
    @Test
    public final void testCompareFailedDifferentStructure() {
        DFA sourceDFA = TestHelper.buildDFA("ABa");
        DFA targetDFA = TestHelper.buildDFA("ABa,ABb");
        assertFalse(new DFAComparator(sourceDFA, targetDFA).compare());
    }
    
    @Test
    public final void testCompareFailedDifferentInputs() {
        DFA sourceDFA = TestHelper.buildDFA("ABb");
        DFA targetDFA = TestHelper.buildDFA("ACc");
        assertFalse(new DFAComparator(sourceDFA, targetDFA).compare());
    }
    
    @Test
    public final void testCompareLoop() {
        String Rep = "ABa,BAa";
        DFA sourceDFA = TestHelper.buildDFA(Rep);
        DFA targetDFA = TestHelper.buildDFA(Rep);
        assertTrue(new DFAComparator(sourceDFA, targetDFA).compare());
    }
    
    @Test
    public final void testCompareLoopFailed() {
        DFA sourceDFA = TestHelper.buildDFA("ABa,BAa");
        DFA targetDFA = TestHelper.buildDFA("ABa,BCb");
        assertFalse(new DFAComparator(sourceDFA, targetDFA).compare());
    }
    
//    @Test
//    public final void compareBug01() {
//        DFA sourceDFA = Helper.buildDFA("DFd,BEd,ACd,ECa,ACa");
//        DFA targetDFA = Helper.buildDFA("EFd");
//        assertFalse(new Helper.Comparator(sourceDFA, targetDFA).compare());
//    }
}

