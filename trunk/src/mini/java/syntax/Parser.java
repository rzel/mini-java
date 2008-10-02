package mini.java.syntax;

import java.util.List;

public interface Parser {
    /**
     * Builds an Abstract Syntax Tree from the list of tokens.
     * NOTE: Both non-terminals and terminals are represented by Symbol.
     * And tokens are also treated as Symbol since they are actually terminals. 
     */
    public Symbol parse(List<Symbol> symbols_);
}
