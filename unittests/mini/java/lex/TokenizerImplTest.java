package mini.java.lex;

import static org.junit.Assert.*;

import mini.java.syntax.Terminal;

import org.junit.Test;


public class TokenizerImplTest {
    // fields
    private final TokenizerImpl _target = new TokenizerImpl(CharTokenizerConfig._instance);
    // dummy tokenizer config, which supports nothing
    private final ITokenizerConfig _emptyConf = new ITokenizerConfig() {
        public IMatcher getMatcher(String type_) {
            return null;
        }
        public IMatcher[] getMatchers() {
            return new IMatcher[0]; // empty array
        }
        public String[] getTokenTypes() {
            return new String[0];
        }
    };
    
    @Test
    public void testGetToken() {
        assertEquals(new Terminal(CharTokenizerConfig.STAR, "*"),
                _target.getToken("*"));
        assertEquals(new Terminal(CharTokenizerConfig.CH, "*"), // backslash is removed
                _target.getToken("\\*"));
        assertEquals(new Terminal(CharTokenizerConfig.NUM, "\\d"),
                _target.getToken("\\d"));
    }
    
    @Test
    public void testGetTokenNull() {        
        TokenizerImpl target = new TokenizerImpl(_emptyConf);
        assertNull(target.getToken("input"));
    }

    @Test
    public void testGetTokenLongest() {
        // dummy matcher: LONG
        final IMatcher longMat = new IMatcher() {
            public String match(String input_) {
                return input_.startsWith("AA")
                    ? "AA" : null;
            }
            public String getType() {
                return "LONG";
            }
        };
        // dummy matcher: SHORT
        final IMatcher shortMat = new IMatcher() {
            public String match(String input_) {
                return input_.startsWith("A")
                    ? "A" : null;
            }
            public String getType() {
                return "SHORT";
            }
        };
        // dummy config
        TokenizerImpl target = new TokenizerImpl(new ITokenizerConfig() {
            public IMatcher getMatcher(String type_) {
                throw new UnsupportedOperationException("Not implemented");
            }
            @Override
            public IMatcher[] getMatchers() {
                // set shortMatcher to have higher precedence over longMatcher
                return new IMatcher[] { shortMat, longMat };
            }
            @Override
            public String[] getTokenTypes() {
                throw new UnsupportedOperationException("Not implemented");
            }
        });
        
        assertEquals(new Terminal("SHORT", "A"), target.getToken("A"));
        assertEquals(new Terminal("LONG", "AA"), target.getToken("AA"));
        assertEquals(new Terminal("LONG", "AA"), target.getToken("AAA"));
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testTokenizeInvalidToken() {
        TokenizerImpl target = new TokenizerImpl(_emptyConf);
        target.tokenize("input");
        
        fail("tokenize() should throw on invalid tokens");
    }
}
