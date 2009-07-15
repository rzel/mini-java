package mini.java.syntax;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;


public class ParserV2ImplTest {
    
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
        NonTerminal got = new ParserV2Impl(rules).parse(tokens);
        
        SymbolDumper dumper = new SymbolDumper();
        got.accept(dumper);
        assertEquals("START(E(1<1>, E(1<1>)))", dumper.toString());
    }
    
    
    @Test
    @Ignore
    public void testPrecedence() {
        RuleSet rules = new RuleSet();
        rules.addRule(new Rule().left(RuleSet.START).right("E"));
        rules.addRule(new Rule().left("E").right("E", "|", "E"));
        rules.addRule(new Rule().left("E").right("C"));
        
        {
            List<Terminal> tokens = new LinkedList<Terminal>();
            for (Character c : "C|CC".toCharArray()) {
                tokens.add(new Terminal(c.toString()));
            }
        }
    }
    
    @Test
    @Ignore("TODO")
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
            
            final StringBuilder sb = new StringBuilder();
            NonTerminal ret = new ParserV2Impl(rules).parse(tokens.toArray(new Terminal[0]));
            
            ret.accept(new SymbolVisitor() {

                @Override
                public void visitNonTerminal(NonTerminal nonTerminal_) {
                    sb.append(nonTerminal_.getType());
                    sb.append('(');
                    for (Symbol symbol : nonTerminal_.getChildren()) {
                        symbol.accept(this);
                        sb.append(',');
                        sb.append(' ');
                    }
                    // remove the trailing ", "
                    sb.deleteCharAt(sb.length() - 1);
                    sb.deleteCharAt(sb.length() - 1);
                    sb.append(')');
                    
                }

                @Override
                public void visitTerminal(Terminal terminal_) {
                    sb.append(terminal_.getType());                    
                }
                
            });
            
            assertEquals("START(E(E(E((,E(C),)),*),|,E(E(C),E(C))))", sb.toString());
        }
    }

}
