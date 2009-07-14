package mini.java.syntax;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ParserV2Impl {
    
    private final IParserConfigV2 _conf;
    private final static Terminal END = new Terminal(RuleSet.END);
    
    public ParserV2Impl(IParserConfigV2 conf_) {
        _conf = conf_;
    }
    
    public ParserV2Impl(RuleSet rules_) {
        _conf = new ParserConfigV2Impl(rules_);
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
        
        
        stack.add(_conf.getEngine());
        
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
                throw new RuntimeException("Parsing error: Illegal syntax at " + nextSymbol);
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
        
//        if (out.size() != 1) {
//            throw new RuntimeException("Parsing error: Missing elements...");
//        }
//        
//        NonTerminal ret = (NonTerminal)out.get(0);
//        if (!ret.getType().equals(RuleSet.START)) {
//            throw new RuntimeException("Parsing error: Illegal grammar -- " + _conf.getRuleSet());
//        }
//        
//        return ret;
        
        throw new RuntimeException("Parsing error: Reaching the end of input queue.");
    }
}
