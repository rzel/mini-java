package mini.java.syntax;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import mini.java.fa.NFAState;

public class ParserState extends NFAState {
    // fields
    private final NFAState          _root;
    private final Map<String, Rule> _rules = new HashMap<String, Rule>();
//    private RuleSet           _ruleSet;
    
    private ParserState() {
        _root = this; // mark self as root
    }
    
    /**
     * Factory method to create a new NFA.
     */
    public static ParserState createRoot() {
        return new ParserState();
    }
    
    public ParserState(NFAState root_) {
        _root = root_;
    }
    
    public ParserState addRule(Rule rule_) {

        for (String lookahead : rule_.getFollows()) {
            Rule old = _rules.put(lookahead, rule_);
            if (old != null) {
                throw new RuntimeException(
                        String.format("The given rule set cannot be handled by LR(1): " +
                                "<%s> and <%s> have the same lookahead <%s>", old, rule_, lookahead));
            }
        }
        return this; // builder...
    }
    
    public ParserState addRules(Rule... rules_) {
        for (Rule rule : rules_) {
            addRule(rule);
        }
        return this; // builder...
    }
    
    public Rule getRule(String lookahead_) {
        return _rules.get(lookahead_);
    }
    
    public Rule[] getRules() {
        // getRules() shouldn't return duplicate rules
        return new HashSet<Rule>(_rules.values()).toArray(new Rule[0]);
    }
    
    public NFAState getRoot() {
        return _root;
    }
    
    public boolean canReduce() {
        // we can reduce if we have at least one rule
        return !_rules.isEmpty();
    }

    @Override
    public void addTransition(NFAState state_, Object input_) {
        
        // add the ability to handle same inputs
        if (getInputs().contains(input_)) {
            // here we actually implement the trick: we create another
            // state as an intermediate to walkaround the limitation
            NFAState bridge = new ParserState(_root);
            bridge.addTransition(state_, input_);
            super.addTransition(bridge);
        }
        else {
            super.addTransition(state_, input_);
        }
    }

    @Override
    public String toString() {
        return super.toString() + _rules.toString();
    }

}
