package mini.java.lex;

import mini.java.syntax.Terminal;

public interface ITokenizer {
    /**
     * Extracts tokens from the given input string.
     */
    public Terminal[] tokenize(String input_);
}
