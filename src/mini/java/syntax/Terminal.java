package mini.java.syntax;

public final class Terminal extends Symbol {
    private String _data;
    
    // Constructor.
    public Terminal(String type_, String data_) {
        super(type_);
        _data = data_;
    }
    
    // Constructor.
    public Terminal(String type_) {
        this(type_, null);
    }
    
    public String getData() {
        return _data;
    }

    @Override
    public void accept(SymbolVisitor visitor_) {
        visitor_.visitTerminal(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((_data == null) ? 0 : _data.hashCode());
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
        Terminal other = (Terminal) obj;
        if (_data == null) {
            if (other._data != null)
                return false;
        } else if (!_data.equals(other._data))
            return false;
        return true;
    }

//    @Override
//    public String toString() {
//        return "Terminal<" + getType() + ", " + _data + ">";
//    }

}
