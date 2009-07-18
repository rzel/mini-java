package mini.java.lex;

import mini.java.syntax.Terminal;

public interface ITokenizer {
    /**
     * Breaks the input string into pieces.
     */
    public Terminal[] tokenize(String input_);
    
    /**
     * Returns all token types supported.
     */
    public String[] getTokenTypes();
    
    /**
     * Returns matchers for each of the token types.
     */
    public IMatcher[] getMatchers();
    
    
    
    /**
     * Helper method: GetMatcherByType()
     */
    public IMatcher getMatcher(String type_);
    
    /**
     * Adds a matcher into the tokenizer. Order matters.
     */
    public void addMatcher(IMatcher matcher_);
}
