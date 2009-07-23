package mini.java.syntax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.List;

import mini.java.fa.NFAState;
import mini.java.fa.helper.Helper;

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
    
    
    @Test(expected=RuntimeException.class)
    public void testReduceReduceConflict() {
        RuleSet rules = new RuleSet();
        rules.addRule(new Rule().left(RuleSet.START).right("S"));
        rules.addRule(new Rule().left("S").right("A"));
        rules.addRule(new Rule().left("S").right("B"));
        rules.addRule(new Rule().left("A").right("1"));
        rules.addRule(new Rule().left("B").right("1"));
        
        Parser.buildEngine(rules);        
        fail("This grammar cannot be handled by LR(1); ParserConfig should be able to detect this.");
    }
    
    @Test
    public void testReduceReduceNoConflict() {
        RuleSet rules = new RuleSet();
        Rule A = new Rule().left("A").right("1");
        Rule B = new Rule().left("B").right("1");
        rules.addRule(new Rule().left(RuleSet.START).right("S"));
        rules.addRule(new Rule().left("S").right("A", "1"));
        rules.addRule(new Rule().left("S").right("B", "2"));
        rules.addRule(A);
        rules.addRule(B);
        

        ParserState engine = Parser.buildEngine(rules);
        assertEquals(
                "0 =>(1) 1\n" + 
                "0 =>(A) 2\n" + 
                "0 =>(B) 3\n" +
                "0 =>(S) 4\n" +
                "2 =>(1) 5\n" +
                "3 =>(2) 6\n", Helper.dump(engine));
        
        ParserState target = (ParserState)engine.getState("1");
        assertNotNull(target);
        assertNotNull(target.getRules());
        assertEquals(2, target.getRules().length);
        assertEquals(A, target.getRule("1"));
        assertEquals(B, target.getRule("2"));
    }
    
    
    @Test
    public void testBuildEngine() {
        RuleSet rules = new RuleSet();
        rules.addRule(new Rule().left(RuleSet.START).right("E"));
        rules.addRule(new Rule().left("E").right("E", "*"));
        rules.addRule(new Rule().left("E").right("E", "|", "E"));
        rules.addRule(new Rule().left("E").right("(", "E", ")"));
        rules.addRule(new Rule().left("E").right("E", "E"));
        rules.addRule(new Rule().left("E").right("C"));
        
        ParserState engine = Parser.buildEngine(rules);
        assertEquals(
                "0 =>(() 1\n" + "0 =>(C) 2\n" + "0 =>(E) 3\n" +
                "1 =>(() 1\n" + "1 =>(C) 2\n" + "1 =>(E) 4\n" +
                "3 =>(() 1\n" + "3 =>(*) 5\n" + "3 =>(C) 2\n" + "3 =>(E) 6\n" + "3 =>(|) 7\n" +
                "4 =>(() 1\n" + "4 =>()) 8\n" + "4 =>(*) 5\n" + "4 =>(C) 2\n" + "4 =>(E) 6\n" + "4 =>(|) 7\n" +
                "6 =>(() 1\n" + "6 =>(*) 5\n" + "6 =>(C) 2\n" + "6 =>(E) 6\n" + "6 =>(|) 7\n" +
                "7 =>(() 1\n" + "7 =>(C) 2\n" + "7 =>(E) 9\n" +
                "9 =>(() 1\n" + "9 =>(*) 5\n" + "9 =>(C) 2\n" + "9 =>(E) 6\n" + "9 =>(|) 7\n",
                Helper.dump(engine));
        
    }
    
    
    @Test
    public void testBug1() {
        RuleSet rules = new RuleSet();
        // "S" has two transitions with the same input, which is implemented by adding a
        // bridge state. But the problem is this bridge state is an NFAState not a ParserState...
        Rule A = new Rule().left("S").right("A");
        Rule AS = new Rule().left("S").right("A", "S");
        rules.addRule(new Rule().left(RuleSet.START).right("S"));
        rules.addRule(A).addRule(AS);
        
        ParserState engine = Parser.buildEngine(rules);
        assertNotNull(engine.getState("A"));
        
        NFAState state = engine.getState("A");
        assertTrue(state instanceof ParserState);
        assertTrue(((ParserState)state).canReduce());
        assertEquals(A, ((ParserState)state).getRules()[0]);
    }

}
