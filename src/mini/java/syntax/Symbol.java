package mini.java.syntax;

import java.util.Collections;
import java.util.List;

/**
 * Symbol represents a node in an Abstract Syntax Tree. Each symbol can have any number of
 * children. Symbols with no children are called Terminals or Tokens.
 *
 * @author Alex
 */
public class Symbol {
    private String _symbolType;
    private List<Symbol> _children;
    
    /**
     * Constructor for Terminals.
     */
    public Symbol(String symbolType_) {
        _symbolType = symbolType_;
        _children = Collections.emptyList();
        
        assert(_symbolType != null);
        assert(_children   != null);
    }
    
    /**
     * Constructor for Non-terminals.
     */
    public Symbol(String symbolType_, List<Symbol> children_) {
        _symbolType = symbolType_;
        _children = children_;
        
        assert(_symbolType != null);
        assert(_children   != null);
    }
    
    public String getSymbolType() {
        return _symbolType;
    }
    
    public List<Symbol> getChildren() {
        return Collections.unmodifiableList(_children);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((_children == null) ? 0 : _children.hashCode());
        result = prime * result
                + ((_symbolType == null) ? 0 : _symbolType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Symbol other = (Symbol) obj;
        if (_children == null) {
            if (other._children != null)
                return false;
        } else if (!_children.equals(other._children))
            return false;
        if (_symbolType == null) {
            if (other._symbolType != null)
                return false;
        } else if (!_symbolType.equals(other._symbolType))
            return false;
        return true;
    }
}
