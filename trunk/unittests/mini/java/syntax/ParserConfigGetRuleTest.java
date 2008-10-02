package mini.java.syntax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;

import mini.java.fa.DFASimulator;
import mini.java.fa.DFASimulatorImpl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ParserConfigGetRuleTest {
    // syntax specification for REGEX
    private static String REGEX_SYNTAX = 
        "START ::= E"    + ",," +
        "E ::= E *"      + ",," +
        "E ::= E | E"    + ",," +
        "E ::= ( E )"    + ",," +
        "E ::= E E"      + ",," +
        "E ::= C";
    
    private ParserConfig _parserConfig;
    private Rule         _expected;
    private String[]     _symbols;
    
    // Constructor
    public ParserConfigGetRuleTest(String rules_, String symbols_,
            String expected_) throws IOException {
        assert (rules_ != null);
        assert (symbols_ != null);
        assert (expected_ != null);

        _parserConfig = new ParserConfig(new StringReader(rules_.replaceAll(
                ",,", "\n")));
        assertNotNull(_parserConfig);
        assertNotNull(_parserConfig.getRules());

        _expected = Rule.createRule(expected_);
        assertNotNull(_expected);

        _symbols = symbols_.split(",,");
        assertNotNull(_symbols);
    }
    
    @Test
    public void testGetRule() {
        DFASimulator dfaSimulator = new DFASimulatorImpl(_parserConfig.getDFA());
        for (String symbol : _symbols) {
            dfaSimulator.step(symbol);
            assert(dfaSimulator.isRunning());
        }
        
//        State targetState = dfaSimulator.getDFAState();
//        assertTrue(targetState instanceof AcceptableState);
//        Rule targetRule = _parserConfig.getRule((AcceptableState)targetState);
        Rule targetRule = _parserConfig.getRule(dfaSimulator.getDFAState());
        
        assertNotNull(targetRule);
        assertEquals(_expected, targetRule);
    }
    
    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
           {"START ::= A",                      "A",            "START ::= A"},
           {"START ::= A B",                    "A,,B",         "START ::= A B"},
           {"START ::= A,,A ::= B",             "A",            "START ::= A"},
           {"START ::= A,,A ::= B",             "B",            "A ::= B"},
           {"START ::= A B,,A ::= C,,B ::= C",  "A,,B",         "START ::= A B"},
           {"START ::= A B,,A ::= C,,B ::= C",  "C",            "A ::= C"},
           {"START ::= A B,,A ::= C,,B ::= C",  "A,,C",         "B ::= C"},
           
           {"START ::= A C,,START ::= B C,,C ::= D",    "A,,D",         "C ::= D"},
           {"START ::= A C,,START ::= B C,,C ::= D",    "B,,D",         "C ::= D"},
           {"START ::= A C,,START ::= B C,,C ::= D",    "A,,C",         "START ::= A C"},
           {"START ::= A C,,START ::= B C,,C ::= D",    "B,,C",         "START ::= B C"},
           
           
           {REGEX_SYNTAX, "C", "E ::= C"}, // Bug001, loop transitions
        });
    }

}
