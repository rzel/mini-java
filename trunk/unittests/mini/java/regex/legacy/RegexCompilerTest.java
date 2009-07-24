package mini.java.regex.legacy;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mini.java.fa.v1.SimpleFA;
import mini.java.fa.v1.State;

import org.junit.Test;

public class RegexCompilerTest {
    // a single token
    @Test
    public void testTokenizeBasic() {
        try {
            List<RegexToken> list = RegexCompiler.tokenize("*");
            assertTrue(list.size() == 1);
            assertEquals(list.get(0), new RegexToken(RegexToken.Type.STAR));
        } catch (Exception ex) {
            fail();
        }
    }

    // more complicated test
    @Test
    public void testTokenizeBasic0() {
        RegexToken[] result, expected;
        try {
            result = RegexCompiler.tokenize("(a|b)*").toArray(new RegexToken[0]);
            Set<Character> in = new HashSet<Character>();
            in.add('a');
            Set<Character> in0 = new HashSet<Character>();
            in0.add('b');
            expected = new RegexToken[] { new RegexToken(RegexToken.Type.LP), new RegexToken(RegexToken.Type.CHAR, in),
                    new RegexToken(RegexToken.Type.BAR), new RegexToken(RegexToken.Type.CHAR, in0),
                    new RegexToken(RegexToken.Type.RP), new RegexToken(RegexToken.Type.STAR) };
            assertTrue(result.length == 6);
            assertArrayEquals(result, expected);
        } catch (Exception ex) {
            fail();
        }
    }

    // escaped characters
    @Test
    public void testTokenizeEscaped() throws Exception {
        String input = "\\r\\n\\s\\t  \\\\\\]\\)\\a";
        List<RegexToken> result = RegexCompiler.tokenize(input);
        assertTrue(result.size() == 10);

        RegexToken token;
        token = result.get(0);
        assertTrue(token.type == RegexToken.Type.CHAR);
        assertTrue((__convert(token.payload)).contains('\r'));

        token = result.get(1);
        assertTrue(token.type == RegexToken.Type.CHAR);
        assertTrue((__convert(token.payload)).contains('\n'));

        token = result.get(2);
        assertTrue(token.type == RegexToken.Type.CHAR);
        assertTrue((__convert(token.payload)).contains('\r'));
        assertTrue((__convert(token.payload)).contains('\n'));
        assertTrue((__convert(token.payload)).contains('\t'));
        assertTrue((__convert(token.payload)).contains(' '));

        token = result.get(3);
        assertTrue(token.type == RegexToken.Type.CHAR);
        assertTrue((__convert(token.payload)).contains('\t'));

        token = result.get(4);
        assertTrue(token.type == RegexToken.Type.CHAR);
        assertTrue((__convert(token.payload)).contains(' '));

        token = result.get(5);
        assertTrue(token.type == RegexToken.Type.CHAR);
        assertTrue((__convert(token.payload)).contains(' '));

        token = result.get(6);
        assertTrue(token.type == RegexToken.Type.CHAR);
        assertTrue((__convert(token.payload)).contains('\\'));

        token = result.get(7);
        assertTrue(token.type == RegexToken.Type.CHAR);
        assertTrue((__convert(token.payload)).contains(']'));

        token = result.get(8);
        assertTrue(token.type == RegexToken.Type.CHAR);
        assertTrue((__convert(token.payload)).contains(')'));

        token = result.get(9);
        assertTrue(token.type == RegexToken.Type.CHAR);
        assertTrue((__convert(token.payload)).contains('a'));
    }


    @Test
    public void testTokenizeSetCaret() throws Exception {
        String input = "[^\\d\\w]";
        List<RegexToken> result = RegexCompiler.tokenize(input);
        assertTrue(result.size() == 1);
        RegexToken token = result.get(0);
        Set<Character> set = __convert(token.payload);
        assertTrue(set.contains(' '));
        assertTrue(set.contains('\n'));
        assertFalse(set.contains('a'));
        assertFalse(set.contains('Z'));
        assertFalse(set.contains('2'));
        assertFalse(set.contains('_'));
    }

    // character set with escaped characters inside
    @Test
    public void testTokenizeSetEscaped() throws Exception {
        String input = "[\\\\\\t\\sabc\\]]";
        List<RegexToken> result = RegexCompiler.tokenize(input);
        assertTrue(result.size() == 1);

        RegexToken token = result.get(0);
        Set<Character> input0 = new HashSet<Character>();
        input0.add('\\');
        input0.add('\t');
        input0.add('\n');
        input0.add('\r');
        input0.add(' ');
        input0.add('a');
        input0.add('b');
        input0.add('c');
        input0.add(']');
        RegexToken token0 = new RegexToken(RegexToken.Type.CHAR, input0);
        assertEquals(token, token0);
    }

    // extra left bracket (IndexOutOfBoundsException
    // --> Exception("illegal character set"))
    @Test(expected = Exception.class)
    public void testException0() throws Exception {
        RegexCompiler.tokenize("[az][");
    }

    // extra right bracket
    @Test(expected = Exception.class)
    public void testException1() throws Exception {
        RegexCompiler.tokenize("[az]]");
    }

    // illegal escaped character
    @Test(expected = Exception.class)
    public void testException7() throws Exception {
        RegexCompiler.tokenize("\\");
    }

    // empty character set
    @Test(expected = Exception.class)
    public void testException8() throws Exception {
        RegexCompiler.tokenize("[]");
    }


    // extra RP
    @Test(expected = Exception.class)
    public void testException10() throws Exception {
        RegexCompiler.compile("(a))");
    }

    // extra LP
    @Test(expected = Exception.class)
    public void testException11() throws Exception {
        RegexCompiler.compile("(a)(");
    }

    // Exception("empty set") --> throw ex
    @Test(expected = Exception.class)
    public void testException12() throws Exception {
        RegexCompiler.compile("([])");
    }

    // extra RP
    @Test(expected = Exception.class)
    public void testException13() throws Exception {
        RegexCompiler.compile(")");
    }

    // missing operator for star
    @Test(expected = Exception.class)
    public void testException14() throws Exception {
        RegexCompiler.compile("*");
    }

    // missing operator for qm
    @Test(expected = Exception.class)
    public void testException15() throws Exception {
        RegexCompiler.compile("?");
    }

    // missing operator for bar
    @Test(expected = IllegalArgumentException.class)
    public void testException16() throws Exception {
        RegexCompiler.compile("a|");
    }

    // missing operator for bar
    @Test(expected = Exception.class)
    public void testException17() throws Exception {
        RegexCompiler.compile("|a");
    }

    // missing operator for bar
    @Test(expected = Exception.class)
    public void testException18() throws Exception {
        RegexCompiler.compile("a||a");
    }

    @Test
    public void testExpr() {
        SimpleFA fa = new SimpleFA();
        Set<Character> input = new HashSet<Character>();
        input.add('a');
        input.add('b');
        RegexToken ch = new RegexToken(RegexToken.Type.CHAR, input);
        RegexToken expr = RegexCompiler.ch(ch, fa);

        Set<State> s = fa.move((State) ((List<?>) expr.payload).get(0), input);
        assertTrue(s.size() == 1);
        assertTrue(s.contains((State) ((List<?>) expr.payload).get(1)));

        Set<Character> input0 = new HashSet<Character>();
        input0.add('a');
        Set<State> s0 = fa.move((State) ((List<?>) expr.payload).get(0), input0);
        assertTrue(s0.size() == 1);
        assertTrue(s0.contains(((List<?>) expr.payload).get(1)));

        Set<Character> input1 = new HashSet<Character>();
        input1.add('c');
        Set<State> s1 = fa.move((State) ((List<?>) expr.payload).get(0), input1);
        assertTrue(s1.isEmpty());
    }


    @Test
    public void testStar() {
        SimpleFA fa = new SimpleFA();
        Set<Character> input = new HashSet<Character>();
        input.add('a');
        input.add('b');
        RegexToken ch = new RegexToken(RegexToken.Type.CHAR, input);
        RegexToken expr = RegexCompiler.ch(ch, fa);
        RegexToken sexpr = RegexCompiler.star(expr, fa);

        Set<State> s = fa.e_closure((State) ((List<?>) sexpr.payload).get(0));
        assertTrue(s.size() == 2);
        assertTrue(s.contains(((List<?>) sexpr.payload).get(1)));
        assertTrue(s.contains(((List<?>) sexpr.payload).get(0)));

        Set<State> s0 = fa.e_closure((State) ((List<?>) expr.payload).get(1));
        assertTrue(s0.size() == 3);
        assertTrue(s0.contains(((List<?>) expr.payload).get(1)));
        assertTrue(s0.contains(((List<?>) sexpr.payload).get(0)));
        assertTrue(s0.contains(((List<?>) sexpr.payload).get(1)));
    }

    
    @Test
    public void testQM() {
        SimpleFA fa = new SimpleFA();
        Set<Character> input = new HashSet<Character>();
        input.add('a');
        input.add('b');
        RegexToken ch = new RegexToken(RegexToken.Type.CHAR, input);
        RegexToken expr = RegexCompiler.ch(ch, fa);
        RegexToken sexpr = RegexCompiler.qm(expr, fa);

        Set<State> s = fa.e_closure((State) ((List<?>) sexpr.payload).get(0));
        assertTrue(s.size() == 2);
        assertTrue(s.contains(((List<?>) sexpr.payload).get(0)));
        assertTrue(s.contains(((List<?>) sexpr.payload).get(1)));
    }
    

    @Test
    public void testReduce(){
        SimpleFA fa = new SimpleFA();
        Set<Character> input = new HashSet<Character>();
        input.add('a');
        input.add('b');
        RegexToken ch = RegexCompiler.ch(new RegexToken(RegexToken.Type.CHAR, input), fa);

        Set<Character> input0 = new HashSet<Character>();
        input0.add('c');
        input0.add('d');
        RegexToken ch0 = RegexCompiler.ch(new RegexToken(RegexToken.Type.CHAR, input0), fa);

        List<RegexToken> exprs = new ArrayList<RegexToken>();
        exprs.add(RegexCompiler.star(ch, fa));
        exprs.add(new RegexToken(RegexToken.Type.BAR));
        exprs.add(RegexCompiler.qm(ch0, fa));
        
        // [ab]*|[cd]*
        RegexToken expr = RegexCompiler.__merge(exprs.toArray(new RegexToken[0]), fa);

        Set<State> s;
        s = fa.e_closure((State) ((List<?>) expr.payload).get(0));
        assertTrue(s.size() == 6);
        assertTrue(s.contains((State) ((List<?>) expr.payload).get(1)));
    }


    @Test
    public void testBug1() throws Exception {
        // BAR should have the lowest precedence
        SimpleFA fa = new SimpleFA();
        Set<Character> input = new HashSet<Character>();
        input.add('a');
        RegexToken ch = RegexCompiler.ch(new RegexToken(RegexToken.Type.CHAR, input), fa);
        RegexToken ch0 = RegexCompiler.ch(new RegexToken(RegexToken.Type.CHAR, input), fa);
        RegexToken ch1 = RegexCompiler.ch(new RegexToken(RegexToken.Type.CHAR, input), fa);


        List<RegexToken> exprs = new ArrayList<RegexToken>();
        exprs.add(ch);
        exprs.add(new RegexToken(RegexToken.Type.BAR));
        exprs.add(ch0);
        exprs.add(ch1);
        RegexToken expr = RegexCompiler.__merge(exprs.toArray(new RegexToken[0]), fa);


        Set<State> s = fa.e_closure((State) ((List<?>) expr.payload).get(0));
        Set<State> s0 = fa.move(s, input);
        Set<State> s1 = fa.e_closure(s0);

        assertTrue(s1.size() == 4);
        assertTrue(s1.contains((State) ((List<?>) expr.payload).get(1)));
    }
    
    
    @Test
    public void testBug2() {
        // Missing '^' in "[^a]"
        try {
            SimpleFA fa = RegexCompiler.compile("[^a]");
            
            Set<Character> input = fa.getInputMixed(fa.getInitialState());
            assertFalse(input.contains('a'));
            assertTrue(input.contains('^'));

        } catch (Exception ex_) {
            fail("Ex: " + ex_);
        }
    }
    
    
    
    private static Set<Character> __convert(Object obj_) {
        Set<Character> ret = new HashSet<Character>();
        for (Object obj : (Set<?>)obj_) {
            ret.add((Character)obj);
        }
        return ret;
    }
}
