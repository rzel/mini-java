package mini.java.lex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import mini.java.fa.AcceptableNFAState;
import mini.java.fa.NFAState;

import org.junit.Test;


public class FAMatcherTest {
    
    @Test
    public void testMatch() {
        NFAState A = new AcceptableNFAState(),
            B = new NFAState();
        A.addTransition(A, "a");
        A.addTransition(B, "b");
        B.addTransition(A, "a");
        
        FAMatcher matcher = new FAMatcher(null, A);
        assertEquals("aa", matcher.match("aab")); // longest
        assertEquals("aaba", matcher.match("aaba"));
        assertEquals("", matcher.match("b")); // empty
        assertNull(matcher.match(null)); // null
    }

}
