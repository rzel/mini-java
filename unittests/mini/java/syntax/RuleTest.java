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
        String ruleSpec = "";
        Rule rule = Rule.parse(ruleSpec);
        
        assertNull(rule);
    }
    
    @Test
    public final void testNullSpec() {
        String ruleSpec = null;
        Rule rule = Rule.parse(ruleSpec);
        
        assertNull(rule);
    }

    @Test
    public final void testEqualsSameInstance() {
        String ruleSpec = "A ::= B";
        Rule rule = Rule.parse(ruleSpec);
        
        assertEquals(rule, rule);
    }
    
    @Test
    public final void testEqualsSameClass() {
        String ruleSpec = "A ::= B";
        Rule rule = Rule.parse(ruleSpec);
        Rule anotherRule = Rule.parse(ruleSpec);
        
        assertEquals(rule, anotherRule);
    }
    
    @Test
    public final void testEqualsNull() {
        String ruleSpec = "A ::= B";
        Rule rule = Rule.parse(ruleSpec);
        
        assertFalse(rule.equals(null));
    }
    
    @Test
    public final void testEqualsDifferentRightSymbol() {
        Rule rule = Rule.parse("A ::= B");
        Rule rule0 = Rule.parse("A :: = C");
        
        assertFalse(rule.equals(rule0));
    }
    
    @Test
    public final void testEqualsDifferentLeftSymbols() {
        Rule rule = Rule.parse("A ::= B");
        Rule rule0 = Rule.parse("A :: = B C");
        
        assertFalse(rule.equals(rule0));
    }
    
    @Test
    public final void testEqualsDifferentClass() {
        String ruleSpec = "A ::= B";
        Rule rule = Rule.parse(ruleSpec);
        
        assertFalse(rule.equals(ruleSpec));
    }
    
    @Test
    public final void testHashCode() {
        Rule rule = Rule.parse("A ::= B");
        Rule rule0 = Rule.parse("A ::= B");
        
        assertEquals(rule.hashCode(), rule0.hashCode());
    }
}
