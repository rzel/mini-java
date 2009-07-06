package mini.java.lex;


/**
 * Contains token specifications and matchers.
 */
public interface ITokenizerConfig {
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
}
