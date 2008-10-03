package mini.java.regex.legacy;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import mini.java.fa.State;
import mini.java.fa.legacy.v1.SimpleFA;

import org.junit.Ignore;
import org.junit.Test;

public class SimpleFATest {
    @Test public void testAddTransitionBasic() {
        SimpleFA fa = new SimpleFA();
        State frm = fa.createState();
        State to = fa.createState();
        Set input = new HashSet();
        input.add('a');
        input.add('b');
        input.add('c');
        fa.addTransition(frm, to, input);

        //exactly the same input
        Set<State> s;
        s = fa.move(frm, input);
        assertTrue(s.size() == 1);
        assertTrue(s.contains(to));

        //a subset of the input
        Set input0 = new HashSet();
        input0.add('a');
        input0.add('b');
        s = fa.move(frm, input0);
        assertTrue(s.size() == 1);
        assertTrue(s.contains(to));

        //different input
        Set input1 = new HashSet();
        input1.add('a');
        input1.add('d');
        s = fa.move(frm, input1);
        assertTrue(s.isEmpty());
    }

    @Test public void testAddTransitionTwice() {
        SimpleFA fa = new SimpleFA();
        State frm = fa.createState();
        State to = fa.createState();
        Set input = new HashSet();
        input.add('a');
        input.add('b');
        input.add('c');
        Set input0 = new HashSet();
        input.add('a');
        input.add('d');
        fa.addTransition(frm, to, input);
        fa.addTransition(frm, to, input0);

        Set<State> s;
        s = fa.move(frm, input0);
        assertTrue(s.size() == 1);
        assertTrue(s.contains(to));

        s = fa.move(frm, input);
        assertTrue(s.size() == 1);
        assertTrue(s.contains(to));

        Set input1 = new HashSet();
        input1.add('a');
        input1.add('b');
        input1.add('c');
        input1.add('d');
        s = fa.move(frm, input);
        assertTrue(s.size() == 1);
        assertTrue(s.contains(to));

        Set input2 = new HashSet();
        input2.add('d');
        s = fa.move(frm, input);
        assertTrue(s.size() == 1);
        assertTrue(s.contains(to));
    }

    @Test public void testAddTransitionTwice0() {
        SimpleFA fa = new SimpleFA();
        State frm = fa.createState();
        State to = fa.createState();
        State to0 = fa.createState();
        Set input = new HashSet();
        input.add('a');
        input.add('b');
        input.add('c');
        Set input0 = new HashSet();
        input0.add('a');
        input0.add('d');
        fa.addTransition(frm, to, input);
        fa.addTransition(frm, to0, input0);

        Set<State> s;
        s = fa.move(frm, input);
        //System.out.println("TO: " + s);
        assertTrue(s.size() == 1);
        assertTrue(s.contains(to));

        s = fa.move(frm, input0);
        //System.out.println("TO0: " + s);
        assertTrue(s.size() == 1);
        assertTrue(s.contains(to0));

        Set input1 = new HashSet();
        input1.add('a');
        s = fa.move(frm, input1);
        //System.out.println("BOTH: " + s);
        assertTrue(s.size() == 2);
        assertTrue(s.contains(to));
        assertTrue(s.contains(to0));
    }

    @Ignore
    @Test public void testEClosureBasic() {
        SimpleFA fa = new SimpleFA();
        State frm = fa.createState();
        State to = fa.createState();
        Set input = new HashSet();
        input.add('a');
        input.add('b');
        fa.addTransition(frm, to, input);
        fa.addTransition(frm, to);
        Set<State> s = fa.e_closure(frm);
        //System.out.println(s);
        assertTrue(s.size() == 2);
        assertTrue(s.contains(to));
        assertTrue(s.contains(frm));
    }

    @Ignore
    @Test public void testEClosureMore() {
        SimpleFA fa = new SimpleFA();
        State frm = fa.createState();
        State to = fa.createState();
        State to0 = fa.createState();
        State to1 = fa.createState();
        fa.addTransition(frm, to);
        fa.addTransition(to, to0);
        fa.addTransition(to0, to1);

        Set<State> s = fa.e_closure(frm);
        assertTrue(s.size() == 4);
        assertTrue(s.contains(frm));
        assertTrue(s.contains(to));
        assertTrue(s.contains(to0));
        assertTrue(s.contains(to1));
    }

    @Test public void testEClosureMutual() {
        SimpleFA fa = new SimpleFA();
        State frm = fa.createState();
        State to = fa.createState();
        fa.addTransition(frm, to);
        fa.addTransition(to, frm);

        Set<State> s = fa.e_closure(frm);
        assertTrue(s.size() == 2);
        assertTrue(s.contains(to));
        assertTrue(s.contains(frm));
    }

    @Ignore
    @Test public void testEClosures() {
        SimpleFA fa = new SimpleFA();
        State frm = fa.createState();
        State to = fa.createState();
        State to0 = fa.createState();
        State to1 = fa.createState();
        fa.addTransition(frm, to);
        fa.addTransition(to, to0);
        fa.addTransition(to0, to1);

        Set<State> s = new HashSet<State>();
        s.add(to);
        s.add(to0);
        s.add(to1);
        s.add(frm);
        Set<State> s0 = fa.e_closure(s);
        assertTrue(s0.size() == 4);
        assertTrue(s0.contains(frm));
        assertTrue(s0.contains(to));
        assertTrue(s0.contains(to0));
        assertTrue(s0.contains(to1));
    }

    @Test public void testPossibleInputs() {
        SimpleFA fa = new SimpleFA();
        State s = fa.createState();
        State s0 = fa.createState();
        State s1 = fa.createState();
        Set input = new HashSet();
        input.add('a');
        input.add('c');
        Set input0 = new HashSet();
        input0.add('b');
        input0.add('a');
        Set input1 = new HashSet();
        input1.add('e');
        fa.addTransition(s, s0, input);
        fa.addTransition(s, s0, input0);
        fa.addTransition(s, s1, input1);
        Set inputs = fa.getInputMixed(s);
        assertTrue(inputs.size() == 4);
        assertTrue(inputs.contains('a'));
        assertTrue(inputs.contains('c'));
        assertTrue(inputs.contains('b'));
        assertFalse(inputs.contains('d'));
        assertTrue(inputs.contains('e'));
    }

    //Bug1: ".*" should accept empty string;
    //@Test void testBug1 {
    //}
}
