package mini.java.syntax.legacy;

import misc.Type;

public abstract class SymbolType extends Type {
    public SymbolType(String rep) {
        super(rep);
    }
    public SymbolType() {
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if ((o == null) || (o.getClass() != this.getClass())) return false;
        return getRep().equals(((SymbolType) o).getRep());
    }

    public int hashCode() {
        return getRep().hashCode();
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

}
