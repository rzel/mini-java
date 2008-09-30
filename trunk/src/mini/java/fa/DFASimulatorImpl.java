package mini.java.fa;

public class DFASimulatorImpl implements DFASimulator {
    private boolean _running;
    private DFA     _dfa;
    private State   _dfaState;

    public DFASimulatorImpl(DFA dfa_) {
        _dfa = dfa_;
        _dfaState = dfa_.getInitialState();
        _running = true;
    }

    @Override
    public State getDFAState() {
        return _dfaState;
    }

    @Override
    public boolean isRunning() {
        return _running;
    }

    @Override
    public void reset() {
        //_dfaState = _dfa.getInitialState();
        setDFAState(_dfa.getInitialState());
        _running = true;
    }

    @Override
    public void step(Object input_) {
        // if the simulator is not running, ignore all following inputs
        if (_running) {
            State newState = _dfa.getState(_dfaState, input_);
            if (newState != null) {
                _dfaState = newState;
            } else {
                // if there's no valid transition, stop the simulator
                _running = false;
            }
        }
    }

    @Override
    public void setDFAState(State state_) {
        _dfaState = state_;        
    }
}
