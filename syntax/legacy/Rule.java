package syntax.legacy;
import java.util.*;
import lex.legacy.Token;
import syntax.SymbolType;

class GenericRule<T> {
    private T lhs; //Oops! I almost forget that lhs should be NonTermianl
    private List<T> rhs;

    public GenericRule(T lhs) {
        this.lhs = lhs;
        //this.rhs = new ArrayList<Token>();
        rhs = new LinkedList<T>();
    }

    public T getLhs() {
        return lhs;
    }

    public List<T> getRhs() {
        return rhs;
    }

    public int getRhsLen() {
        return rhs.size();
    }

    public void addRhsToken(T t) {
        rhs.add(t);
    }

    public String toString() {
        //return "=== Rule ===\n" +
        //    " lhs: " + lhs.toString() + "\n" +
        //    " rhs: " + rhs.toString();
        String str = new String();
        str += lhs.toString() + " ::=";
        for (T type : rhs) {
            str += " " + type;
        }
        return str;
    }

    public boolean equals(Object obj) {
        if(!(obj instanceof Rule)) return false;
        else {
            Rule rule = (Rule) obj;
            return rule.getLhs().equals(this.lhs) && rule.getRhs().equals(this.rhs);
        }
    }
}

public class Rule extends GenericRule<SymbolType> {
    public Rule(SymbolType lhs) {
        super(lhs);
    }
}
