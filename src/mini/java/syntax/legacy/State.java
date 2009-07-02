package mini.java.syntax.legacy;

/**
 * Wrapper around an integer/id. FIXME - reuse dfs state.
 */
public final class State {
    private Integer _id = null;

    public State(int id_) {
        _id = new Integer(id_);
    }

    @Override
    public String toString() {
        return _id.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_id == null) ? 0 : _id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final State other = (State) obj;
        if (_id == null) {
            if (other._id != null)
                return false;
        } else if (!_id.equals(other._id))
            return false;
        return true;
    }
    
    
}
