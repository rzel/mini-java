package mini.java.syntax.legacy;
import java.util.*;



public class Rule {
    private TokenSpec lhs;	// TODO: should be TokenSpecSpec lhs!
    private List<TokenSpec> rhs;
    
    public Rule() {
        
    }
    public Rule(TokenSpec lhs) {
        this.lhs = lhs;
        this.rhs = new ArrayList<TokenSpec>();
    }

    public TokenSpec getLhs() {
        return lhs;
   
    }
    public List<TokenSpec> getRhs() {
        return rhs;
    }

    public int getRhsLen() {
        return rhs.size();
    }

    public void addRhsTokenSpec(TokenSpec t) {
        rhs.add(t);
    }
    public boolean nullable(){
    	if(rhs.get(0).equals(TokenSpec.EMPTY_TOKEN)){
    		return true;
    	}
    	return false;
    }
    public String toString() {
        return " Rule: " +lhs.toString() + " := " + rhs.toString();
    }
    
    public boolean equals(Object obj) {
        if(!(obj instanceof Rule)) return false;
        else {
            Rule rule = (Rule) obj;
            return rule.lhs.equals(this.lhs) && rule.rhs.equals(this.rhs);
        }
    }
}

