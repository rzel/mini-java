package mini.java.lex.legacy;

import java.util.List;
//import java.util.Set;

public interface LexConfig {
    //public Set<TokenType> getTokenTypes();
    public List<TokenType> getTokenTypes();
    public String getTokenSpec(TokenType type);
}
