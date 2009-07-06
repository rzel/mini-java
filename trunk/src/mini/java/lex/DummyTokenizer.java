package mini.java.lex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import mini.java.syntax.Terminal;

/**
 * Helper class used for testing. It takes a list of symbol types and creates
 * the corresponding symbol objects for those symbol types. <b>NOTE:</b> Symbol
 * types should be sperated by single SPACE.
 * 
 * @author Alex
 */
public class DummyTokenizer implements ITokenizer {
    public static final String SPACE = " ";

    
    public List<Terminal> tokenize(Reader reader_) {
        List<Terminal> symbols = new LinkedList<Terminal>();
        BufferedReader reader = new BufferedReader(reader_);
        
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                for (String symbolType : line.split(SPACE)) {
                    symbols.add(new Terminal(symbolType));
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
    @Override
    public Terminal[] tokenize(String string_) {
        return tokenize(new StringReader(string_)).toArray(new Terminal[0]);
    }
}
