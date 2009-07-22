package mini.java.regex;


import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.List;

import mini.java.fa.AcceptableNFAState;
import mini.java.fa.NFAState;
import mini.java.fa.helper.Helper;
import mini.java.lex.Tokenizer;
import mini.java.syntax.NonTerminal;
import mini.java.syntax.Parser;
import mini.java.syntax.Symbol;
import mini.java.syntax.Terminal;

import org.junit.Test;


public class RegexCompilerTest {

    private static final Parser parser = new Parser(RegexCompiler.RULE_SET);
    private static final Tokenizer tokenizer = RegexCompiler.TOKENIZER;
    
    @Test
    public void testTokenizer() {
        Tokenizer target = RegexCompiler.TOKENIZER;
        {
            List<Terminal> tokens = new LinkedList<Terminal>();
            tokens.add(new Terminal(RegexCompiler.NUM, "1"));
            tokens.add(new Terminal(RegexCompiler.NUM, "2"));
            tokens.add(new Terminal(RegexCompiler.NUM, "3"));
            tokens.add(new Terminal(RegexCompiler.LOWER, "a"));
            tokens.add(new Terminal(RegexCompiler.UPPER, "A"));
            tokens.add(new Terminal(RegexCompiler.STAR, "*"));
            tokens.add(new Terminal(RegexCompiler.DOT, "."));
            tokens.add(new Terminal(RegexCompiler.QM, "?"));
            tokens.add(new Terminal(RegexCompiler.LP, "("));
            tokens.add(new Terminal(RegexCompiler.RP, ")"));
            tokens.add(new Terminal(RegexCompiler.LB, "["));
            tokens.add(new Terminal(RegexCompiler.RB, "]"));
            tokens.add(new Terminal(RegexCompiler.BAR, "|"));
            tokens.add(new Terminal(RegexCompiler.HYPHEN, "-"));
            tokens.add(new Terminal(RegexCompiler.CARET, "^"));
            tokens.add(new Terminal(RegexCompiler.CH, "\\*"));
            tokens.add(new Terminal(RegexCompiler.CH, "\\"));
            
            assertArrayEquals(
                    tokens.toArray(new Terminal[0]),
                    target.tokenize("123aA*.?()[]|-^\\*\\"));
        }
    }
    
    
    @Test
    public void testSyntax() {
        NonTerminal root = (NonTerminal)parser.parse(RegexCompiler.TOKENIZER.tokenize("a"));
        assertEquals("START(BarExpr(SeqExpr(Atom(lower))))", root.toString());
        
        
        String lowerRange = "ClassExpr(Range(lower,hyphen,lower))";
        String numRange = "ClassExpr(Range(num,hyphen,num))";
        __testSyntax("[a-b1-2]",
                "START(BarExpr(SeqExpr(Atom(lb,ClassExpr("
                + lowerRange + "," + numRange + "),rb))))");
        
        __testSyntax("[aa-b]",
                "START(BarExpr(SeqExpr(Atom(lb,ClassExpr(ClassExpr(lower)," + lowerRange + "),rb))))");
        __testSyntax("[*]",
                "START(BarExpr(SeqExpr(Atom(lb,ClassExpr(star),rb))))");
        __testSyntax("[\\[]", "START(BarExpr(SeqExpr(Atom(lb,ClassExpr(ch),rb))))");
        __testSyntax("[^a]", "START(BarExpr(SeqExpr(Atom(lb,caret,ClassExpr(lower),rb))))");

        
        __testIllegalSyntax("[[]]");
        __testIllegalSyntax("a?*");
        __testIllegalSyntax("a*?");
        __testIllegalSyntax("[A-z]");
        __testIllegalSyntax("[a^b]");
    }
    
    @Test
    public void testPrecedence() {
        Tokenizer tokenizer = RegexCompiler.TOKENIZER;
        String lower = "SeqExpr(Atom(lower))";
        {
            NonTerminal root = (NonTerminal)parser.parse(tokenizer.tokenize("aa|a"));
            assertEquals(
                    "START(BarExpr("
                    + "BarExpr(SeqExpr(" + lower + "," + lower + ")),bar,BarExpr(" + lower + ")))", root.toString());
        }
        
        {
            NonTerminal root = (NonTerminal)parser.parse(tokenizer.tokenize("a|aa"));
            assertEquals(
                    "START(BarExpr("
                    + "BarExpr(" + lower + "),bar,BarExpr(SeqExpr(" + lower + "," + lower + "))))", root.toString());
        }
        
        {
            NonTerminal root = (NonTerminal)parser.parse(tokenizer.tokenize("aa*"));
            assertEquals(
                    "START(BarExpr(SeqExpr(" + lower + ",SeqExpr(StarExpr(Atom(lower),star)))))", root.toString());
        }
    }
    
    
    @Test
    public void testRegexCh() {
        __testRegex("a", "0 =>(a) 1\n");
    }
    
    @Test
    public void testRegexBar() {
        __testRegex("a|b",
                "0 =>(a) 1\n" 
              + "0 =>(b) 1\n");
    }
    
    @Test
    public void testRegexSeq() {
        __testRegex("ab",
                "0 =>(a) 1\n" +
                "1 =>(b) 2\n");
    }
    
    @Test
    public void testRegexClass() {
        __testRegex("[a-b1-1*]",
                "0 =>(*) 1\n" +
                "0 =>(1) 1\n" +
                "0 =>(a) 1\n" +
                "0 =>(b) 1\n");
        __testRegex("[b-a2-1]",
                "0 =>(1) 1\n" +
                "0 =>(2) 1\n" +
                "0 =>(a) 1\n" +
                "0 =>(b) 1\n");
        
        __testRegex("[\\*]", "0 =>(*) 1\n");
                
    }
    

    
    
    @Test
    public void testRegexStar() {
        NonTerminal root = (NonTerminal)parser.parse(tokenizer.tokenize("a*"));
        NFAState head = new NFAState(),
            tail = new AcceptableNFAState(); 
        root.execute(new NFAState[] {head, tail});
        
        NFAState dfa = Helper.collapse(head);
//        assertEquals(
//                "0 =>(a) 1\n" +
//                "1 =>(a) 1\n", Helper.dump(dfa));
        assertEquals("0 =>(a) 0\n", Helper.dump(dfa));
        assertEquals(AcceptableNFAState.class, dfa.getClass());
        assertNotNull(dfa.getState("a"));
        assertEquals(AcceptableNFAState.class, dfa.getState("a").getClass());
        
    }
    
    
    @Test
    public void testRegexQM() {
        NonTerminal root = (NonTerminal)parser.parse(tokenizer.tokenize("a?"));
        NFAState head = new NFAState(),
            tail = new AcceptableNFAState(); 
        root.execute(new NFAState[] {head, tail});
        
        NFAState dfa = Helper.collapse(head);
        assertEquals(
                "0 =>(a) 1\n", Helper.dump(dfa));
        assertEquals(AcceptableNFAState.class, dfa.getClass());
        assertNotNull(dfa.getState("a"));
        assertEquals(AcceptableNFAState.class, dfa.getState("a").getClass());
        
    }
    
    @Test
    public void testRegexParenthesis() {
        __testRegex("(((a|b)ab)*)?a",
                "0 =>(a) 1\n" +
                "0 =>(b) 2\n" +
                "1 =>(a) 3\n" +
                "2 =>(a) 3\n" +
                "3 =>(b) 0\n");
    }
    
    @Test
    public void testRegexDot() {
        StringBuilder sb = new StringBuilder();
        for (char ch=0; ch<128; ++ch) {
            sb.append("0 =>(" + ch + ") 1\n");
        }
        __testRegex("a|.", sb.toString());
    }
    
    @Test
    public void testRegexCaret() {
        StringBuilder sb = new StringBuilder();
        for (char ch=RegexCompiler.CH_MIN; ch <= RegexCompiler.CH_MAX; ++ch) {
            if ((ch >= 'a' && ch <= 'z')
                    || (ch >= 'A' && ch <= 'Z')
                    || (ch >= '0' && ch <= '9')
                    || (ch == '_'))
            {
              // continue  
            }
            else
            {
              sb.append("0 =>(" + ch + ") 1\n");
            }
        }
        __testRegex("[^0-9A-Za-z_]", sb.toString());
    }
    
    @Test
    public void testBug1() {
        String regex = "(a|aa)b";
        Symbol root = parser.parse(tokenizer.tokenize(regex));
        assertEquals("START(BarExpr(SeqExpr(SeqExpr(Atom(lp," + 
                "BarExpr(BarExpr(SeqExpr(Atom(lower))),bar," +
                    "BarExpr(SeqExpr(SeqExpr(Atom(lower)),SeqExpr(Atom(lower))))),rp))," +
                    "SeqExpr(Atom(lower)))))", root.toString());
        
        NFAState head = new NFAState(),
            tail = new AcceptableNFAState(); 
        ((NonTerminal)root).execute(new NFAState[] {head, tail});
        assertEquals(
                "0 =>(a) 1\n" +
                "1 =>(a) 2\n" +
                "1 =>(b) 3\n" +
                "2 =>(b) 3\n", Helper.dump(Helper.collapse(head)));
    }
    
    @Test
    public void testBug2() {
        __testRegex("-",
                "0 =>(-) 1\n");
    }
    
    @Test
    public void testBug3() {
        __testRegex("\\.", "0 =>(.) 1\n");
    }
    
    
    
    private static void __testSyntax(String input_, String expected_) {
        NonTerminal root = (NonTerminal)parser.parse(RegexCompiler.TOKENIZER.tokenize(input_));
        assertEquals(expected_, root.toString());
    }
    
    private static void __testIllegalSyntax(String input_) {
        try
        {
            parser.parse(tokenizer.tokenize(input_));
            fail("Illegal syntax cannot be detected: " + input_);
        }
        catch (RuntimeException ex_)
        {
            //
        }
    }
    
    private static void __testRegex(String input_, String expected_) {
        NonTerminal root = (NonTerminal)parser.parse(RegexCompiler.TOKENIZER.tokenize(input_));
        NFAState head = new NFAState(),
            tail = new AcceptableNFAState(); 
        root.execute(new NFAState[] {head, tail});
        
        NFAState dfa = Helper.collapse(head);
        assertEquals(expected_, Helper.dump(dfa));
    }
}
