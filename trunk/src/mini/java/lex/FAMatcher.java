package mini.java.lex;

import mini.java.fa.AcceptableNFAState;
import mini.java.fa.NFAState;

public class FAMatcher implements IMatcher {
    
    private final String _type;
    private final NFAState _fa;
    
    
    public FAMatcher(String type_, NFAState fa_) {
        _type = type_;
        _fa = fa_;
    }

    @Override
    public String getType() {
        return _type;
    }

    @Override
    public String match(String input_) {
        if (input_ == null) {
            return null;
        }
        NFAState curState = _fa;
        String match =
            (curState instanceof AcceptableNFAState)
                ? "" : null;
        StringBuilder buf = new StringBuilder();
        
        for (Character c : input_.toCharArray()) {
            buf.append(c);
            curState = curState.getState(c.toString());
            
            if (curState == null) {
                break;
            }
            
            if (curState instanceof AcceptableNFAState) {
                match = buf.toString();
            }
        }
        return match;
    }

}
