package mini.java.syntax;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class RuleCreateRuleTest {
    private String _rule;
    private boolean _isNull;
    
    // Constructor
    public RuleCreateRuleTest(String rule_, boolean isNull_) {
        _rule = rule_;
        _isNull = isNull_;
    }
    
    @Test
    public void testCreateRule() {
        Rule rule = Rule.createRule(_rule);
        if (_isNull) {            
            assertNull(rule);
        } else {
            // the representation should be the same
            assertEquals(rule.toString(), _rule);
        }
    }
    
    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
           {"A ::= B",          false},
           {"A ::= B C",        false},
           {" ::= B",           true},
           {"A ::=  ",          true},
           {"A B",              true},
           {"A ::= B ::= C",    true},
           {"",                 true},
           {null,               true},
        });
    }
}
