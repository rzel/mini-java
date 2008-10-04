package mini.java.syntax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HelperTest {
    
    @Test
    public void testCreateSymbol001() {
        // TEST:        Create a terminal symbol
        // INPUTS:      "A"
        Symbol symbol = Helper.createSymbol("A");
        assertNotNull(symbol);
        assertTrue(symbol instanceof Terminal);
    }
    
    @Test
    public void testCreateSymbol002() {
        // TEST:        Create a non terminal symbol
        // INPUTS:      "A"
        Symbol symbol = Helper.createSymbol("A(A)");
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
