package mini.java.syntax.legacy;

import mini.java.fa.v3.State;

public class Action {
    public static final int ACTION_SHIFT = 0;
    public static final int ACTION_REDUCE = 1;
    public static final int ACTION_GOTO = 2;
    public static final int ACTION_ACCT = 3;    /* accept */

    /* TODO: need rethinking!!! */
    private int type;
    private State state;
    private Rule rule;

    public Action(int type, State state) {
        this(type, state, null);
    }

    public Action(int type, Rule rule) {
        this(type, null, rule);
    }

    public Action(int type) {
        this(type, null, null);
    }

    public Action(int type, State state, Rule rule) {
        if(type != ACTION_SHIFT &&
           type != ACTION_REDUCE &&
           type != ACTION_GOTO &&
           type != ACTION_ACCT)
           
            throw new RuntimeException("Unknown Action");
        this.type = type;
        this.state = state;
        this.rule = rule;
    }

    public State getState() {
        return state;
    }

    public Rule getRule() {
        return rule;
    }

    public int getType() {
        return type;
    }
    
//    public String toString() {
//    	if(type == Action.ACTION_ACCT) {
//    		return "ACCEPT";
//    	} else if(type == Action.ACTION_GOTO) {
//    		return "GOTO " + state.getId();
//    	} else if(type == Action.ACTION_SHIFT) {
//    		return "SHIFT " + state.getId();
//    	} else {
//    		return "REDUCE " + rule.getLhs() + "->" + rule.getRhs();
//    	}
//    }
}

