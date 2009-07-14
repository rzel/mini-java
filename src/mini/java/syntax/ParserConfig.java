package mini.java.syntax;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mini.java.ComponentFactory;
import mini.java.fa.v3.DFA;
import mini.java.fa.v3.DFASimulator;
import mini.java.fa.v3.InitialState;
import mini.java.fa.v3.NFABuilder;
import mini.java.fa.v3.State;
import mini.java.fa.v3.impl.DFASimulatorImpl;

/**
 * ParserConfig contains all the information a Parser needs to do the parsing.
 * It maintains a syntax specification which consists a set of production rules.
 * It can also create a DFA for the Parser to ease the parsing process.
 * 
 * @author Alex
 */

public class ParserConfig {
    // NOTE: there should be one and only one such START symbol
    public static final String START = "START";

    private Set<Rule> _rules;
    private DFA       _dfa;

    // mapping from DFA state to the corresponding production rule
    // NOTE: don't be confused with the NFA states
    private Map<State, Rule>    _ruleMap;
    
    /**
     * Constructor. Reads in syntax specification from the given stream and
     * parses it into production rules.
     * 
     * NOTE: ParserConfig is immutable object.
     * NOTE: Production rule specifications are separated by line feeds.
     */
    public ParserConfig(Reader reader_) throws IOException {
        _rules = new HashSet<Rule>();

        String ruleSpec;
        BufferedReader reader = new BufferedReader(reader_);
        // read in the syntax specifications and create production rules
        while ((ruleSpec = reader.readLine()) != null) {
            Rule rule = Rule.createRule(ruleSpec);
            // TODO Rule.parse should throw exception instead of returning null
            if (rule != null) {
                _rules.add(rule);
            }
        }        
    }

    public Set<Rule> getRules() {
        return Collections.unmodifiableSet(_rules);
    }

    /**
     * Returns the DFA represented by the syntax specification.
     */
    public DFA getDFA() {
        // lazy initialization
        if (_dfa == null)
            initDFA();
        
        return _dfa;
    }
    
    // helper function used to create the DFA
    private void initDFA() {
        assert(_rules != null);
        
        // the DFA will be built using an NFA
        NFABuilder nfaBuilder = ComponentFactory.createNFABuilder();
        
        // mapping from non-terminal symbols to its corresponding NFA state
        Map<String, State> nfaStates = new HashMap<String, State>();
        
        for (Rule rule : _rules) {
            // 1. create NFA states for non-terminal symbols
            String rightSymbol = rule.getLeftSymbol();
            if (!nfaStates.containsKey(rightSymbol)) {
                // NOTE: START is a virtual symbol that serves as a start
                // point for every syntax specifiation
                nfaStates.put(rightSymbol, rightSymbol.equals(START)
                        ? new InitialState() : new State());
            }

            // link the right symbol to the left symbols
            nfaBuilder.addTransition(nfaStates.get(rightSymbol),
                    rule.getItems().get(0));            

            // 2. walk through the left symbols; create transitions
            List<String> symbols = rule.getRightSymbols();
            List<State> items = rule.getItems();
            for (int i = 0; i < symbols.size(); ++i) {
                nfaBuilder.addTransition(items.get(i),
                        items.get(i + 1), symbols.get(i));
            }
        }
        
        // 3. add epsilon transitions as alias for non-terminal symbols
        for (Rule rule : _rules) {
            List<String> symbols = rule.getRightSymbols();
            List<State> items = rule.getItems();
            for (int i = 0; i < symbols.size(); ++i) {
                String symbol = symbols.get(i);
                // find non terimnals among the left symbols
                if (nfaStates.containsKey(symbol)) {
                    nfaBuilder.addTransition(items.get(i),
                            nfaStates.get(symbol));
                }
            }
        }

        // 4. create the DFA for the underlying syntax specification
        // TODO if the NFA or the DFA cannot be built, throw an exception
        _dfa = mini.java.fa.helper.Helper.collapse(nfaBuilder.buildNFA());
        
        //assert(_dfa != null);
    }

    /**
     * Returns the corresponding production rule for the given DFA state.
     * NOTE: Don't be confused with the NFA state.
     */
    public Rule getRule(State _dfaState) {
        // lazy initialization
        if (_ruleMap == null)
            initRuleMap();
        
        return _ruleMap.get(_dfaState);
    }
    
    // helper function used to initialize the rule mapping.
    private void initRuleMap() {
        assert(_dfa     != null);
        assert(_rules   != null);
        
        _ruleMap = new HashMap<State, Rule>();
        
        // mapping from non-terminal symbols to its corresponding DFA states
        // NOTE: all DFA states should be equivalent after the DFA being reduced
        // to its minimal version
        Map<String, Set<State>> dfaStates = new HashMap<String, Set<State>>();        
        
        // 1. visit the DFA to initialize the non-terminal to dfa states mapping
        Set<State> checkedStates = new HashSet<State>();
        List<State> uncheckedStates = new LinkedList<State>(
                Arrays.asList(_dfa.getInitialState())); 
        while (!uncheckedStates.isEmpty()) {
            State sourceState = uncheckedStates.remove(0);
            checkedStates.add(sourceState);
            // NOTE: this is BFS
            for (Object input : _dfa.getInputs(sourceState)) {                
                String symbol = (String)input;
                // create the state set if there is none
                if (!dfaStates.containsKey(symbol)) {
                    dfaStates.put(symbol, new HashSet<State>());
                }
                
                // add the source state to the state set
                // NOTE: not the target state, or it need to go back one step
                dfaStates.get(symbol).add(sourceState);
                
                // add the target state to the unchecked state list
                State targetState = _dfa.getState(sourceState, symbol);
                if (!checkedStates.contains(targetState)) {
                    uncheckedStates.add(targetState);
                }
            }
        }
        
        // 2. build the mapping from the DFA states to the production rules
        DFASimulator dfaSimulator = new DFASimulatorImpl(_dfa);
        for (Rule rule : _rules) {
            String rightSymbol = rule.getLeftSymbol();
            State targetState = null;
            Set<State> sourceStates = dfaStates.containsKey(rightSymbol)
                ? dfaStates.get(rightSymbol)
                // for START symbol; it will never appear in the DFA
                : Collections.singleton((State)_dfa.getInitialState());
                
            for (State sourceState : sourceStates) {
                // start from the source state
                //dfaSimulator.reset();
                dfaSimulator.setDFAState(sourceState);
                for (String symbol : rule.getRightSymbols()) {
                    // walk through all the left symbols
                    dfaSimulator.step(symbol);
                    assert (dfaSimulator.isRunning());
                }

                if (targetState == null) {
                    targetState = dfaSimulator.getDFAState();
                    //assert(targetState instanceof AcceptableState);
                } else {
                    // make sure this is a SIMPLE SYNTAX
                    // TODO throw IllegalSyntaxException
                    assert (targetState == dfaSimulator.getDFAState());
                }
            }
            
            assert (targetState != null);
            _ruleMap.put(targetState, rule);
        }
    }
}
