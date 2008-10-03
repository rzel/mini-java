package mini.java.regex.legacy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import java.util.Set;
import java.util.HashSet;

public class RegexTokenTest {
    @Test public void testEqualsNullInput() {
        RegexToken token = new RegexToken(RegexToken.Type.CHAR, null);
        RegexToken token0 = new RegexToken(RegexToken.Type.CHAR, null);
        assertEquals(token, token0);
    }

    @Test public void testEqualsNullInputFalse() {
        RegexToken token = new RegexToken(RegexToken.Type.CHAR, null);
        RegexToken token0 = new RegexToken(RegexToken.Type.STAR, null);
        assertFalse(token.equals(token0));
    }

    @Test public void testEqualsNullInputFalse0() {
        RegexToken token = new RegexToken(RegexToken.Type.CHAR, null);
        RegexToken token0 = new RegexToken(RegexToken.Type.STAR, new HashSet());
        assertFalse(token.equals(token0));
    }

    @Test public void testEquals() {
        Set s = new HashSet();
        s.add('a');
        s.add('b');

        Set s0 = new HashSet();
        s0.add('b');
        s0.add('a');

        RegexToken token = new RegexToken(RegexToken.Type.CHAR, s);
        RegexToken token0 = new RegexToken(RegexToken.Type.CHAR, s0);
        assertEquals(token, token0);
    }

    @Test public void testEqualsFalse() {
        Set s = new HashSet();
        s.add('a');
        s.add('b');

        Set s0 = new HashSet();

        RegexToken token = new RegexToken(RegexToken.Type.CHAR, s);
        RegexToken token0 = new RegexToken(RegexToken.Type.CHAR, s0);
        assertFalse(token.equals(token0));
    }
}
