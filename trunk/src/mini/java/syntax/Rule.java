package mini.java.syntax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import mini.java.fa.v3.State;


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
    public interface IRuleHandler<T> {
        /**
         * Handles the symbol(tree) based on the context obj.
         */
        public void handle(Symbol sym_, T ctx_);
    }
    
    private static final String SYMBOL_SEPARATOR  = " ";
    private static final String ASSIGNMENT_SYMBOL = " ::= ";
    private String              _leftSymbol;
    private List<String>        _rightSymbols;
    private List<State>         _items;
    private IRuleHandler<?>     _handler;
    
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
    
    
    public Rule addHandler(IRuleHandler<?> handler_) {
        _handler = handler_;
        return this; // builder...
    }
    
    public IRuleHandler<?> getHandler() {
        return _handler;
    }

    // private constructor
    private Rule(String leftSymbol_, List<String> rightSymbols_) {
        assert (leftSymbol_ != null && !leftSymbol_.isEmpty());
        assert (rightSymbols_ != null && rightSymbols_.size() > 0);

        _leftSymbol = leftSymbol_;
        // defensive copy to protect the immutable object
        _rightSymbols = new ArrayList<String>(rightSymbols_);
        
        // create the corresponding "items" for the left symbols;
        // "items" are represented by the NFA states, and will be used
        // to create the DFA of the syntax specification
        _items = new LinkedList<State>();
        for (int i=0; i<=_rightSymbols.size(); ++i) {
            _items.add(new State());
        }
        
//        // there should be an "END" item
//        _items.add(new AcceptableState());
        
        _parent = null;
    }

    public String getLeftSymbol() {
        return _leftSymbol;
    }

    public List<String> getRightSymbols() {
        return _rightSymbols;
    }
    
    public List<State> getItems() {
        return _items;
    }

    /**
     * Factory method used to create Rule instances. This method accepts a line
     * of string and returns the corresponding Rule instance represented by the
     * string.
     * 
     * Rule specification should look like this: RightSymbol ::= LeftSymbolA
     * LeftSymbolB LeftSymbolC ... NOTE: left symbols should be seperated by
     * single spaces.
     */
    public static Rule createRule(String spec_) {
        if (spec_ == null || spec_.isEmpty())
            return null;
        if (spec_.indexOf(ASSIGNMENT_SYMBOL) < 0)
            return null;

        // split the rule spec into right and left
        String[] parts = spec_.split(ASSIGNMENT_SYMBOL);
        if (parts.length != 2)
            return null;

        // check symbols
        String rightSymbol = parts[0];
        String[] leftSymbols = parts[1].split(SYMBOL_SEPARATOR);
        if (rightSymbol.isEmpty())
            return null;
        if (leftSymbols.length < 1)
            return null;

        // create and return the new rule instance
        return new Rule(rightSymbol, Arrays.asList(leftSymbols));
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
    @SuppressWarnings("unchecked")
    public Set<String> getFollows() {
        return (_parent != null)
            ? _parent.getFollows(getLeftSymbol()) : Collections.EMPTY_SET;
    }
}
