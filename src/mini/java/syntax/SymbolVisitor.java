package mini.java.syntax;

public interface SymbolVisitor {
    /**
     * Visits a terminal symbol.
     */
    public void visitTerminal(Terminal terminal_);
    
    /**
     * Visits a non terminal symbol. <b>NOTE:</b> The AST traversal algorithm should be
     * put here, rather than in the NonTerminal class. Because different operations
     * may need different traversal algorithms
     */
    public void visitNonTerminal(NonTerminal nonTerminal_);
}
