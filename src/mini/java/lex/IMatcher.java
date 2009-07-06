package mini.java.lex;

/**
 * Matchers serve as the actual specifications for tokens. It
 * provides method for extracting tokens from input strings.
 *
 * @author Alex
 */
public interface IMatcher {
    /**
     * Returns the matching substring at the beginning of the input.
     * Null if no matching.
     */
    public String match(String input_);
    
    /**
     * Returns the token type supported by the matcher.
     */
    public String getType();
}
