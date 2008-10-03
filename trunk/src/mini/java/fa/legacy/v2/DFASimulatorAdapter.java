package mini.java.fa.legacy.v2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mini.java.fa.Acceptable;
import mini.java.fa.DFASimulator;
import mini.java.fa.InitialState;
import mini.java.fa.State;
import mini.java.fa.legacy.v1.InputConvertor;

public class DFASimulatorAdapter implements DFASimulator {
    private mini.java.fa.DFA       _dfa;
    private DFA                    _simpleDFA;
    private Map<State, DFANode>    _states;
//    private Map<Object, Character> _inputs;
//    private char                   _charValue;
    private InputConvertor      _inputConvertor;
    private DFAState            _currStatus;
//    private DFAState            _prevStatus;
    
    // Constructor
    public DFASimulatorAdapter(mini.java.fa.DFA dfa_) {
        _dfa = dfa_;
        _simpleDFA = new SimpleDFA();
        _states = new HashMap<State, DFANode>();
//        _inputs = new HashMap<Object, Character>();
//        _charValue = 'A';
        _inputConvertor = new InputConvertor();
        _currStatus = DFAState.RUNNING;
//        _prevStatus = DFAState.RUNNING;

        // CONVERT DFA to SimpleDFA
        Set<State> checkedStates = new HashSet<State>();
        List<State> uncheckedStates = new LinkedList<State>();

        // 1. get the initial state from the DFA
        InitialState initialState = _dfa.getInitialState();
        // add a start node for the initial state
        _states.put(initialState, DFANode.getStartNode());
        uncheckedStates.add(initialState);
        while (!uncheckedStates.isEmpty()) {
            // 2. get the source state from the DFA
            State sourceState = uncheckedStates.remove(0);
            // 2.5 get the source node
            DFANode sourceNode = _states.get(sourceState);
            assert (sourceNode != null);

            // 3. mark source state as checked
            checkedStates.add(sourceState);

            // 4. iterate though all possible inputs
            for (Object input : _dfa.getInputs(sourceState)) {
//                 //5. get the input object
//                if (!_inputs.containsKey(input)) {
//                    _inputs.put(input, _charValue++);
//                }
//                Character ch = _inputs.get(input);
//                assert (ch != null);
                
                // 5. get the input object
                Character ch = _inputConvertor.convert(input);

                // 6. get the target state
                State targetState = _dfa.getState(sourceState, input);
                assert (targetState != null);

                // 6.5 get the target node
                if (!_states.containsKey(targetState)) {
                    _states.put(targetState, new DFANode(
                            _states.keySet().size()));
                }
                DFANode targetNode = _states.get(targetState);
                assert (targetNode != null);
                
                if (targetState instanceof Acceptable) {
                    _simpleDFA.addAcceptedNode(targetNode);
                }


                // 7. add the transition to the SimpleDFA
                Transition transition = new Transition(sourceNode, targetNode);
                transition.addInput(ch);
                _simpleDFA.addTransition(transition);

                // 8. continue te process
                if (!checkedStates.contains(targetState)) {
                    uncheckedStates.add(targetState);
                }
            }
        }
    }

    @Override
    public State getDFAState() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isRunning() {
        return (_currStatus == DFAState.RUNNING);
    }

    @Override
    public void reset() {
        _simpleDFA.reset();
        _currStatus = DFAState.RUNNING;
    }

    @Override
    public void setDFAState(State state_) {
        // TODO Auto-generated method stub

    }

    @Override
    public void step(Object input_) {
        if (_currStatus == DFAState.RUNNING) {
            Character ch = _inputConvertor.convert(input_);
//            _prevStatus = _currStatus;
            _currStatus = _simpleDFA.feed(ch);
        }
    }

}
