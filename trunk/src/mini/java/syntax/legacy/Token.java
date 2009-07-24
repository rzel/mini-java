package mini.java.syntax.legacy;

public class Token {
    
    private final String _type;
    private final String _data;
    
    public Token(String type_) {
        this(type_, null);
    }
    
    public Token(String type_, String data_) {
        _type = type_;
        _data = data_;
    }
    
    public Token(String type_, String data_, int i_, int j_) {
        this(type_, data_);
    }
    
    public String getData() {
        return _data;
    }
    
    public String getType() {
        return _type;
    }

    @Override
    public String toString() {
        return String.format("%s<%s>", _type, _data);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_data == null) ? 0 : _data.hashCode());
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
        final Token other = (Token) obj;
        if (_data == null) {
            if (other._data != null)
                return false;
        } else if (!_data.equals(other._data))
            return false;
        if (_type == null) {
            if (other._type != null)
                return false;
        } else if (!_type.equals(other._type))
            return false;
        return true;
    }

    public String getText() {
        return getData();
    }

    public int getLineNum() {
        throw new UnsupportedOperationException();
    }
    
    
}
