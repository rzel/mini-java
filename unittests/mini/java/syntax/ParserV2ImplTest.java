package mini.java.syntax;

import static org.junit.Assert.assertEquals;

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

}
