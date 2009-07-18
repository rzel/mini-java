package mini.java.fa.helper;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import mini.java.fa.NFAClosure;

public class NFAClosureFinder implements IFinder<NFAClosure> {
    private IFinderCallback<NFAClosure> _cb;
    
    public NFAClosureFinder(IFinderCallback<NFAClosure> cb_) {
        if (cb_ == null) {
            throw new IllegalArgumentException("IFinderCallback cannot be null");
        }
        _cb = cb_;
    }

    @Override
    public List<NFAClosure> findNext(NFAClosure node_) {
        List<NFAClosure> ret = new LinkedList<NFAClosure>();
        
        // sort the closures by inputs
        Set<Object> inputs = new TreeSet<Object>(Helper.STR_CMP);
        inputs.addAll(node_.getInputs());
        
        for (Object input : inputs) {
            NFAClosure target = node_.getClosure(input);
            if (_cb.onNext(node_, target, input)) {
                ret.add(target);
            }
        }
        return ret;
    }

}
