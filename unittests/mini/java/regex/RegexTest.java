package mini.java.regex;

import static org.junit.Assert.assertEquals;
import mini.java.fa.NFAState;
import mini.java.fa.helper.Helper;
import mini.java.lex.FAMatcher;
import mini.java.lex.IMatcher;

import org.junit.Test;


public class RegexTest {
    
    private final static boolean DEBUG = true;

    @Test
    public void testBasic() {
        __testRegex("(a|aa)b", "a", null);
        __testRegex("(a|aa)b", "ab");
        __testRegex("(a|aa)b", "aab");
        __testRegex("(a|aa)b", "aaab", null);
        __testRegex("a*b", "a", null);
        __testRegex("a*b", "ab");
        __testRegex("a*b", "aab");
        __testRegex("a*b", "aaabb", "aaab");
        __testRegex("a*b", "b");
        __testRegex("a*b", "", null);
        __testRegex("[abc].c*a?", "b\n");
        __testRegex("[abc].c*a?", "b\nccca");
        __testRegex("[abc].c*a?", "b\na");
        __testRegex("[abc].c*a?", "c\nc");
    }
    
    @Test
    public void testDot() {
        __testRegex(".*", "a");
        __testRegex(".*", "");
        __testRegex(".*", "\nthis is a test\n");
    }
    
    @Test
    public void testIntegers() {
        __testRegex("-?[0-9][0-9]*(\\.[0-9][0-9]*)?", "-1");
        __testRegex("-?[0-9][0-9]*(\\.[0-9][0-9]*)?", "-1.0");
        __testRegex("-?[0-9][0-9]*(\\.[0-9][0-9]*)?", "-1.001");
        __testRegex("-?[0-9][0-9]*(\\.[0-9][0-9]*)?", "-10.10");
        __testRegex("-?[0-9][0-9]*(\\.[0-9][0-9]*)?", "-110.", "-110");
        __testRegex("-?[0-9][0-9]*(\\.[0-9][0-9]*)?", "10.10");
        __testRegex("-?[0-9][0-9]*(\\.[0-9][0-9]*)?", "10e10", "10");
    }
    
    @Test
    public void testCStyleComments() {
        __testRegex("/\\*.*\\*/", "/*hello, world*/");
        __testRegex("/\\*.*\\*/", "/*hello\r\nworld*/");
        __testRegex("/\\*.*\\*/", "/*hello, world\r\n*/");
        __testRegex("/\\*.*\\*/", "/*  hello, world  */");
        __testRegex("/\\*.*\\*/", "/*hello, world***/");
        __testRegex("/\\*.*\\*/", "/***hello, world*/");
        __testRegex("/\\*.*\\*/", "/*//hello, world*/");
        __testRegex("/\\*.*\\*/", "/*hello, world//*/");
    }
    
    @Test
    public void testCppStyleComments() {
        __testRegex("//[^\n]*\n", "//hello, world\n");
        __testRegex("//[^\n]*\n", "//hello, world//\n");
        __testRegex("//[^\n]*\n", "////hello, world\n");
        __testRegex("//[^\n]*\n", "///*hello, world*/\n");
        __testRegex("//[^\n]*\n", "//hello, world\nhowdy, world\n", "//hello, world\n");
        __testRegex("//[^\n]*\n", "//hello, world\r\nhowdy, world\n", "//hello, world\r\n");
        __testRegex("//[^\n]*\n", "//hello\r, world\n");
        __testRegex("//[^\n]*\n", "//\n");
    }
    
    @Test
    public void testNonGreedyMatch() {
        __testRegex("/\\*([^*]|\\*[^/])*\\**\\*/", "/*hello, world*/*/", "/*hello, world*/");
        __testRegex("/\\*([^*]|\\*[^/])*\\**\\*/", "/*hello, world*/**/", "/*hello, world*/");
        __testRegex("/\\*([^*]|\\*[^/])*\\**\\*/", "/**/hello, world*/", "/**/");
        __testRegex("/\\*([^*]|\\*[^/])*\\**\\*/", "/*hello,*/ world*/", "/*hello,*/");
        __testRegex("/\\*([^*]|\\*[^/])*\\**\\*/", "/*hello, world*/");
        __testRegex("/\\*([^*]|\\*[^/])*\\**\\*/", "/*hello\r\nworld*/");
        __testRegex("/\\*([^*]|\\*[^/])*\\**\\*/", "/*hello, world\r\n*/");
        __testRegex("/\\*([^*]|\\*[^/])*\\**\\*/", "/*  hello, world  */");
        __testRegex("/\\*([^*]|\\*[^/])*\\**\\*/", "/*hello, world**/");
        __testRegex("/\\*([^*]|\\*[^/])*\\**\\*/", "/**hello, world*/");
        __testRegex("/\\*([^*]|\\*[^/])*\\**\\*/", "/*/hello, world*/");
        __testRegex("/\\*([^*]|\\*[^/])*\\**\\*/", "/*hello, world/*/");
        __testRegex("/\\*([^*]|\\*[^/])*\\**\\*/", "/*/*hello, world*/");
    }
    
    
    private static void __testRegex(String regex_, String target_) {
        __testRegex(regex_, target_, target_);
    }
    
    private static void __testRegex(String regex_, String target_, String match_) {
        NFAState fa = RegexCompiler.compile(regex_);
        
        IMatcher matcher = new FAMatcher(null, fa);
        assertEquals(String.format("Regex: %s%s", regex_, 
                (DEBUG) ? "\n----\n" + Helper.dump(fa) : ""),
                    match_, matcher.match(target_));
    }
}
