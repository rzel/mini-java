package mini.java.lex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import mini.java.syntax.Symbol;

/**
 * Helper class used for testing. It takes a list of symbol types and creates
 * the corresponding symbol objects for those symbol types. <b>NOTE:</b> Symbol
 * types should be sperated by single SPACE.
 * 
 * @author Alex
 */
public class DummyTokenizer implements Tokenizer {
    public static final String SPACE = " ";

    @Override
    public List<Symbol> tokenize(Reader reader_) {
        List<Symbol> symbols = new LinkedList<Symbol>();
        BufferedReader reader = new BufferedReader(reader_);
        
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                for (String symbolType : line.split(SPACE)) {
                    symbols.add(new Symbol(symbolType, null));
                }
            }
        } catch (IOException ex_) {
            // DO NOTHING HERE
        }
        
        return symbols;
    }
    
    /**
     * Helper method that will wrap the input string with a
     * StringReader. This method will call the actual tokenizer(Reader)
     * to get the result token list.
     */
    public List<Symbol> tokenize(String string_) {
        return tokenize(new StringReader(string_));
    }
}
