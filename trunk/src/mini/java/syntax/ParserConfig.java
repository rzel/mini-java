package mini.java.syntax;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import mini.java.fa.NFAClosure;
import mini.java.fa.NFAState;
import mini.java.fa.helper.Helper;
import mini.java.fa.helper.IFinderCallback;
import mini.java.fa.helper.NFAClosureFinder;
import mini.java.fa.helper.NFAStateFinder;

public class ParserConfig {
    private static boolean DEBUG = false;
    // fields
    private final RuleSet      _rules;
    private final ParserState  _engine;
    
    
    public ParserConfig(RuleSet rules_) {
        if (rules_ == null || rules_.getRules().length <= 0) {
            throw new IllegalArgumentException("Invalid rule set: null/empty");
        }
        _rules = rules_;
        
        
        // **Generates the NFA represented by the rule set
        ParserState nfaRoot = ParserState.createRoot(); // this is the root for NFA
        final Map<String, ParserState> nonTerminalStates = new HashMap<String, ParserState>();
        
        // create an NFA state for each of the non-terminals
        {
            nonTerminalStates.put(RuleSet.START, nfaRoot);
            
            Set<String> nonTerminals = new HashSet<String>(_rules.getNonTerminals());
            nonTerminals.remove(RuleSet.START); // already added
            for (String nonTerminal : nonTerminals) {
                nonTerminalStates.put(nonTerminal, new ParserState(nfaRoot));
            }
        }
        
        
        for (Rule rule : _rules.getRules()) {
            // "items" are presented by NFA states
            String lhs = rule.getLeftSymbol();
            ParserState prev = nonTerminalStates.get(lhs);
            
            for (String rhs : rule.getRightSymbols()) {                
                ParserState curr = new ParserState(nfaRoot);
                prev.addTransition(curr, rhs);
                // for non-terminals, add an epsilon to their corresponding nfa states
                if (nonTerminalStates.containsKey(rhs)) {
                    prev.addTransition(nonTerminalStates.get(rhs));
                }
                prev = curr;
            }            
            
            prev.addRule(rule); // add the rule to the final state
        }
        
        if (DEBUG) {
            final StringBuilder sb = new StringBuilder();
            Helper.visit(nfaRoot, new NFAStateFinder(
                    new IFinderCallback<NFAState>() {
                        public boolean onNext(NFAState src_, NFAState dest_, Object input_) {
                            sb.append(String.format("%s => %s(%s)%n",
                                    src_, dest_, input_));
                            return true;
                        }
                    }));
            System.err.print(sb);
            System.err.println("----");
        }
        
        // **Generates the actual parser engine
        final Map<NFAClosure, NFAState> mapping = new HashMap<NFAClosure, NFAState>();
        _engine = ParserState.createRoot();
        NFAClosure init = nfaRoot.getClosure();
        mapping.put(init, _engine);
        
        Helper.visit(init, new NFAClosureFinder(
                new IFinderCallback<NFAClosure>() {
                    public boolean onNext(NFAClosure src_, NFAClosure dest_, Object input_) {
                        assert(input_ != null);
                        
                        if (!mapping.containsKey(dest_)) { // new mapping
                            mapping.put(dest_, buildEngine(dest_));
                        }
                        
                        // add the corresponding transitions in the newly created DFA
                        NFAState src = mapping.get(src_);
                        src.addTransition(mapping.get(dest_), input_);
                        return true;
                    }
                }));
    }
    
    
    // create states and add them to the engine(DFA)
    private ParserState buildEngine(NFAClosure closure_) {
        ParserState ret = new ParserState(_engine);
        // copy the rules from the NFA to the engine
        for (NFAState state : closure_.getStates()) {
            ParserState st = (ParserState)state;
            if (st.canReduce()) {
                ret.addRules(st.getRules());
            }
        }
        
        return ret;
    }
    

    public ParserState getEngine() {
        return _engine;
    }


    public RuleSet getRuleSet() {
        return _rules; 
    }

}
