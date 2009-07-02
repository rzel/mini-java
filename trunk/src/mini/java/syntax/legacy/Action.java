package mini.java.syntax.legacy;



public class Action {
    public static final int ACTION_SHIFT = 0;
    public static final int ACTION_REDUCE = 1;
    public static final int ACTION_GOTO = 2;
    public static final int ACTION_ACCT = 3;    /* accept */
    
    public static final String[] REP = new String[] {
        "SHIFT", "REDUCE", "GOTO", "ACCT"
    };   

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((rule == null) ? 0 : rule.hashCode());
        result = prime * result + ((state == null) ? 0 : state.hashCode());
        result = prime * result + type;
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
        final Action other = (Action) obj;
        if (rule == null) {
            if (other.rule != null)
                return false;
        } else if (!rule.equals(other.rule))
            return false;
        if (state == null) {
            if (other.state != null)
                return false;
        } else if (!state.equals(other.state))
            return false;
        if (type != other.type)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "" + REP[type] + "(" + state + ", " + rule + ")";
    }
}

