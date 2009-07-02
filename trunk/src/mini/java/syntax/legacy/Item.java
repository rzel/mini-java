package mini.java.syntax.legacy;
import java.util.List;

public class Item {
    private Rule rule;
    private int dotPos;

    public Item(Rule rule) {
        this(rule, 0);
    }

    public Item(Rule rule, int dotPos) {
        this.rule = rule;
        this.dotPos = dotPos;
    }

    public Rule getRule() { return rule; }
    public int getDotPos() { return dotPos; }
   
    public Item move() { return new Item(rule, dotPos + 1); }

    public TokenSpec getFollow() {
        List<TokenSpec> rhs = rule.getRhs();
        if(dotPos < rhs.size()) return rhs.get(dotPos);
        else return null;
    }
    public boolean hasFollow(){ 
    	   if(dotPos < rule.getRhs().size()) 
    		   return true;
           else return false;
    }
    public TokenSpec getPrevious() {
        List<TokenSpec> rhs = rule.getRhs();
        if(dotPos >0) return rhs.get(dotPos-1);
        else return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + dotPos;
        result = prime * result + ((rule == null) ? 0 : rule.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Item other = (Item) obj;
        if (dotPos != other.dotPos)
            return false;
        if (rule == null) {
            if (other.rule != null)
                return false;
        } else if (!rule.equals(other.rule))
            return false;
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(rule.getLhs().toString());
        sb.append(" :: =");
        for (int i=0; i<rule.getRhsLen(); ++i) {
            if (dotPos == i) {
                sb.append(" dot");
            }
            sb.append(' ');
            sb.append(rule.getRhs().get(i).toString());
        }
        return sb.toString();
    }
}

