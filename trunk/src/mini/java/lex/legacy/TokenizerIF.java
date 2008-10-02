package mini.java.lex.legacy;

import java.util.List;

public interface TokenizerIF {
    public List<TokenRevamped> tokenize(String src);
}
