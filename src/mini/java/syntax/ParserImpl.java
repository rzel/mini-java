package mini.java.syntax;

import java.util.LinkedList;
import java.util.List;

import mini.java.fa.DFASimulator;
import mini.java.fa.DFASimulatorImpl;
import mini.java.fa.State;

public abstract class ParserImpl implements Parser {
    private static final String END = "END";
    //private List<Symbol> _symbolList;
    private List<Symbol> _symbolStack  = new LinkedList<Symbol>();
    private List<State>  _stateStack   = new LinkedList<State>();
    private ParserConfig _parserConfig = getParserConfig();

    @Override
    public Symbol parse(List<Symbol> symbols_) {
        DFASimulator dfaSimulator = new DFASimulatorImpl(_parserConfig.getDFA());
        
        // create a new list, so the parameter won't be changed
        List<Symbol> symbols = new LinkedList<Symbol>(symbols_);
        
        // add an "END" symbol to the symbol list; so the symbol list
        // will never be empty
        symbols.add(new Symbol(END, null));

        while (!symbols.isEmpty()) { // should always be true
            Symbol symbol = symbols.remove(0);
            String symbolType = symbol.getSymbolType();
            
            // if current symbol is of type START, then we have done
            if (symbolType.equals(ParserConfig.START)) {
                return symbol;
            }
            
            dfaSimulator.step(symbolType);
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
    private Symbol reduce(Rule rule_) {
        // the expected left symbols
        List<String> symbolTypes = rule_.getLeftSymbols();

        // the start index of the production rule in the symbol stack
        int idx = _symbolStack.size() - symbolTypes.size();

        List<Symbol> children = new LinkedList<Symbol>();
        for (int i = 0; i < symbolTypes.size(); ++i) {
            Symbol symbol = _symbolStack.get(idx);

            // make sure this is the expected production rule
            String got = symbol.getSymbolType();
            String expected = symbolTypes.get(i);
            assert (expected.equals(got));

            _symbolStack.remove(idx);
            _stateStack.remove(idx);

            // and create a new symbol with the left symbols of the production
            // rule as the children symbols
            children.add(symbol);
        }

        return new Symbol(rule_.getRightSymbol(), null, children);
    }
    
    /**
     * Returns the specific parser config for this parser. Subclasses should
     * override this method to provide a ParserConfig instance for the parsing
     * algorithm.
     */
    public abstract ParserConfig getParserConfig();
}
