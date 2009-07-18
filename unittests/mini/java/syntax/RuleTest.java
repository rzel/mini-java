package mini.java.syntax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;


public class RuleTest {
    @Test
    public final void testEqualsSameInstance() {
        Rule rule = Rule.createRule("A ::= B");
        
        assertNotNull(rule);
        assertEquals(rule, rule);
    }
    
    @Test
    public final void testEqualsSameClass() {
        String ruleSpec = "A ::= B";
        Rule rule = Rule.createRule(ruleSpec);
        Rule anotherRule = Rule.createRule(ruleSpec);
        
        assertNotNull(rule);
        assertNotNull(anotherRule);
        assertEquals(rule, anotherRule);
    }
    
    @Test
    public final void testEqualsDifferentClass() {
        Rule rule = Rule.createRule("A ::= B");
        
        assertNotNull(rule);
        assertFalse(rule.equals(new Object()));
    }
    
    @Test
    public final void testEqualsNull() {
        Rule rule = Rule.createRule("A ::= B");
        
        assertNotNull(rule);
        assertFalse(rule.equals(null));
    }
    
    @Test
    public final void testEqualsDifferentRightSymbol() {
        Rule rule = Rule.createRule("A ::= B");
        Rule anotherRule = Rule.createRule("A ::= C");
        
        assertNotNull(rule);
        assertNotNull(anotherRule);
        assertFalse(rule.equals(anotherRule));
    }
    
    @Test
    public final void testEqualsDifferentLeftSymbols() {
        Rule rule = Rule.createRule("A ::= B");
        Rule anotherRule = Rule.createRule("A ::= B C");
        
        assertNotNull(rule);
        assertNotNull(anotherRule);
        assertFalse(rule.equals(anotherRule));
    }
    
    @Test
    public final void testHashCode() {
        Rule rule = Rule.createRule("A ::= B");
        Rule anotherRule = Rule.createRule("A ::= B");
        
        assertEquals(rule.hashCode(), anotherRule.hashCode());
    }
    
//    @Test
//    public final void testGetItems() {
//        Rule rule = Rule.createRule("A ::= B");
//        
//        assertNotNull(rule);
//        assertNotNull(rule.getRightSymbols());
//        assertNotNull(rule.getItems());
//        assertTrue(rule.getRightSymbols().size() == (rule.getItems().size()-1));
//    }
}
