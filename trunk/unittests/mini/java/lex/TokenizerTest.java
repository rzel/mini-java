package mini.java.lex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.List;

import mini.java.syntax.Terminal;

import org.junit.Test;


public class TokenizerTest {
    
    @Test
    public void testGetTokenNull() {        
        Tokenizer target = new Tokenizer();
        assertNull(target.getToken("input"));
    }

    @Test
    public void testGetTokenLongest() {
        Tokenizer target = new Tokenizer();
        
        // dummy matcher: LONG
        target.addMatcher(new IMatcher() {
            public String match(String input_) {
                return input_.startsWith("AA")
                    ? "AA" : null;
            }
            public String getType() {
                return "LONG";
            }
        });
        // dummy matcher: SHORT
        target.addMatcher(new IMatcher() {
            public String match(String input_) {
                return input_.startsWith("A")
                    ? "A" : null;
            }
            public String getType() {
                return "SHORT";
            }
        });
        
        
        assertEquals(new Terminal("SHORT", "A"), target.getToken("A"));
        assertEquals(new Terminal("LONG", "AA"), target.getToken("AA"));
        assertEquals(new Terminal("LONG", "AA"), target.getToken("AAA"));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testTokenizeInvalidToken() {
        Tokenizer target = new Tokenizer();
        target.tokenize("input");
        
        fail("tokenize() should throw on invalid tokens");
    }
    
    
    @Test
    public void testTokenize() {
        Tokenizer target = new Tokenizer();
        target.addMatcher(new IMatcher() {

            @Override
            public String getType() {
                return "A";
            }

            @Override
            public String match(String input_) {
                return input_.startsWith("A")
                    ? "A" : null;
            }
            
        });
        
        target.addMatcher(new IMatcher() {

            @Override
            public String getType() {
                return "BB";
            }

            @Override
            public String match(String input_) {
                return input_.startsWith("BB")
                    ? "BB" : null;
            }
            
        });
        
        List<Terminal> tokens = new LinkedList<Terminal>();
        for (String str : new String[] { "A", "BB", "A", "A", "BB" }) {
            tokens.add(new Terminal(str, str));
        }
        assertArrayEquals(tokens.toArray(new Terminal[0]),
                target.tokenize("ABBAABB"));
    }
}
