package mini.java.syntax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import mini.java.fa.helper.Helper;

import org.junit.Test;


public class ParserStateTest {
    
    @Test
    public void testGetRule() {
        RuleSet rules = new RuleSet();
        Rule A = new Rule().left("A").right("C");
        Rule B = new Rule().left("B").right("D");
        rules.addRule(new Rule().left(RuleSet.START).right("A", "B"));
        rules.addRule(A);
        rules.addRule(B);
        
        ParserState state = ParserState.createRoot();
        state.addRules(A, B);
        
        assertEquals(A, state.getRule("D"));
        assertEquals(B, state.getRule(RuleSet.END));
        assertNull("NonTerminals should be considered as lookaheads", state.getRule("B"));
    }

    @Test
    public void testAddTransition() {
        ParserState root = ParserState.createRoot();
        root.addTransition(new ParserState(root), "A");
        root.addTransition(new ParserState(root), "A"); // another with the same input
        
        assertNotNull("ParserState should be able to handle multiple transitions with the same input",
                root.getEpsilons());
        assertEquals(1, root.getEpsilons().size());
        assertEquals("0 =>(A) 1\n", Helper.dump(Helper.collapse(root)));
    }
}
