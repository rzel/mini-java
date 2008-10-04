package mini.java.syntax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class NonTerminal extends Symbol {
    private List<Symbol> _children;
    
    // Constructor.
    public NonTerminal(String type_, List<Symbol> children_) {
        super(type_);
        // NOTE: defensive copy
        _children = new ArrayList<Symbol>(children_);
    }
    
    public List<Symbol> getChildren() {
        return Collections.unmodifiableList(_children);
    }

    @Override
    public void accept(SymbolVisitor visitor_) {
        visitor_.visitNonTerminal(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((_children == null) ? 0 : _children.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        NonTerminal other = (NonTerminal) obj;
        if (_children == null) {
            if (other._children != null)
                return false;
        } else if (!_children.equals(other._children))
            return false;
        return true;
    }
    

    /**
     * Helper function used to create the string representation of the symbol
     * and the its children symbols. See createSymbol() for detail.
     */
    @Override
    public String toString() {
        // TODO should use SymbolVisitorImpl
        StringBuilder builder = new StringBuilder();
        for (Symbol child : _children) {
            builder.append(child);
            builder.append(',');
        }
        // remove the tailing comma
        builder.deleteCharAt(builder.length() - 1);
        return String.format("%s(%s)", super.toString(), builder);
    }

}
