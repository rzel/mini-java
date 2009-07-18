package mini.java.syntax;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mini.java.fa.helper.Helper;
import mini.java.fa.helper.IFinder;

public class RuleSet {
    
    public final static String START = "START";
    public final static String END = "END";
    
    // fields
    private final Set<Rule> _rules = new HashSet<Rule>();
    private final Set<String> _symbols = new HashSet<String>();
    private final Set<String> _nonTerminals = new HashSet<String>();
    
    private Rule _start;
    private Set<String> _terminals; // cached
    private Map<String, Set<String>> _follows; // cached
    
    
    public RuleSet addRule(Rule rule_) {
        rule_.setParent(this); // a rule can be in only one rule set?
        if (rule_.getLeftSymbol().equals(START)) {
            if (_start != null) {
                throw new IllegalArgumentException(
                        "The rule set must contain one and only one START symbol");
            }
            
//            List<String> rhs = rule_.getRightSymbols();
//            if (!rhs.get(rhs.size() - 1).equals("END")) {
//                throw new IllegalArgumentException(
//                        "The START rule must end with END symbol");
//            }
            
            _start = rule_;
        }
        
        String lhs = rule_.getLeftSymbol();
        _rules.add(rule_);
        _nonTerminals.add(lhs);
        
        _symbols.add(lhs);
        _symbols.addAll(rule_.getRightSymbols());       
        // clear the cache; need to recalc
        _terminals = null;
        _follows = null;

        
        return this;
    }
    
    public Rule[] getRules() {
        return _rules.toArray(new Rule[0]);
    }
    
    public Set<String> getSymbols() {
        return Collections.unmodifiableSet(_symbols);
    }
    
    public Set<String> getTerminals() {
        if (_terminals == null) {
            _terminals = new HashSet<String>(_symbols);
            _terminals.removeAll(_nonTerminals);
        }
        
        return Collections.unmodifiableSet(_terminals);
    }
    
    public Set<String> getNonTerminals() {
        return Collections.unmodifiableSet(_nonTerminals);
    }
    
    public Rule getStart() {
        if (!_nonTerminals.contains(START)) {
            throw new IllegalArgumentException("The rule set must contain one and only one START symbol");
        }
        return _start;
    }
    

    @SuppressWarnings("unchecked")
    public Set<String> getFollows(String symbol_) {
        if (!_symbols.contains(symbol_)) {
            throw new IllegalArgumentException("Unknown symbol: " + symbol_);
        }
        
        if (_follows == null) {
            _follows = new HashMap<String, Set<String>>();
            
            // we have three pieces info to store for each symbol
            // info[0] - the direct follows
            // info[1] - if it's the last one of a rule, the rule's lhs
            // info[2] - if it's a lhs itself, the rule's first rhs
            Map<String, Set<String>>[] __info =
                (Map<String, Set<String>>[])Array.newInstance(Map.class, 3);
            for (int i=0; i<__info.length; ++i) {
                __info[i] = new HashMap<String, Set<String>>();
            }
            for (String symbol : getSymbols()) {
                for (int i=0; i<__info.length; ++i) {
                    __info[i].put(symbol, new HashSet<String>());
                }
            }
            final Map<String, Set<String>>
                __follows = __info[0],
                __tails = __info[1],
                __heads = __info[2];
            
            
            for (Rule rule : getRules()) {
                String lhs = rule.getLeftSymbol();
                
                String prev = null;
                for (String symbol : rule.getRightSymbols()) {
                    if (prev == null) {
                        __heads.get(lhs).add(symbol); 
                    } else {
                        __follows.get(prev).add(symbol);                        
                    }
                    prev = symbol;
                }                
                __tails.get(prev).add(lhs); // finally the tail
            }            
            __follows.get(START).add(END); // an extra follower for START
            __heads.put(END, new HashSet<String>());
            
            // now for each symbol we find its real follows
            for (String symbol : getSymbols()) {
                final Set<String> ret = new HashSet<String>();                
                
                Helper.visit(symbol, new IFinder<String>(){    
                    @Override
                    public List<String> findNext(String node_) {
                        ret.addAll(__follows.get(node_)); // add direct follows
                        return
                            // we also need to check if it is a "tail"
                            new LinkedList<String>(__tails.get(node_)); 
                    }                    
                });
                
                __expand(ret, __heads);
                _follows.put(symbol, ret);
            }            
            
        }
        return _follows.get(symbol_);
    }
    
    
    private void __expand(Set<String> ret_, Map<String, Set<String>> heads_) {
        final Set<String> ret = ret_;
        final Map<String, Set<String>> heads = heads_;
        
        Helper.visit(ret, new IFinder<String>(){
            @Override
            public List<String> findNext(String node_) {
                ret.addAll( heads.get(node_));
                return new LinkedList<String>(ret); // next...
            }
            
        });
        // all the non-terminals should be wiped out since we have already
        // expanded then into their first element(s)
        ret.removeAll(getNonTerminals());
        
    }

}
