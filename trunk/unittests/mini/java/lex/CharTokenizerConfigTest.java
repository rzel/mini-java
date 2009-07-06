package mini.java.lex;

import static org.junit.Assert.*;

import org.junit.Test;


public class CharTokenizerConfigTest {
    // the target being tested
    private final CharTokenizerConfig conf = CharTokenizerConfig._instance;

    @Test
    public void testNumMatcher() {
        IMatcher numMatcher = conf.getMatcher(CharTokenizerConfig.NUM);
        
        assertNotNull(numMatcher.match("\\d"));
        assertNotNull(numMatcher.match("\\d*"));
        assertNull(numMatcher.match("1")); // NumMatcher matches '\d' not numbers!
        assertNull(numMatcher.match("\\w"));
        assertNull(numMatcher.match(""));
    }
    
    @Test
    public void testChMatcher() {
        IMatcher chMatcher = conf.getMatcher(CharTokenizerConfig.CH);
        
        assertNotNull(chMatcher.match("abc"));
        assertNotNull(chMatcher.match("\\*"));
        assertNotNull(chMatcher.match("\\"));
        assertNull(chMatcher.match(""));
    }
}
