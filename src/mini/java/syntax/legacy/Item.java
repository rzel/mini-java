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

    public void dump() {
        System.out.println("Rule: " + rule);
        System.out.println("DotPos: " + dotPos);
    }

    public boolean equals(Object obj) {
        if(!(obj instanceof Item)) return false;
        else {
            Item item = (Item) obj;
            return item.rule.equals(this.rule) &&
                item.dotPos == this.dotPos;
        }
    }
}

