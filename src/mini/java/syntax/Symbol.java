package mini.java.syntax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Symbol represents a node in an Abstract Syntax Tree. Each symbol can have any number of
 * children. Symbols with no children are called Terminals or Tokens.
 *
 * @author Alex
 */
public class Symbol {
    private String       _symbolType;
    private Object       _data;
    private List<Symbol> _children;
    
    /**
     * Constructor for Terminals.
     */
    public Symbol(String symbolType_, Object data_) {
        this(symbolType_, data_, new ArrayList<Symbol>());
    }
    
    /**
     * Constructor for Non-terminals.
     */
    public Symbol(String symbolType_, Object data_, List<Symbol> children_) {
        _symbolType     = symbolType_;
        _data           = data_;
        // defensive copy to protect the immutable object
        _children       = new ArrayList<Symbol>(children_);
        
        assert(_symbolType != null);
        assert(_children   != null);
    }
    
    public String getSymbolType() {
        return _symbolType;
    }
    
    public Object getData() {
        return _data;
    }
    
    public List<Symbol> getChildren() {
        return Collections.unmodifiableList(_children);
    }
    
    /**
     * Helper function used to create the string representation of the
     * symbol and the its children symbols. See createSymbol() for detail.
     */    
    @Override
    public String toString() {
        if (_children.size() == 0) {
            // this is a terminal symbol
            return _symbolType;
        } else {
            // this is a non-terminal symbol
            StringBuilder builder = new StringBuilder();
            for (Symbol child : _children) {
                builder.append(child);
                builder.append(',');
            }
            // remove the tailing comma
            builder.deleteCharAt(builder.length()-1);
            return String.format("%s(%s)", _symbolType, builder);
        }
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
