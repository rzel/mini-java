package mini.java.fa;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class DFAComparatorTestParameterized {
//    private static int id = 0; // indicator for stdout dumps
    private static int MAXCOUNT = 10;
    private static int MAXLENGTH = 20;
    private static char[] states = {'A','B','C','D','E','F'};
    private static char[] inputs = {'a','b','c','d'};
    private String _repA;
    private String _repB;

    public DFAComparatorTestParameterized(String repA_, String repB_) {
        assertNotNull(repA_); // avoid error
        assertNotNull(repB_);
        assertNotSame(repA_, repB_);
        _repA = repA_;
        _repB = repB_;
        //++id;
    }
    
    @Parameters
    public static Collection<Object[]> getParameters() {
        List<Object[]> list = new LinkedList<Object[]>();
        // there should be at least one string representation
        for (int i = 0; i < MAXCOUNT; ++i) {
            // the possibility of two representions being equal is extremely low
            String repA = createRep();
            String repB = createRep();
            list.add(new Object[] {repA, repB});
            
//            // dump the randomly generated string representations to stdout
//            System.out.println("[" + i + "]repA = " + repA);
//            System.out.println("[" + i + "]repB = " + repB);     
            
            // dump the randomly generated string representation to stdout
            System.out.println("[" + i + "]_rep = " + repA);
        }
        
        return list;
    }
    
    // helper function used to create string representations
    private final static String createRep() {
        Random rnd = new Random();
        StringBuilder rep = new StringBuilder();
        //List<String> list = new LinkedList<String>();
        // there should be at least one transition
        for (int i = 0, n = rnd.nextInt(MAXLENGTH)+1; i < n; ++i) {
            Character source = states[rnd.nextInt(states.length)];
            Character target = states[rnd.nextInt(states.length)];
            Character input = inputs[rnd.nextInt(inputs.length)];
            rep.append(source);
            rep.append(target);
            rep.append(input);
            rep.append(',');
        }
        return rep.substring(0, rep.length()-1).toString(); // trim the trailing comma
    }
    
    @Test
    public final void testCompare() {
//        // dump the randomly generated string representation to stdout
//        System.out.println("[" + id + "]_rep = " + _repA);
        
        DFA sourceDFA = TestHelper.buildDFA(_repA);
        DFA targetDFA = TestHelper.buildDFA(_repA);
        assertNotNull(sourceDFA);
        assertNotNull(targetDFA);
        assertTrue(new DFAComparator(sourceDFA, targetDFA).compare());
    }
    
//    @Test
//    public final void testCompareFailed() {
////        // dump the randomly generated string representations to stdout
////        System.out.println("[" + id + "]_repA = " + _repA);
////        System.out.println("[" + id + "]_repB = " + _repB);        
////        ++id;
//        
//        DFA sourceDFA = Helper.buildDFA(_repA);
//        DFA targetDFA = Helper.buildDFA(_repB);
//        assertNotNull(sourceDFA);
//        assertNotNull(targetDFA);
//        assertFalse(new Helper.Comparator(sourceDFA, targetDFA).compare());
//    }
}
