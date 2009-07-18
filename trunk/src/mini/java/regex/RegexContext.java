package mini.java.regex;

import mini.java.fa.NFAState;
import mini.java.syntax.Rule.IContext;

public class RegexContext implements IContext {
    
    private final NFAState _head, _tail;
    
    public RegexContext(NFAState head_, NFAState tail_) {
        _head = head_;
        _tail = tail_;
    }
    
    public RegexContext() {
        this(new NFAState(), new NFAState());
    }
    
    public NFAState getHead() {
        return _head;
    }
    
    public NFAState getTail() {
        return _tail;
    }
    
    /**
     * Helper method.
     */
    public void linkHead(NFAState next_) {
        getHead().addTransition(next_);
    }
    
    public void linkTail(NFAState next_) {
        getTail().addTransition(next_);
    }

}
