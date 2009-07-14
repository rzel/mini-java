package mini.java.syntax;

public class SymbolDumper implements SymbolVisitor {
    
    private final StringBuilder _sb = new StringBuilder();

    @Override
    public void visitNonTerminal(NonTerminal nonTerminal_) {
        _sb.append(nonTerminal_.getType());
        _sb.append('(');
        for (Symbol symbol : nonTerminal_.getChildren()) {
            symbol.accept(this);
            _sb.append(',');
            _sb.append(' ');
        }
        // remove the trailing ", "
        _sb.deleteCharAt(_sb.length() - 1);
        _sb.deleteCharAt(_sb.length() - 1);
        _sb.append(')');
    }

    @Override
    public void visitTerminal(Terminal terminal_) {
        // TODO: move the string rep to Terminal.toString();
        _sb.append(terminal_.getType() + "<" + terminal_.getData() + ">");
    }

    @Override
    public String toString() {
        return _sb.toString();
    }

    
}
