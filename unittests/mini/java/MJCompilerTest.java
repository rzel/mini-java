package mini.java;

import static mini.java.MJCompiler.CLASS;
import static mini.java.MJCompiler.COMMENT;
import static mini.java.MJCompiler.ID;
import static mini.java.MJCompiler.LEFT_BRACE;
import static mini.java.MJCompiler.LEFT_BRACKET;
import static mini.java.MJCompiler.LP;
import static mini.java.MJCompiler.MAIN;
import static mini.java.MJCompiler.PARSER;
import static mini.java.MJCompiler.PUBLIC;
import static mini.java.MJCompiler.RIGHT_BRACE;
import static mini.java.MJCompiler.RIGHT_BRACKET;
import static mini.java.MJCompiler.RP;
import static mini.java.MJCompiler.STATIC;
import static mini.java.MJCompiler.STRING;
import static mini.java.MJCompiler.TOKENIZER;
import static mini.java.MJCompiler.VOID;
import static mini.java.MJCompiler.WHITESPACE;
import static org.junit.Assert.assertArrayEquals;

import java.util.LinkedList;

import mini.java.syntax.Terminal;

import org.junit.Test;


public class MJCompilerTest {

    @Test
    public void testTokenizer() {
        Terminal[] terminals = TOKENIZER.tokenize(
                "class Main {\n"
                + "    public static void main(String[] args) {\n"
                + "        // comment\n"
                + "    }\n"
                + "} ");
        
        Terminal[] expected = new Terminal[] {
                new Terminal(CLASS, "class"),
                new Terminal(WHITESPACE, " "),
                new Terminal(ID, "Main"),
                new Terminal(WHITESPACE, " "),
                new Terminal(LEFT_BRACE, "{"),
                new Terminal(WHITESPACE, "\n    "),
                
                new Terminal(PUBLIC, "public"),
                new Terminal(WHITESPACE, " "),
                new Terminal(STATIC, "static"),
                new Terminal(WHITESPACE, " "),
                new Terminal(VOID, "void"),
                new Terminal(WHITESPACE, " "),
                new Terminal(MAIN, "main"),
                new Terminal(LP, "("),
                new Terminal(STRING, "String"),
                new Terminal(LEFT_BRACKET, "["),
                new Terminal(RIGHT_BRACKET, "]"),
                new Terminal(WHITESPACE, " "),
                new Terminal(ID, "args"),
                new Terminal(RP, ")"),
                new Terminal(WHITESPACE, " "),
                new Terminal(LEFT_BRACE, "{"),
                new Terminal(WHITESPACE, "\n        "),
                
                new Terminal(COMMENT, "// comment\n"),
                new Terminal(WHITESPACE, "    "),
                new Terminal(RIGHT_BRACE, "}"),
                new Terminal(WHITESPACE, "\n"),
                new Terminal(RIGHT_BRACE, "}"),
                new Terminal(WHITESPACE, " "),
        };
        
//        {
//            assertEquals(Arrays.asList(expected).subList(0, 6),
//                    Arrays.asList(terminals).subList(0, 6));
//            assertEquals(Arrays.asList(expected).subList(6, 23),
//                    Arrays.asList(terminals).subList(6, 23));
//            assertEquals(Arrays.asList(expected).subList(23, expected.length),
//                    Arrays.asList(terminals).subList(23, terminals.length));
//        }
        
        assertArrayEquals(expected, terminals);
    }
    
    
    @Test
    public void testSyntax() {
        Terminal[] terminals = null;
        
        {
            java.util.List<Terminal> terms = new LinkedList<Terminal>();
            for (Terminal term : TOKENIZER.tokenize(
                "class Main { public static void main(String[] args) { System.out.println(123); } }"))
            {
                if (WHITESPACE.equals(term.getType())
                        || COMMENT.equals(term.getType()))
                {
                    
                }
                else
                {
                    terms.add(term);
                }
            }
            terminals = terms.toArray(new Terminal[0]);
        }
        
        PARSER.parse(terminals);
        
    }
}
