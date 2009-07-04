package mini.java.fa.helper;

/**
 * Callback used by Finder on each of the new transitions found.
 */
public interface IFinderCallback<T> {
    // (input == null) for epsilons
    public boolean onNext(T src_, T dest_, Object input_);
}
