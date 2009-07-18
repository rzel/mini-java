package mini.java.lex;

import static org.junit.Assert.*;

import mini.java.regex.RegexConfig;
import mini.java.syntax.Terminal;

import org.junit.Test;


public class TokenizerTest {
    // fields
//    private final Tokenizer _target = new Tokenizer(RegexConfig._instance);
    // dummy tokenizer config, which supports nothing
//    private final ITokenizerConfig _emptyConf = new ITokenizerConfig() {
//        public IMatcher getMatcher(String type_) {
//            return null;
//        }
//        public IMatcher[] getMatchers() {
//            return new IMatcher[0]; // empty array
//        }
//        public String[] getTokenTypes() {
//            return new String[0];
//        }
//    };
    
//    @Test
//    public void testGetToken() {
//        assertEquals(new Terminal(RegexConfig.STAR, "*"),
//                _target.getToken("*"));
//        assertEquals(new Terminal(RegexConfig.CH, "*"), // backslash is removed
//                _target.getToken("\\*"));
//        assertEquals(new Terminal(RegexConfig.NUM, "1"),
//                _target.getToken("1"));
//    }
    
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
}
