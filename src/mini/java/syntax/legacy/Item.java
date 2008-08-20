package mini.java.syntax.legacy;
import java.util.*;

import mini.java.syntax.SymbolType;

//import lex.legacy.Token;

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

    public SymbolType getFollow() {
        List<SymbolType> rhs = rule.getRhs();
        if(dotPos < rhs.size()) return rhs.get(dotPos);
        else return null;
    }
    public SymbolType getPrevious() {
        List<SymbolType> rhs = rule.getRhs();
        if(dotPos >0) return rhs.get(dotPos-1);
        else return null;
    }

    public void dump() {
        System.out.println("Rule: " + rule);
        System.out.println("DotPos: " + dotPos);
    }

    public String toString() {
        String str = new String();
        str += rule.getLhs().toString() + " ::=";
//        for (int i=0; i<dotPos; ++i) {
//            str += " " + rule.getRhs().get(i).toString();
//        }
//        str += " *";
//        for (int i=dotPos; i<rule.getRhs().size(); ++i) {
//            str += " " + rule.getRhs().get(i).toString();
//        }

        int i = 0;
        for (SymbolType type : rule.getRhs()) {
            if (i == dotPos)
                str += " *";
            str += " " + type.toString();
            ++i;
        }
        if (str.indexOf('*') == -1)
            str += " *";
        return str;
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

