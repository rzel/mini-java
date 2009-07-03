package mini.java.syntax;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import mini.java.ComponentFactory;
import mini.java.fa.v3.DFA;
import mini.java.fa.v3.DFABuilder;
import mini.java.fa.v3.InitialState;
import mini.java.fa.v3.State;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ParserConfigGetDFATest {
    private ParserConfig _parserConfig;
    private DFA          _dfa;
    
    // Constructor
    public ParserConfigGetDFATest(String rules_, String transitions_) throws IOException {
//        assertNotNull(ruleSpecs_); // avoid error
//        assertNotNull(dfaSpec_);
        
        _parserConfig = createParserConfig(rules_.split(",,"));
        _dfa          = createDFA(transitions_.split(",,"));
        
        assertNotNull(_parserConfig);
        assertNotNull(_dfa);
    }
    
    @Test
    public void testGetDFA() {
        DFA dfa = _parserConfig.getDFA();
        assertNotNull(dfa);
        
        String got = mini.java.fa.Helper.dumpString(dfa);
        String expected = mini.java.fa.Helper.dumpString(_dfa);
//        assertTrue(mini.java.fa.Helper.compare(dfa, _dfa));
        assertEquals(expected, got);
    }
    
    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
           {"START ::= A",              "abA"},
           {"START ::= A B",            "abA,,bcB"},
           {"START ::= A,,A ::= B",     "abA,,acB"},
           {"START ::= A B,,B ::= C",   "abA,,bcB,,bdC"},
           {"START ::= A B,,A ::= C",   "abA,,bcB,,adC"},
           {"START ::= A,,A ::= B,,B ::= C",                    "abA,,acB,,adC"},
           {"START ::= S,,S ::= A B,,S ::= C B,,B ::= D",       "abA,,acC,,agS,,bdB,,bfD,,ceB,,cfD"},
           {
               // syntax specification for REGEX
               "START ::= E"    + ",," +
               "E ::= E *"      + ",," +
               "E ::= E | E"    + ",," +
               "E ::= ( E )"    + ",," +
               "E ::= E E"      + ",," +
               "E ::= C",
               
               "abE,,acC,,ad(,," +
               "beE,,bf*,,bg|,,bcC,,bd(,," +
               "dhE,,dcC,,dd(,," +
               "eeE,,ef*,,eg|,,ecC,,ed(,," +
               "giE,,gcC,,gd(,," +
               "heE,,hf*,,hg|,,hcC,,hd(,,hj),," +
               "ieE,,if*,,ig|,,icC,,id("
           }
        });
    }
    
    // Helper function used to create the ParserConfig instance based on the given rule specs
    private static ParserConfig createParserConfig(String... ruleSpecs) throws IOException {
        assert(ruleSpecs.length > 0);
        
        // build the syntax specification
        StringBuilder stringBuilder = new StringBuilder();
        for (String ruleSpec : ruleSpecs) {
            stringBuilder.append(ruleSpec);
            stringBuilder.append("\n");
        }
        String syntaxSpec = stringBuilder.toString();
        ParserConfig parserConfig = new ParserConfig(new StringReader(syntaxSpec));
        
        assertNotNull(parserConfig);
        assertNotNull(parserConfig.getRules());
        assertTrue(parserConfig.getRules().size() == ruleSpecs.length);
        
        return parserConfig;
    }
    
    /**
     * Helper function used to create DFA. It takes a set of string
     * representations of the transitions as parameter. The representation has
     * the form of: "abSTR", in which the first character ("a") stands for
     * source state, the second character ("b") stands for the target state and
     * the rest ("STR") stands for the input string. <b>NOTE</b>: The first source
     * state will be treated as the initial state.
     */
    private static DFA createDFA(String... transitions) {
        assert(transitions != null);
        assert(transitions.length > 0);
        
        DFABuilder builder = ComponentFactory.createDFABuilder();
        
        // mapping from character representation to the actual state
        Map<Character, State> states = new HashMap<Character, State>();
        
        // the first source state should be treated as an initial state
        states.put(transitions[0].charAt(0), new InitialState());
        
        for (String transition : transitions) {
            assert(transition.length() > 2);
            
            Character source = transition.charAt(0);
            Character target = transition.charAt(1);
            Object input = transition.substring(2);
            
            // create the corresponding states if they don't exist
            if (!states.containsKey(source)) {
                states.put(source, new State());
            }            
            if (!states.containsKey(target)) {
                states.put(target, new State());
            }
            
            // get the states adn add the transition
            State sourceState = states.get(source);
            State targetState = states.get(target);
            builder.addTransition(sourceState, targetState, input);
        }
        
        // build and return the DFA
        return builder.buildDFA();
    }
}
