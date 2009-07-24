package mini.java.regex.legacy;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import mini.java.fa.v1.SimpleFASimulator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RegexTest {
    private String regex;
    private String str;
    private boolean result;

    public RegexTest(String regex, String str, boolean result) {
        this.regex = regex;
        this.str = str;
        this.result = result;
    }

    @Test public void test() throws Exception {
        assertEquals((new SimpleFASimulator(RegexCompiler.compile(regex).toDFA())).match(str), result);
    }

    @Parameters
    public static List<?> data() {
        return Arrays.asList(new Object[][] {
            {"(a|aa)b", "a", false}, //test[0]
            {"(a|aa)b", "ab", true},
            {"(a|aa)b", "aab", true},
            {"(a|aa)b", "aaab", false},
            {"a*b", "a", false},
            {"a*b", "ab", true}, //test[5]
            {"a*b", "aab", true},
            {"a*b", "aaab", true},
            {"a*b", "aaabb", false},
            {"a*b", "b", true},
            {"a*b", "", false}, //test[10]
            {"[abc].c*a?", "b\n", true},
            {"[abc].c*a?", "b\nccca", true},
            {"[abc].c*a?", "b\na", true},
            {"[abc].c*a?", "c\nc", true},

            //Anything
            {".*", "a", true}, //test[15]
            {".*", "", true},
            {".*", "\nthis is a test\n", true},

            //Integers
            {"-?\\d\\d*(\\.\\d\\d*)?", "-1", true},
            {"-?\\d\\d*(\\.\\d\\d*)?", "-1.0", true},
            {"-?\\d\\d*(\\.\\d\\d*)?", "-1.001", true}, //test[20]
            {"-?\\d\\d*(\\.\\d\\d*)?", "-10.10", true},
            {"-?\\d\\d*(\\.\\d\\d*)?", "-110.", false},
            {"-?\\d\\d*(\\.\\d\\d*)?", "10.10", true},
            {"-?\\d\\d*(\\.\\d\\d*)?", "10e10", false},

            //C-style Comments
            {"/\\*.*\\*/", "/*hello, world*/", true}, //test[25]
            {"/\\*.*\\*/", "/*hello\r\nworld*/", true},
            {"/\\*.*\\*/", "/*hello, world\r\n*/", true},
            {"/\\*.*\\*/", "/*  hello, world  */", true},
            {"/\\*.*\\*/", "/*hello, world***/", true},
            {"/\\*.*\\*/", "/***hello, world*/", true}, //test[30]
            {"/\\*.*\\*/", "/*//hello, world*/", true},
            {"/\\*.*\\*/", "/*hello, world//*/", true},

            //handle non-greedy match
            {"/\\*([^\\*]|\\*[^/])*\\**\\*/", "/*hello, world*/*/", false},
            {"/\\*([^\\*]|\\*[^/])*\\**\\*/", "/*hello, world*/**/", false},
            {"/\\*([^\\*]|\\*[^/])*\\**\\*/", "/**/hello, world*/", false},
            {"/\\*([^\\*]|\\*[^/])*\\**\\*/", "/*hello,*/ world*/", false},
            {"/\\*([^\\*]|\\*[^/])*\\**\\*/", "/*hello, world*/", true},
            {"/\\*([^\\*]|\\*[^/])*\\**\\*/", "/*hello\r\nworld*/", true}, //test[35]
            {"/\\*([^\\*]|\\*[^/])*\\**\\*/", "/*hello, world\r\n*/", true},
            {"/\\*([^\\*]|\\*[^/])*\\**\\*/", "/*  hello, world  */", true},
            {"/\\*([^\\*]|\\*[^/])*\\**\\*/", "/*hello, world**/", true},
            {"/\\*([^\\*]|\\*[^/])*\\**\\*/", "/**hello, world*/", true},
            {"/\\*([^\\*]|\\*[^/])*\\**\\*/", "/*/hello, world*/", true}, //test[40]
            {"/\\*([^\\*]|\\*[^/])*\\**\\*/", "/*hello, world/*/", true},
            {"/\\*([^\\*]|\\*[^/])*\\**\\*/", "/*/*hello, world*/", true},

            //C++-style comments, not for mac(\r as linefeed)
            {"//[^\n]*\n", "//hello, world\n", true},
            {"//[^\n]*\n", "//hello, world//\n", true},
            {"//[^\n]*\n", "////hello, world\n", true},
            {"//[^\n]*\n", "///*hello, world*/\n", true},
            {"//[^\n]*\n", "//hello, world\nhowdy, world\n", false},
            {"//[^\n]*\n", "//hello, world\r\nhowdy, world\n", false},
            {"//[^\n]*\n", "//hello\r, world\n", true},
            {"//[^\n]*\n", "//\n", true},
        });
    }
}
