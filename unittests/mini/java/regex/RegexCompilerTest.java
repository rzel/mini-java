package mini.java.regex;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import mini.java.fa.AcceptableNFAState;
import mini.java.fa.NFAState;
import mini.java.fa.helper.Helper;
import mini.java.lex.IMatcher;
import mini.java.lex.ITokenizer;
import mini.java.lex.Tokenizer;
import mini.java.syntax.NonTerminal;
import mini.java.syntax.Parser;

import org.junit.Test;


public class RegexCompilerTest {
    // the target being tested
//    private static final RegexConfig conf = RegexConfig._instance;    
    private static final Parser parser = new Parser(RegexCompiler.RULE_SET);
//    private static final ITokenizer tokenizer = new Tokenizer(conf); 
//
//
//    @Test
//    public void testNumMatcher() {
//        IMatcher numMatcher = conf.getMatcher(RegexConfig.NUM);
//        
//        assertEquals("1",numMatcher.match("1"));
//        assertEquals("1",numMatcher.match("1*"));
//        assertNull(numMatcher.match("abc"));
//        assertNull(numMatcher.match("\\1")); // shouldn't match escaped
//        assertNull(numMatcher.match(""));
//    }
//    
//    @Test
//    public void testChMatcher() {
//        IMatcher chMatcher = conf.getMatcher(RegexConfig.CH);
//        
//        assertEquals("a",chMatcher.match("abc"));
//        assertEquals("a",chMatcher.match("\\abc")); //backslash should be removed
//        assertEquals("[",chMatcher.match("\\[["));
//        assertEquals("\\",chMatcher.match("\\"));
//        
//        assertNull(chMatcher.match(""));
//    }
    
    
    @Test
    public void testSyntax() {
        NonTerminal root = (NonTerminal)parser.parse(RegexCompiler.TOKENIZER.tokenize("a"));
        assertEquals("START(BarExpr(SeqExpr(Atom(alpha))))", root.toString());
        
        
        String alphaRange = "ClassExpr(Range(alpha,hyphen,alpha))";
        String numRange = "ClassExpr(Range(num,hyphen,num))";
        __testSyntax("[a-b1-2]",
                "START(BarExpr(SeqExpr(Atom(lb,ClassExpr("
                + alphaRange + "," + numRange + "),rb))))");
        
        __testSyntax("[aa-b]",
                "START(BarExpr(SeqExpr(Atom(lb,ClassExpr(ClassExpr(alpha)," + alphaRange + "),rb))))");
        __testSyntax("[*]",
                "START(BarExpr(SeqExpr(Atom(lb,ClassExpr(star),rb))))");
//        __testSyntax("[\\[]", "START(BarExpr(SeqExpr(Atom(lb,ClassExpr(ch),rb))))");
    }
    
    @Test
    public void testPrecedence() {
        Tokenizer tokenizer = RegexCompiler.TOKENIZER;
        String alpha = "SeqExpr(Atom(alpha))";
        {
            NonTerminal root = (NonTerminal)parser.parse(tokenizer.tokenize("aa|a"));
            assertEquals(
                    "START(BarExpr("
                    + "BarExpr(SeqExpr(" + alpha + "," + alpha + ")),bar,BarExpr(" + alpha + ")))", root.toString());
        }
        
        {
            NonTerminal root = (NonTerminal)parser.parse(tokenizer.tokenize("a|aa"));
            assertEquals(
                    "START(BarExpr("
                    + "BarExpr(" + alpha + "),bar,BarExpr(SeqExpr(" + alpha + "," + alpha + "))))", root.toString());
        }
        
        {
            NonTerminal root = (NonTerminal)parser.parse(tokenizer.tokenize("aa*"));
            assertEquals(
                    "START(BarExpr(SeqExpr(" + alpha + ",SeqExpr(StarExpr(Atom(alpha),star)))))", root.toString());
        }
    }
    
    
    @Test
    public void testRegexCh() {
        NonTerminal root = (NonTerminal)parser.parse(RegexCompiler.TOKENIZER.tokenize("a"));
        NFAState head = new NFAState(),
            tail = new AcceptableNFAState(); 
        root.execute(new RegexContext(head, tail));
        
        assertEquals("0 =>(a) 1\n", Helper.dump(head));
    }
    
    @Test
    public void testRegexBar() {
        __testRegex("a|b",
                "0 =>(a) 1\n" 
              + "0 =>(b) 2\n");
    }
    
    
    private static void __testSyntax(String input_, String expected_) {
        NonTerminal root = (NonTerminal)parser.parse(RegexCompiler.TOKENIZER.tokenize(input_));
        assertEquals(expected_, root.toString());
    }
    
    private static void __testRegex(String input_, String expected_) {
        NonTerminal root = (NonTerminal)parser.parse(RegexCompiler.TOKENIZER.tokenize(input_));
        NFAState head = new NFAState(),
            tail = new AcceptableNFAState(); 
        root.execute(new RegexContext(head, tail));
        
        NFAState dfa = Helper.collapse(head);
        assertEquals(expected_, Helper.dump(dfa));
    }
}
