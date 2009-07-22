package mini.java.syntax;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

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

//    @Test
//    public void testAddTransition() {
//        ParserState root = ParserState.createRoot();
//        root.addTransition(new ParserState(root), "A");
//        root.addTransition(new ParserState(root), "A"); // another with the same input
//        
//        assertNotNull("ParserState should be able to handle multiple transitions with the same input",
//                root.getEpsilons());
//        assertEquals(1, root.getEpsilons().size());
//        assertEquals("0 =>(A) 1\n", Helper.dump(Helper.collapse(root)));
//    }
    
    @Test
    public void testGetRules() {
        
        RuleSet rules = new RuleSet();
        
        rules.addRule(new Rule().left(RuleSet.START).right("A"));
        rules.addRule(new Rule().left("A").right("B", "C"));
        rules.addRule(new Rule().left("A").right("B", "D"));
        Rule B = new Rule().left("B").right("E"); // has both "C" and "D" as follows
        rules.addRule(B); 
        
        ParserState root = ParserState.createRoot();
        root.addRule(B);
        assertEquals(B, root.getRule("C"));
        assertEquals(B, root.getRule("D"));
        assertNull(root.getRule("E"));
        // getRules() shouldn't return duplicate rules
        assertArrayEquals(new Rule[] {B}, root.getRules());
    }
}
