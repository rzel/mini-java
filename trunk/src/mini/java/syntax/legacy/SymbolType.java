package mini.java.syntax.legacy;


public abstract class SymbolType {
    
    private final String _rep;
    
    public SymbolType(String rep_) {
        _rep = rep_;
    }
    public SymbolType() {
        _rep = null;
    }
    
    public String getRep() {
        return _rep;
    }

    public String toString() {
        return getRep();
    }
    
    /**
     * Converts TokenSpec to SymbolType.
     */
    public static SymbolType createSymbol(TokenSpec spec_) {
        if (spec_.getType().equals(TokenSpec.NON_TERMINAL_TYPE)) {
            return new NonTerminal(spec_.getText());
        }
        else if (spec_.getType().equals(TokenSpec.TERMINAL_TYPE)) {
            return new Terminal(spec_.getText());
        }
        else {
            throw new IllegalArgumentException("Unknown token/symbol type: " + spec_.getType());
        }
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_rep == null) ? 0 : _rep.hashCode());
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
        final SymbolType other = (SymbolType) obj;
        if (_rep == null) {
            if (other._rep != null)
                return false;
        } else if (!_rep.equals(other._rep))
            return false;
        return true;
    }

    
}
