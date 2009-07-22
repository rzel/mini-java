package mini.java.lex;

public class CharLiteralMatcher implements IMatcher {
    
    private final String _type;
    private final char _ch;
    
    public CharLiteralMatcher(String type_, char ch_) {
        _type = type_;
        _ch = ch_;
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
        if (ch == _ch)
        {
            return "" + ch;
        }
        else
        {
            return null;
        }
    }

}
