package mini.java.syntax;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mini.java.fa.NFAClosure;
import mini.java.fa.NFAState;
import mini.java.fa.helper.Helper;
import mini.java.fa.helper.IFinderCallback;
import mini.java.fa.helper.NFAClosureFinder;
import mini.java.fa.helper.NFAStateFinder;

public class Parser {
    
    private static boolean DEBUG = false;
    
    private final ParserState _engine;
    private final RuleSet _rules;
    private final static Terminal END = new Terminal(RuleSet.END);

    
    public Parser(RuleSet rules_) {
        _rules = rules_;
        _engine = buildEngine(rules_);
    }
    
    public ParserState getEngine() {
        return _engine;
    }


    public RuleSet getRuleSet() {
        return _rules; 
    }

    /**
     * New interface.
     */
    public NonTerminal parse(Terminal... tokens_) {
        
        List<Symbol> in = new LinkedList<Symbol>();
        in.addAll(Arrays.asList(tokens_));
        in.add(END); // as the end
        
        List<Symbol> out = new LinkedList<Symbol>();
        List<ParserState> stack = new LinkedList<ParserState>();
        
        
        stack.add(_engine);
        
        while (!in.isEmpty()) { // should never be empty
            Symbol nextSymbol = in.remove(0);
            if (RuleSet.START.equals(nextSymbol.getType())) {
                if (in.size() != 1 && in.get(0) != END) {
                    throw new RuntimeException("Parsing error: Extra trailing symbols.");
                }
                
                return (NonTerminal)nextSymbol;
            }
            if (nextSymbol == END) {
                throw new RuntimeException("Parsing error: END symbol encountered.");
            }
            ParserState currState = stack.get(stack.size() - 1);
            ParserState nextState = (ParserState)currState.getState(nextSymbol.getType());
            
            if (nextState == null) {
                throw new RuntimeException("Parsing error: Illegal syntax at <" + nextSymbol + ">");
            }
            
            out.add(nextSymbol);
            stack.add(nextState);
            
            if (nextState.canReduce()) {
                Symbol lookahead = in.get(0); // peek
                Rule rule = nextState.getRule(lookahead.getType());
                if (rule != null)
                {
                    {
                        // we should reduce
                        int toIndex = out.size();
                        int fromIndex = toIndex - rule.getRightSymbols().size();
                        List<Symbol> children = out.subList(fromIndex, toIndex);
                        
                        NonTerminal node = new NonTerminal(rule.getLeftSymbol(), rule).addChildren(children);
                        in.add(0, node); // puch the new NonTerminal back to the input queue
                        
                        children.clear();
                    }
                    int toIndex = stack.size();
                    int fromIndex = toIndex - rule.getRightSymbols().size();
                    stack.subList(fromIndex, toIndex).clear();   
                }
                else
                {
                    // otherwise we should shift...
                }
                
             
            }
        }
        
        throw new RuntimeException("Parsing error: Reaching the end of input queue.");
    }
    
    
    protected static ParserState buildEngine(RuleSet rules_) {
        if (rules_ == null || rules_.getRules().length <= 0) {
            throw new IllegalArgumentException("Invalid rule set: null/empty");
        }
        
        
        // **Generates the NFA represented by the rule set
        ParserState nfaRoot = ParserState.createRoot(); // this is the root for NFA
        final Map<String, ParserState> nonTerminalStates = new HashMap<String, ParserState>();
        
        // create an NFA state for each of the non-terminals
        {
            nonTerminalStates.put(RuleSet.START, nfaRoot);
            
            Set<String> nonTerminals = new HashSet<String>(rules_.getNonTerminals());
            nonTerminals.remove(RuleSet.START); // already added
            for (String nonTerminal : nonTerminals) {
                nonTerminalStates.put(nonTerminal, new ParserState(nfaRoot));
            }
        }
        
        
        for (Rule rule : rules_.getRules()) {
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
        
        __dumpEngine(nfaRoot);
        
        
        // **Generates the actual parser engine
        final Map<NFAClosure, NFAState> mapping = new HashMap<NFAClosure, NFAState>();
        final ParserState engine = ParserState.createRoot();
        NFAClosure init = nfaRoot.getClosure();
        mapping.put(init, engine);
        
        Helper.visit(init, new NFAClosureFinder(
                new IFinderCallback<NFAClosure>() {
                    public boolean onNext(NFAClosure src_, NFAClosure dest_, Object input_) {
                        assert(input_ != null);
                        
                        if (!mapping.containsKey(dest_)) { // new mapping
                            ParserState engineState = new ParserState(engine);
                            // copy the rules from the NFA to the engine
                            for (NFAState state : dest_.getStates()) {
                                // XXX - hack
                                if (state instanceof ParserState) {
                                    ParserState st = (ParserState)state;
                                    if (st.canReduce()) {
                                        engineState.addRules(st.getRules());
                                    }
                                }
                            }
                            mapping.put(dest_, engineState);
                        }
                        
                        // add the corresponding transitions in the newly created DFA
                        NFAState src = mapping.get(src_);
                        src.addTransition(mapping.get(dest_), input_);
                        return true;
                    }
                    
                }));
        
        
        __dumpEngine(engine);
        
        return engine;
    }
    
    
    private static void __dumpEngine(NFAState root_) {
        if (DEBUG) {
            final StringBuilder sb = new StringBuilder();
            Helper.visit(root_, new NFAStateFinder(
                    new IFinderCallback<NFAState>() {
                        public boolean onNext(NFAState src_, NFAState dest_, Object input_) {
                            sb.append(String.format("%s => %s(%s)%n",
                                    src_, dest_, input_));
                            return true;
                        }
                    }));
            System.err.print(sb);
            System.err.println("----__dumpEngine()\n");
        }
    }

}
