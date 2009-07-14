package mini.java;

import java.util.LinkedList;
import java.util.List;

import mini.java.syntax.NonTerminal;
import mini.java.syntax.Symbol;
import mini.java.syntax.Terminal;

/**
 * A collection of helper functions for syntax analysis.
 *
 * @author Alex
 */
public final class TestHelperV3 {
    /**
     * Helper function used to create a symbol tree. It takes a string
     * representation of the symbol tree and build the tree recursively.
     * 
     * The string representation takes the form of: A(B,C,D(A,D)); in which the
     * parentheses specify the children symbols, and the children symbols are
     * sperated by the commas.
     */
    public static Symbol createSymbol(String rep_) {
        int idxOfLP = rep_.indexOf('(');
        int idxOfRP = rep_.indexOf(')');
        int idxOfComma = rep_.indexOf(',');
        
        // if there is a left parenthesis, and the parenthesis is
        // right after the symbol type (before commas, if there is any)
        if (idxOfLP > 0 && (idxOfComma < 0 || idxOfComma > idxOfLP)) {
            // this is a non-terminal symbol
            assert(idxOfRP > 0);
            
            String symbolType = rep_.substring(0, idxOfLP);
            List<Symbol> symbols = new LinkedList<Symbol>();
            
            // remove the symbol type from the string representation
            rep_ = rep_.substring(idxOfLP+1);
            assert(!rep_.isEmpty());
            
//            while (!rep_.isEmpty() && rep_.charAt(0)!=')') {
            while (rep_.charAt(0)!=')') {
                Symbol symbol = createSymbol(rep_);
                symbols.add(symbol);
                
                // remove the children symbols from the representation
                rep_ = rep_.substring(symbol.toString().length());
                assert(!rep_.isEmpty());
                
                // remove the extra comma
                if (rep_.charAt(0) == ',')
                    rep_ = rep_.substring(1);
                assert(!rep_.isEmpty());
            }            
//            assert(rep_.charAt(0) == ')');
//            rep_ = rep_.substring(1); // remove the trailing left parenthesis
            
            return new NonTerminal(symbolType, symbols);
        } else {
            // this is a terminal symbol
            if (idxOfComma < 0 && idxOfRP < 0) {
                // if either comma or RP doesn't exist
                return new Terminal(rep_);
            } else if (idxOfComma > 0 && idxOfRP > 0) {
                // if both exist, return the one with the smaller index
                return (idxOfComma < idxOfRP)
                    ? new Terminal(rep_.substring(0, idxOfComma))
                    : new Terminal(rep_.substring(0, idxOfRP));
            } else {
                // else return the one exists
                return (idxOfComma > idxOfRP)
                ? new Terminal(rep_.substring(0, idxOfComma))
                : new Terminal(rep_.substring(0, idxOfRP));
            }
        }
    }
}
