package mini.java.fa.helper;

import java.util.Queue;


/**
 * Finder interface is used as callbacks for collection traversal algorithms. It defines
 * the actual structure of the collection being traversed by returning the following
 * nodes of the current node.
 */
public interface IFinder<T> {
    public Queue<T> findNext(T node_);
}
