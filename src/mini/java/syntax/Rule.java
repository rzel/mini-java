package mini.java.syntax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import mini.java.fa.v3.State;


/**
 * Class Rule represents a production rule for a syntax specification. It
 * contains a right hand symbol and a list of left hand symbols. It also
 * maintains a collection of "items" for the corresponding left hand symbols.
 * 
 * @author Alex
 */
public class Rule {
    private static final String SYMBOL_SEPARATOR  = " ";
    private static final String ASSIGNMENT_SYMBOL = " ::= ";
    private String              _rightSymbol;
    private List<String>        _leftSymbols;
    private List<State>         _items;

    // private constructor
    private Rule(String rightSymbol_, List<String> leftSymbols_) {
        assert (rightSymbol_ != null && !rightSymbol_.isEmpty());
        assert (leftSymbols_ != null && leftSymbols_.size() > 0);

        _rightSymbol = rightSymbol_;
        // defensive copy to protect the immutable object
        _leftSymbols = new ArrayList<String>(leftSymbols_);
        
        // create the corresponding "items" for the left symbols;
        // "items" are represented by the NFA states, and will be used
        // to create the DFA of the syntax specification
        _items = new LinkedList<State>();
        for (int i=0; i<=_leftSymbols.size(); ++i) {
            _items.add(new State());
        }
        
//        // there should be an "END" item
//        _items.add(new AcceptableState());
    }

    public String getRightSymbol() {
        return _rightSymbol;
    }

    public List<String> getLeftSymbols() {
        return _leftSymbols;
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
                + ((_leftSymbols == null) ? 0 : _leftSymbols.hashCode());
        result = prime * result
                + ((_rightSymbol == null) ? 0 : _rightSymbol.hashCode());
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
        return rule._rightSymbol.equals(this._rightSymbol)
                && rule._leftSymbols.equals(this._leftSymbols);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(_rightSymbol);
        builder.append(ASSIGNMENT_SYMBOL);
        for (String leftSymbol : _leftSymbols) {
            builder.append(leftSymbol);
            builder.append(SYMBOL_SEPARATOR);
        }
        // remove the trailing SPACE
        builder.deleteCharAt(builder.length()-1);
        return builder.toString();
    }
}
