package mini.java.lex;

public class CharRangeMatcher implements IMatcher {
    
    private final String _type;
    private final char _start, _end;
    
    public CharRangeMatcher(String type_, char start_, char end_) {
        _type = type_;
        _start = start_;
        _end = end_;
    }
    

    @Override
    public String getType() {
        return _type;
    }

    @Override
    public String match(String input_) {
        if (input_.length() <= 0) {
            return null;
        }
        
        char ch = input_.charAt(0);
        if (ch >= _start && ch <= _end)
        {
            return "" + ch;
        }
        else
        {
            return null;
        }
    }

}
