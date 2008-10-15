package mini.java.fa.my;

import java.util.Queue;

/**
 * Finder interface is used as callbacks for collection traversal algorithms. It defines
 * the actually structure of the collection being traversed by returning the following
 * nodes for the current node.
 *
 * @author Alex
 */
public interface Finder<T> {
    /**
     * Returns following nodes for the give source node.
     */
    public Queue<T> findNext(T node_);
}
