package mini.java.lex;

import java.io.Reader;
import java.util.List;

import mini.java.syntax.Symbol;

public interface Tokenizer {
    /**
     * Builds a token list from the given input stream.
     * NOTE: Tokens are represented by terminal symbols. The actual string
     * will be stored as "data" inside the symbol objects.
     */
    public List<Symbol> tokenize(Reader reader_);
}
