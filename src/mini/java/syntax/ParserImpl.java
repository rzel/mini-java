package mini.java.syntax;

import java.util.LinkedList;
import java.util.List;

import mini.java.fa.v3.DFASimulator;
import mini.java.fa.v3.State;
import mini.java.fa.v3.impl.DFASimulatorImpl;

public abstract class ParserImpl implements Parser {
    private static final String END = "END";

    private List<Symbol> _symbolStack  = new LinkedList<Symbol>();
    private List<State>  _stateStack   = new LinkedList<State>();
    private ParserConfig _parserConfig = getParserConfig();

    @Override
    public NonTerminal parse(List<Terminal> terminals_) {
        DFASimulator dfaSimulator = new DFASimulatorImpl(_parserConfig.getDFA());
        
        // create a new list, so the parameter won't be changed
        List<Symbol> symbols = new LinkedList<Symbol>(terminals_);
        
        // add an "END" symbol to the symbol list;
        // so the symbol list will never be empty
        symbols.add(new Terminal(END));

        while (!symbols.isEmpty()) { // should always be true
            Symbol symbol = symbols.remove(0);
            String type = symbol.getType();
            
            // if current symbol is of type START, then we have done
            if (type.equals(ParserConfig.START)) {
                assert(symbol instanceof NonTerminal);
                return((NonTerminal) symbol);
            }
            
            dfaSimulator.step(type);
            // NOTE: if the dfaSimulator has stopped, then the
            // dfaState returned is the previous state
            State dfaState = dfaSimulator.getDFAState();
            
            if (dfaSimulator.isRunning()) {
                _stateStack.add(dfaState);
                _symbolStack.add(symbol);
            } else {
                // we are ready for reduction; first get the production rule
                Rule rule = _parserConfig.getRule(dfaState);
                // TODO throw IllegalSyntaxException
                assert(rule != null);
                
                // unshift the extra symbol and the reduction result back
                // to the symbol list
                symbols.add(0, symbol);
                symbols.add(0, reduce(rule));
                
                // revert back to the previous DFA state
                if (_stateStack.isEmpty()) {
                    dfaSimulator.reset();
                } else {
                    // set DFA state to the last state in the stack
                    dfaSimulator.setDFAState(_stateStack.get(_stateStack.size()-1));
                }
            }
        }
        
        return null; // SYNTAX ERROR!
    }
    
    // Helper function used to reduce the production rules
    // NOTE: this will also clear the _symbolStack and the _stateStack
    private NonTerminal reduce(Rule rule_) {
        // the expected left symbols
        List<String> types = rule_.getRightSymbols();

        // the start index of the production rule in the symbol stack
        int idx = _symbolStack.size() - types.size();

        List<Symbol> children = new LinkedList<Symbol>();
        for (int i = 0; i < types.size(); ++i) {
            Symbol symbol = _symbolStack.get(idx);

            // make sure this is the expected production rule
            String got = symbol.getType();
            String expected = types.get(i);
            assert (expected != null);
            assert (expected.equals(got));

            _symbolStack.remove(idx);
            _stateStack.remove(idx);

            // and create a new symbol with the left symbols of the production
            // rule as the children symbols
            children.add(symbol);
        }

        return new NonTerminal(rule_.getLeftSymbol(), children);
    }
    
    /**
     * Returns the specific parser config for this parser. Subclasses should
     * override this method to provide a ParserConfig instance for the parsing
     * algorithm.
     */
    public abstract ParserConfig getParserConfig();
}
