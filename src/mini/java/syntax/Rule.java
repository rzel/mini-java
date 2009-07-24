package mini.java.syntax;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * Class Rule represents a production rule for a syntax specification. It
 * contains a right hand symbol and a list of left hand symbols. It also
 * maintains a collection of "items" for the corresponding left hand symbols.
 * 
 * @author Alex
 */
public class Rule {
    /**
     * Used to process the symbol tree corresponding to the specific rule.
     */
    public interface IRuleHandler {
        /**
         * Handles the symbol(tree) based on the context obj.
         */
        public void handle(Symbol sym_, Object ctx_);
    }
    
    private static final String SYMBOL_SEPARATOR  = " ";
    private static final String ASSIGNMENT_SYMBOL = " ::= ";
    private String              _leftSymbol;
    private List<String>        _rightSymbols;
    private IRuleHandler        _handler;
    
    private RuleSet       _parent;
    
    // to be used with the builder interface
    public Rule() {
        _rightSymbols = new LinkedList<String>();
    }
    
    public void setParent(RuleSet parent_) {
        _parent = parent_;
    }
    
    public RuleSet getParent() {
        return _parent;
    }
    
    public Rule addLeftSymbol(String leftSymbol_) {
        _leftSymbol = leftSymbol_;
        return this; // builder...
    }
    
    public Rule addRightSymbols(String... rightSymbols_) {
        _rightSymbols.addAll(Arrays.asList(rightSymbols_));
        // builder...
        return this;
    }
    
    // alias
    public Rule left(String leftSymbol_) {
        return addLeftSymbol(leftSymbol_);
    }    
    public Rule right(String... rightSymbols_) {
        return addRightSymbols(rightSymbols_);
    }
    
    
    public Rule addHandler(IRuleHandler handler_) {
        _handler = handler_;
        return this; // builder...
    }
    
    public IRuleHandler getHandler() {
        return _handler;
    }

    public String getLeftSymbol() {
        return _leftSymbol;
    }

    public List<String> getRightSymbols() {
        return _rightSymbols;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((_rightSymbols == null) ? 0 : _rightSymbols.hashCode());
        result = prime * result
                + ((_leftSymbol == null) ? 0 : _leftSymbol.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object o_) {
        if (o_ == this)
            return true;
        if (o_ == null)
            return false;
        if (o_.getClass() != this.getClass())
            return false;
        Rule rule = (Rule) o_;
        return rule._leftSymbol.equals(this._leftSymbol)
                && rule._rightSymbols.equals(this._rightSymbols);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(_leftSymbol);
        builder.append(ASSIGNMENT_SYMBOL);
        for (String leftSymbol : _rightSymbols) {
            builder.append(leftSymbol);
            builder.append(SYMBOL_SEPARATOR);
        }
        // remove the trailing SPACE
        builder.deleteCharAt(builder.length()-1);
        return builder.toString();
    }
    
    /**
     * Helper method. Get the following symbols for this rule.
     */
    public Set<String> getFollows() {
        Set<String> ret = Collections.emptySet();
        if (_parent != null) {
            ret = _parent.getFollows(getLeftSymbol());
        }

        return ret;
    }
}
