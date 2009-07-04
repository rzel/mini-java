package mini.java.fa.helper;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import mini.java.fa.NFAState;

public class NFAStateFinder implements IFinder<NFAState> {
    private IFinderCallback<NFAState> _cb;
    
    public NFAStateFinder(IFinderCallback<NFAState> cb_) {
        if (cb_ == null) {
            throw new IllegalArgumentException("IFinderCallback cannot be null");
        }
        _cb = cb_;
    }

    @Override
    public Queue<NFAState> findNext(NFAState node_) {
        Queue<NFAState> ret = new LinkedList<NFAState>();
        // first the epsilons, ordered according to target states
        Set<NFAState> epsilons = new TreeSet<NFAState>(Helper.STR_CMP);
        epsilons.addAll(node_.getEpsilons());
        for (NFAState s : epsilons) {
            if (_cb.onNext(node_, s, null)) {
                ret.add(s);
            }
        }
        
        // the normal transitions, ordered according to inputs
        Set<Object> inputs = new TreeSet<Object>(Helper.STR_CMP);
        inputs.addAll(node_.getInputs());
        for (Object input : inputs) {
            NFAState target = node_.getState(input);
            if (_cb.onNext(node_, target, input)) {
                ret.add(target);
            }
        }
        return ret;
    }

}
