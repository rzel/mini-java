package mini.java.syntax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import mini.java.fa.helper.Helper;

import org.junit.Test;


public class ParserConfigV2ImplTest {
    
    @Test(expected=RuntimeException.class)
    public void testReduceReduceConflict() {
        RuleSet rules = new RuleSet();
        rules.addRule(new Rule().left(RuleSet.START).right("S"));
        rules.addRule(new Rule().left("S").right("A"));
        rules.addRule(new Rule().left("S").right("B"));
        rules.addRule(new Rule().left("A").right("1"));
        rules.addRule(new Rule().left("B").right("1"));
        
        new ParserConfigV2Impl(rules);        
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
        
        ParserConfigV2Impl conf = new ParserConfigV2Impl(rules);
        ParserState engine = conf.getEngine();
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
    
}
