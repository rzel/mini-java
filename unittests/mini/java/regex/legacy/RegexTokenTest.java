package mini.java.regex.legacy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import java.util.Set;
import java.util.HashSet;

public class RegexTokenTest {
    @Test
    public void testEqualsNullInput() {
        RegexToken token = new RegexToken(RegexToken.Type.CHAR, null);
        RegexToken token0 = new RegexToken(RegexToken.Type.CHAR, null);
        assertEquals(token, token0);
    }

    @Test
    public void testEqualsNullInputFalse() {
        RegexToken token = new RegexToken(RegexToken.Type.CHAR, null);
        RegexToken token0 = new RegexToken(RegexToken.Type.STAR, null);
        assertFalse(token.equals(token0));
    }

    @Test
    public void testEqualsNullInputFalse0() {
        RegexToken token = new RegexToken(RegexToken.Type.CHAR, null);
        RegexToken token0 = new RegexToken(RegexToken.Type.STAR, new HashSet<Object>());
        assertFalse(token.equals(token0));
    }

    @Test
    public void testEquals() {
        Set<Character> s = new HashSet<Character>();
        s.add('a');
        s.add('b');

        Set<Character> s0 = new HashSet<Character>();
        s0.add('b');
        s0.add('a');

        RegexToken token = new RegexToken(RegexToken.Type.CHAR, s);
        RegexToken token0 = new RegexToken(RegexToken.Type.CHAR, s0);
        assertEquals(token, token0);
    }

    @Test
    public void testEqualsFalse() {
        Set<Character> s = new HashSet<Character>();
        s.add('a');
        s.add('b');

        Set<Character> s0 = new HashSet<Character>();

        RegexToken token = new RegexToken(RegexToken.Type.CHAR, s);
        RegexToken token0 = new RegexToken(RegexToken.Type.CHAR, s0);
        assertFalse(token.equals(token0));
    }
}
