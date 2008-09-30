package mini.java.fa;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DFAComparatorTest {
    
    @Test
    public final void testCompare() {
        String rep = "abA,acB";
        DFA sourceDFA = Helper.buildDFA(rep);
        DFA targetDFA = Helper.buildDFA(rep);
        assertNotNull(sourceDFA);
        assertNotNull(targetDFA);
        assertTrue(new DFAComparator(sourceDFA, targetDFA).compare());
    }    

    @Test
    public final void testCompareSingleTransition() {
        String Rep = "ABa";
        DFA sourceDFA = Helper.buildDFA(Rep);
        DFA targetDFA = Helper.buildDFA(Rep);
        assertTrue(new DFAComparator(sourceDFA, targetDFA).compare());
    }
    
    @Test
    public final void testCompareMultipleTransitionsDifferentInputs() {
        String Rep = "ABb,ACc";
        DFA sourceDFA = Helper.buildDFA(Rep);
        DFA targetDFA = Helper.buildDFA(Rep);
        assertTrue(new DFAComparator(sourceDFA, targetDFA).compare());
    }
    
    @Test
    public final void testCompareMultipleTransitionsDifferentTransitions() {
        String Rep = "ABa,BCb";
        DFA sourceDFA = Helper.buildDFA(Rep);
        DFA targetDFA = Helper.buildDFA(Rep);
        assertTrue(new DFAComparator(sourceDFA, targetDFA).compare());
    }
    
    @Test
    public final void testCompareFailedDifferentStructure() {
        DFA sourceDFA = Helper.buildDFA("ABa");
        DFA targetDFA = Helper.buildDFA("ABa,ABb");
        assertFalse(new DFAComparator(sourceDFA, targetDFA).compare());
    }
    
    @Test
    public final void testCompareFailedDifferentInputs() {
        DFA sourceDFA = Helper.buildDFA("ABb");
        DFA targetDFA = Helper.buildDFA("ACc");
        assertFalse(new DFAComparator(sourceDFA, targetDFA).compare());
    }
    
    @Test
    public final void testCompareLoop() {
        String Rep = "ABa,BAa";
        DFA sourceDFA = Helper.buildDFA(Rep);
        DFA targetDFA = Helper.buildDFA(Rep);
        assertTrue(new DFAComparator(sourceDFA, targetDFA).compare());
    }
    
    @Test
    public final void testCompareLoopFailed() {
        DFA sourceDFA = Helper.buildDFA("ABa,BAa");
        DFA targetDFA = Helper.buildDFA("ABa,BCb");
        assertFalse(new DFAComparator(sourceDFA, targetDFA).compare());
    }
    
//    @Test
//    public final void compareBug01() {
//        DFA sourceDFA = Helper.buildDFA("DFd,BEd,ACd,ECa,ACa");
//        DFA targetDFA = Helper.buildDFA("EFd");
//        assertFalse(new Helper.Comparator(sourceDFA, targetDFA).compare());
//    }
}

