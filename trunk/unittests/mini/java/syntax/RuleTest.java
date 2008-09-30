package mini.java.syntax;
import static org.junit.Assert.*;

import java.util.Arrays;


import org.junit.Test;


public class RuleTest {
    @Test
    public final void testParseSingleLeftSymbol() {
        String ruleSpec = "A ::= B";
        Rule rule = Rule.parse(ruleSpec);
        
        assertNotNull(rule);
        assertEquals(rule.getRightSymbol(), "A");
        assertEquals(rule.getLeftSymbols(), Arrays.asList("B"));
    }
    
    @Test
    public final void testParseMultipleLeftSymbols() {
        String ruleSpec = "A ::= B C";
        Rule rule = Rule.parse(ruleSpec);
        
        assertNotNull(rule);
        assertEquals(rule.getRightSymbol(), "A");
        assertEquals(rule.getLeftSymbols(), Arrays.asList("B", "C"));
    }
    
    @Test
    public final void testParseNoRightSymbol() {
        String ruleSpec = " ::= B";
        Rule rule = Rule.parse(ruleSpec);
        
        assertNull(rule);
    }
    
    @Test
    public final void testParseNoLeftSymbol() {
        String ruleSpec = "A ::=  ";
        Rule rule = Rule.parse(ruleSpec);
        
        assertNull(rule);
    }
    
    @Test
    public final void testParseNoAssignmentSymbol() {
        String ruleSpec = "A B";
        Rule rule = Rule.parse(ruleSpec);
        
        assertNull(rule);
    }
    
    @Test
    public final void testParseMultipleAssignmentSymbols() {
        String ruleSpec = "A ::= B ::= C";
        Rule rule = Rule.parse(ruleSpec);
        
        assertNull(rule);
    }
    
    @Test
    public final void testEmptySpec() {
        Rule rule = Rule.parse("");
        
        assertNull(rule);
    }
    
    @Test
    public final void testNullSpec() {
        Rule rule = Rule.parse(null);
        
        assertNull(rule);
    }

    @Test
    public final void testEqualsSameInstance() {
        Rule rule = Rule.parse("A ::= B");
        
        assertNotNull(rule);
        assertEquals(rule, rule);
    }
    
    @Test
    public final void testEqualsSameClass() {
        String ruleSpec = "A ::= B";
        Rule rule = Rule.parse(ruleSpec);
        Rule anotherRule = Rule.parse(ruleSpec);
        
        assertNotNull(rule);
        assertNotNull(anotherRule);
        assertEquals(rule, anotherRule);
    }
    
    @Test
    public final void testEqualsNull() {
        Rule rule = Rule.parse("A ::= B");
        
        assertNotNull(rule);
        assertFalse(rule.equals(null));
    }
    
    @Test
    public final void testEqualsDifferentRightSymbol() {
        Rule rule = Rule.parse("A ::= B");
        Rule anotherRule = Rule.parse("A ::= C");
        
        assertNotNull(rule);
        assertNotNull(anotherRule);
        assertFalse(rule.equals(anotherRule));
    }
    
    @Test
    public final void testEqualsDifferentLeftSymbols() {
        Rule rule = Rule.parse("A ::= B");
        Rule anotherRule = Rule.parse("A ::= B C");
        
        assertNotNull(rule);
        assertNotNull(anotherRule);
        assertFalse(rule.equals(anotherRule));
    }
    
    @Test
    public final void testEqualsDifferentClass() {
        Rule rule = Rule.parse("A ::= B");
        
        assertNotNull(rule);
        assertFalse(rule.equals(new Object()));
    }
    
    @Test
    public final void testHashCode() {
        Rule rule = Rule.parse("A ::= B");
        Rule anotherRule = Rule.parse("A ::= B");
        
        assertEquals(rule.hashCode(), anotherRule.hashCode());
    }
    
    @Test
    public final void testGetItems() {
        Rule rule = Rule.parse("A ::= B");
        
        assertNotNull(rule);
        assertNotNull(rule.getLeftSymbols());
        assertNotNull(rule.getItems());
        assertTrue(rule.getLeftSymbols().size() == (rule.getItems().size()-1));
    }
}
