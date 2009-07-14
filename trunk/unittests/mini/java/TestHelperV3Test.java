package mini.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import mini.java.syntax.NonTerminal;
import mini.java.syntax.Symbol;
import mini.java.syntax.Terminal;

import org.junit.Test;

public class TestHelperV3Test {
    
    @Test
    public void testCreateSymbol001() {
        // TEST:        Create a terminal symbol
        // INPUTS:      "A"
        Symbol symbol = TestHelperV3.createSymbol("A");
        assertNotNull(symbol);
        assertTrue(symbol instanceof Terminal);
    }
    
    @Test
    public void testCreateSymbol002() {
        // TEST:        Create a non terminal symbol
        // INPUTS:      "A"
        Symbol symbol = TestHelperV3.createSymbol("A(A)");
        assertNotNull(symbol);
        assertTrue(symbol instanceof NonTerminal);
        
        NonTerminal nonTerminal = (NonTerminal)symbol;
        assertNotNull(nonTerminal.getChildren());
        assertEquals(1, nonTerminal.getChildren().size());
        
        Symbol terminal = nonTerminal.getChildren().get(0);
        assertNotNull(terminal);
        assertTrue(terminal instanceof Terminal);
    }

}
