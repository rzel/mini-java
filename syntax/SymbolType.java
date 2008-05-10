package syntax;

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

}
