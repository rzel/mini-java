package mini.java.syntax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class HelperTest {
    
    @Test
    public void testCreateSymbolSingleSymbol() {
        Symbol symbol = Helper.createSymbol("A");
        assertNotNull(symbol);
        assertNotNull(symbol.getChildren());
        assertEquals(0, symbol.getChildren().size());
    }
    
    @Test
    public void testCreateSymbolSingleChild() {
        Symbol symbol = Helper.createSymbol("A(A)");
        assertNotNull(symbol);
        assertNotNull(symbol.getChildren());
        assertEquals(1, symbol.getChildren().size());
    }

}
