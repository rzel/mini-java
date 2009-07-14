package mini.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collection;

import mini.java.syntax.Symbol;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class TestHelperV3TestParameterized {
    private String _symbol;
    
    public TestHelperV3TestParameterized(String symbol_) {
        _symbol = symbol_;
    }
    
    @Test
    public void testCreateSymbol() {
        Symbol symbol = TestHelperV3.createSymbol(_symbol);
//        System.out.println(symbol.toString());
        
        assertNotNull(symbol);
        assertEquals(symbol.toString(), _symbol);
    }
    
    @Parameters
    public static Collection<Object[]> getParameters() {
        return Arrays.asList(new Object[][] {
           {"A"},
           {"A(B)"},
           {"A(B,C)"},
           {"A(B,C,D)"},
           {"A(B(E))"},
           {"A(B(E),C(E))"},
           {"A(B(E),C(E),D(E))"},
           {"A(B,C(E))"},
           {"A(B,C(E),D)"},
           {"A(B,C(E),D(E))"},
           {"A(B(C(E)))"},
           {"A(B(C(E),D(E)))"},
           {"A(B(C(D(E))))"},
        });
    }
}
