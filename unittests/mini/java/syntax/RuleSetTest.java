package mini.java.syntax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;


public class RuleSetTest {
    
    private static RuleSet RULES;
    
    static {
        RULES = new RuleSet();
        RULES.addRule(new Rule().left(RuleSet.START).right("E"));
        RULES.addRule(new Rule().left("E").right("E", "+", "T"));
        RULES.addRule(new Rule().left("E").right("T"));
        RULES.addRule(new Rule().left("T").right("T", "*", "F"));
        RULES.addRule(new Rule().left("T").right("F"));
        RULES.addRule(new Rule().left("F").right("(", "E", ")"));
        RULES.addRule(new Rule().left("F").right("id"));
    }
    

    @Test
    public void testGetTerminals() {
        RuleSet rules = new RuleSet();
        rules.addRule(new Rule().left(RuleSet.START).right("A", "B"));
        rules.addRule(new Rule().left("A").right("B"));
        
        assertEquals(Collections.singleton("B"), rules.getTerminals());
    }
    
    @Test
    public void testGetTerminalsMore() {
        assertEquals(new HashSet<String>(
                Arrays.asList("+", "*", "(", ")", "id")
            ), RULES.getTerminals());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testGetStart() {
        new RuleSet().getStart();
        
        fail(
                "RuleSet should throw if it doesn't contain one START rule");
    }
    
    @Test
    public void testGetFollows() {
        RuleSet rules = new RuleSet();
        rules.addRule(new Rule().left(RuleSet.START).right("A", "B"));
        rules.addRule(new Rule().left("A").right("C"));
        rules.addRule(new Rule().left("C").right("D"));
        
        assertEquals(Collections.singleton("B"), rules.getFollows("D"));
        assertEquals(Collections.singleton("B"), rules.getFollows("C"));
        assertEquals(Collections.singleton("END"), rules.getFollows("B"));
        assertEquals(Collections.singleton("END"), rules.getFollows(RuleSet.START));
        
        try {
            rules.getFollows("E");
            fail(
                    "RuleSet should throw if given unknown symbols");
        } catch (IllegalArgumentException ex_) { }
        
    }
    
    @Test
    public void testGetFollowsMore() {
        assertEquals(
                new HashSet<String>(
                        Arrays.asList("+", "*", ")", RuleSet.END)), RULES.getFollows("id"));
        assertEquals(
                new HashSet<String>(
                        Arrays.asList("(", "id")), RULES.getFollows("+"));
        assertEquals(
                new HashSet<String>(
                        Arrays.asList("+", ")", RuleSet.END)), RULES.getFollows("E"));
    }
    
    
    @Test(expected=IllegalArgumentException.class)
    public void testMultipleStartRules() {
        RuleSet rules = new RuleSet();
        rules.addRule(new Rule().left(RuleSet.START));
        rules.addRule(new Rule().left(RuleSet.START));
        
        fail("RuleSet should throw if given multiple START rules");
    }
    
    @Test
    public void testGetSymbols() {
        Set<String> expected = new HashSet<String>();
        expected.addAll(Arrays.asList("+", "*", "(", ")", "id", "E", "F", "T", RuleSet.START));
        assertEquals(expected, RULES.getSymbols());
    }
    
    @Test
    public void testGetTerminalsCached() {
        RuleSet rules = new RuleSet();
        rules.addRule(new Rule().left(RuleSet.START).right("A"));
        assertEquals(
                new HashSet<String>(Arrays.asList("A")), rules.getTerminals());
        
        rules.addRule(new Rule().left("A").right("B"));
        assertEquals(
                new HashSet<String>(Arrays.asList("B")), rules.getTerminals());
        assertEquals(
                new HashSet<String>(Arrays.asList("A", RuleSet.START)), rules.getNonTerminals());
        assertEquals(
                new HashSet<String>(Arrays.asList("A", "B", RuleSet.START)), rules.getSymbols());
    }
    
}
