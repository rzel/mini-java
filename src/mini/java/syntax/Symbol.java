package mini.java.syntax;

public abstract class Symbol {
    private String _type;

    // Constructor.
    public Symbol(String type_) {
        _type = type_;
    }

    /**
     * Returns the symbol type. Don't be confused with the symbol type of
     * Terminal or NonTerminal. The type is specific to the AST and will be used
     * in the Visitor to differentiate different Terminals or NonTerminals.
     * 
     * @return A string represents the symbol type.
     */
    public String getType() {
        return _type;
    }

    /**
     * Accepts a visitor to apply certain operations on the AST.
     */
    public abstract void accept(SymbolVisitor visitor_);

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_type == null) ? 0 : _type.hashCode());
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
        if (_type == null) {
            if (other._type != null)
                return false;
        } else if (!_type.equals(other._type))
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
        return _type;
    }
}
