package mini.java.syntax;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Class Rule represents a production rule for a syntax specification. It contains a
 * right hand symbol and a list of left hand symbols.
 * NOTE: Rules are immutable objects.
 * 
 * @author Alex
 */
public class Rule {    
    private static final String SYMBOL_SEPARATOR = " ";
    private static final String ASSIGNMENT_SYMBOL = " ::= ";
    private String _rightSymbol;
    private List<String> _leftSymbols;
    
    // private constructor
    private Rule() {        
    }
    
    public String getRightSymbol() {
        return _rightSymbol;
    }
    
    public List<String> getLeftSymbols() {
        return _leftSymbols;
    }
    
    /**
     * Factory method used to create Rule instances. This method accepts a line
     * of string and returns the corresponding Rule instance represented by the string.
     * 
     * Rule specification should look like this:
     * RightSymbol ::= LeftSymbolA LeftSymbolB LeftSymbolC ...
     * NOTE: left symbols should be seperated by single spaces.
     */
    public static Rule parse(String spec_) {
        if (spec_ == null || spec_.isEmpty()) return null;
        if (spec_.indexOf(ASSIGNMENT_SYMBOL) < 0) return null;
        
        // split the rule spec into right and left
        String[] parts = spec_.split(ASSIGNMENT_SYMBOL);
        if (parts.length != 2) return null;
        
        // check symbols
        String rightSymbol = parts[0];
        String[] leftSymbols = parts[1].split(SYMBOL_SEPARATOR);        
        if (rightSymbol.isEmpty()) return null;
        if (leftSymbols.length < 1) return null;
        
        // create the new rule instance
        Rule rule = new Rule();
        rule._rightSymbol = rightSymbol;
        rule._leftSymbols = Arrays.asList(leftSymbols);
        return rule;
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
            .append(_rightSymbol)
            .append(_leftSymbols)
            .toHashCode();
    }

    @Override
    public boolean equals(Object o_) {
        if (o_ == this) return true;
        if (o_ == null) return false;
        if (o_.getClass() != this.getClass()) return false;
        Rule rule = (Rule) o_;
        return new EqualsBuilder()
            .append(rule._rightSymbol, this._rightSymbol)
            .append(rule._leftSymbols, this._leftSymbols)
            .isEquals();
    }
}
