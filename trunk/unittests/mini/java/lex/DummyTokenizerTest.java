package mini.java.lex;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import mini.java.syntax.Symbol;

import org.junit.Test;

public class DummyTokenizerTest {
    private static String[] _symbols = { "A", "B", "C", "D" };

    @Test
    public final void testTokenize() throws IOException {
        StringBuilder builder = new StringBuilder();
        for (String symbol : _symbols) {
            builder.append(symbol);
            builder.append(DummyTokenizer.SPACE);
        }
        
//        Tokenizer tokenizer = new DummyTokenizer();
//        List<Symbol> symbols = tokenizer.tokenize(new StringReader(builder.toString()));
        List<Symbol> symbols = new DummyTokenizer().tokenize(builder.toString());
        
        assertEquals(symbols.size(), _symbols.length);
        for (int i=0; i<_symbols.length; ++i) {
            String got = symbols.get(i).getSymbolType();
            String expected = _symbols[i];
            assertEquals(got, expected);
        }
    }

}
