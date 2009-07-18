package mini.java.syntax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;


public class ParserTest {
    
    @Test
    public void testReduceShiftConflict() {
        RuleSet rules = new RuleSet();
        rules.addRule(new Rule().addLeftSymbol(RuleSet.START).addRightSymbols("E"));
        rules.addRule(new Rule().addLeftSymbol("E").addRightSymbols("1", "E"));
        rules.addRule(new Rule().addLeftSymbol("E").addRightSymbols("1"));
        
        
        Terminal[] tokens = new Terminal[] {
                new Terminal("1", "1"),
                new Terminal("1", "1"),
        };
        NonTerminal got = new Parser(rules).parse(tokens);
        
        SymbolDumper dumper = new SymbolDumper();
        got.accept(dumper);
        assertEquals("START(E(1<1>, E(1<1>)))", dumper.toString());
    }
    
    
    @Test
    public void testPrecedence() {
        RuleSet rules = new RuleSet();
        rules.addRule(new Rule().left(RuleSet.START).right("E"));
        rules.addRule(new Rule().left("E").right("E", "E"));
        rules.addRule(new Rule().left("E").right("E", "|", "E"));
        rules.addRule(new Rule().left("E").right("C"));
        
        {
            List<Terminal> tokens = new LinkedList<Terminal>();
            for (Character c : "C|CC".toCharArray()) {
                tokens.add(new Terminal(c.toString()));
            }
            NonTerminal root = new Parser(rules).parse(tokens.toArray(new Terminal[0]));
            assertNotNull(root);
            assertEquals("START(E(E(E(C),|,E(C)),E(C)))", root.toString());
        }
    }
    
    
    @Test
    public void testPrecedenceFixed() {
        RuleSet rules = new RuleSet();
        rules.addRule(new Rule().left(RuleSet.START).right("BarExpr"));
        rules.addRule(new Rule().left("BarExpr").right("BarExpr", "|", "BarExpr"));
        rules.addRule(new Rule().left("BarExpr").right("E"));
        rules.addRule(new Rule().left("E").right("E", "E"));        
        rules.addRule(new Rule().left("E").right("C"));
        
        {
            List<Terminal> tokens = new LinkedList<Terminal>();
            for (Character c : "C|CC".toCharArray()) {
                tokens.add(new Terminal(c.toString()));
            }
            NonTerminal root = new Parser(rules).parse(tokens.toArray(new Terminal[0]));
            assertNotNull(root);
            assertEquals("START(BarExpr(BarExpr(E(C)),|,BarExpr(E(E(C),E(C)))))", root.toString());
        }
    }
    
    @Test
    public void testParser() {
        RuleSet rules = new RuleSet();
        rules.addRule(new Rule().left(RuleSet.START).right("E"));
        rules.addRule(new Rule().left("E").right("E", "*"));
        rules.addRule(new Rule().left("E").right("E", "|", "E"));
        rules.addRule(new Rule().left("E").right("(", "E", ")"));
        rules.addRule(new Rule().left("E").right("E", "E"));
        rules.addRule(new Rule().left("E").right("C"));
        
        {
            List<Terminal> tokens = new LinkedList<Terminal>();
            for (Character c : "(C)*|CC".toCharArray()) {
                tokens.add(new Terminal(c.toString()));
            }
            
            NonTerminal ret = new Parser(rules).parse(tokens.toArray(new Terminal[0]));
            assertNotNull(ret);
            assertEquals("START(E(E(E(E((,E(C),)),*),|,E(C)),E(C)))", ret.toString());
        }
    }

}
